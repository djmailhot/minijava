/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import SemanticAnalyzer.SemanticTypes.*;

public class CodeGenerator {

  private static final String[] PARAM_INTEGER_REGISTERS =
    { "%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9" };
  private static final String[] PARAM_DOUBLE_REGISTERS =
    { "%xmm0", "%xmm1", "%xmm2", "%xmm3", "%xmm4", "%xmm5" };

  // The runtime error codes that our compiled program can exit with.
  private static final int ERR_OUT_OF_BOUNDS = 10;
  private static final int ERR_NEG_ARRAY_SIZE = 11;

  private PrintStream outputStream;
  private final boolean statementCounting;

  private int labelCounter;
  private Map<String, Integer> localOffsets;

  /** The number of bytes allocated for arguments and locals. */
  private int localSegmentSize;

  /** The number of 8-byte values on the current function's expression stack. */
  private int itemsOnStack;

  private int maxLineNumber;

  public String assemblerPrefixName;

  /**
   * Constructs a new CodeGenerator which outputs to the given file. Outputs to
   * stdout if the given filename is "stdout" or null.
   */
  public CodeGenerator(String outputFileName, boolean statementCounting) {
    if (outputFileName != null && outputFileName != "stdout") {
      try {
        outputStream = new PrintStream(outputFileName);
      } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
      }
    } else {
      outputStream = System.out;
    }

    String osName = System.getProperty("os.name");
    // System.out.println("os.name is " + System.getProperty("os.name"));
    if (osName.equals("Mac OS X")) {
      assemblerPrefixName = "_";
    } else {
      assemblerPrefixName = "";
    }

    this.statementCounting = statementCounting;
  }

  /**
   * Returns a unique assembler label name for the given class's vtable.
   *
   * Mimics g++ name mangling, for fun.
   */
  private String vtblName(String className) {
    StringBuilder sb = new StringBuilder();
    sb.append("_ZTV")
      .append(className.length())
      .append(className);

    return sb.toString();
  }

  /**
   * Returns an assembler label name uniquely identifying the given method.
   *
   * Mimics g++ name mangling, for fun.
   */
  private String mangle(String className, MethodMetadata method) {
    StringBuilder sb = new StringBuilder();
    sb.append("_ZN")
      .append(className.length())
      .append(className)
      .append(method.name.length())
      .append(method.name)
      .append("E");

    if (method.params.isEmpty()) {
      sb.append("v");
    } else {
      for (VarType param : method.params.values()) {
        if (param instanceof Primitive) {
          if (param == Primitive.INT_ARRAY || param == Primitive.DOUBLE_ARRAY)
            sb.append("P");
          sb.append(param.toString().charAt(0));
        } else {
          sb.append(param.toString().length())
            .append(param.toString());
        }
      }
    }

    return sb.toString();
  }

  /**
   * Generates code that exits the minijava program with the given error code.
   */
  private void genRuntimeError(int errorCode) {
    switch (errorCode) {
    case ERR_OUT_OF_BOUNDS:
      printComment("index out of bounds");
      break;
    case ERR_NEG_ARRAY_SIZE:
      printComment("negative array size");
      break;
    default:
      printComment("unknown error");
      break;
    }
    printInsn("movq", "$"+errorCode, "%rdi");
    genCall(assemblerPrefixName + "exit");
  }

  /**
   * Generates the prelude to a function body.
   */
  public void genFunctionEntry(String functionName) {
    printComment("entry point for " + assemblerPrefixName + functionName);
    printSection(".text");
    printGlobalName(functionName);
    printLabel(functionName);

    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");

    itemsOnStack = 0;
  }

  /**
   * Generates the end of a function body.
   */
  public void genFunctionExit(String functionName) {
    printComment("return point for " + assemblerPrefixName + functionName);
    printInsn("popq", "%rbp");
    printInsn("ret");
  }

  /**
   * Generates the prelude to a method body. Ensures that the stack pointer is
   * aligned to a multiple of 16 afterwards.
   */
  public void genMethodEntry(String className, MethodMetadata method) {
    printComment("entry point for " + className + "." + method + "()");
    printLabel(mangle(className, method));

    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");

    // Store callee-saved registers
    printInsn("pushq", "%rbx");
    int savedRegistersSize = 8;

    // Allocate space for `this` pointer, parameters, and local variables
    localSegmentSize = 8 * (1 + method.params.size() + method.localVars.size());

    // Align to 16 byte multiple to help with stack alignment.
    if ((savedRegistersSize + localSegmentSize) % 16 != 0)
      localSegmentSize += 8;

    printInsn("subq", String.format("$%d", localSegmentSize), "%rsp");

    // Store `this` pointer
    printInsn("movq", PARAM_INTEGER_REGISTERS[0], String.format("-%d(%%rbp)", 8));

    // Store arguments and keep track of their offsets
    localOffsets = new HashMap<String, Integer>();
    for (String argName : method.params.keySet()) {
      VarType argType = method.params.get(argName);

      int offset = 8 * (localOffsets.size() + 2);

      if (argType == Primitive.DOUBLE) {
        printInsn("movsd", PARAM_DOUBLE_REGISTERS[localOffsets.size() + 1],
            String.format("-%d(%%rbp)", offset));
      } else {
        printInsn("movq", PARAM_INTEGER_REGISTERS[localOffsets.size() + 1],
            String.format("-%d(%%rbp)", offset));
      }
      localOffsets.put(argName, offset);
    }

    // Track offsets for locals
    for (String localName : method.localVars.keySet()) {
      int offset = 8 * (localOffsets.size() + 2);
      localOffsets.put(localName, offset);
    }

    itemsOnStack = 0;
  }

  /**
   * Generates the end of a method body.
   */
  public void genMethodExit(String className, MethodMetadata method) {
    printComment("return point for " + className + "." + method + "()");

    // Pop return value
    if (method.returnType == Primitive.DOUBLE) {
      genPopDouble("%xmm0");
    } else {
      printInsn("popq", "%rax");
    }
    itemsOnStack--;

    if (itemsOnStack != 0) {
      System.out.println("Warning: " + itemsOnStack + " values were left on the "
          + "expression stack at return from " + className + "." + method + "().");
    }

    // Free the space used by arguments and local variables
    printInsn("addq", String.format("$%d", localSegmentSize), "%rsp");
    localOffsets = null;
    localSegmentSize = 0;

    // Restore callee-saved registers
    printInsn("popq", "%rbx");

    printInsn("popq", "%rbp");
    printInsn("ret");
  }

  /**
   * Generates a call to the given function.
   */
  private void genCall(String functionName) {
    // Align the stack pointer to a 16-byte multiple, if needed
    if (itemsOnStack % 2 != 0)
      printInsn("subq", "$8", "%rsp");

    printInsn("call", assemblerPrefixName + functionName);

    // Restore the stack pointer
    if (itemsOnStack % 2 != 0)
      printInsn("addq", "$8", "%rsp");
  }

  /**
   * Generates a call to the given method.
   */
  public void genMethodCall(ClassVarType classType, MethodMetadata method) {
    int offset = classType.getMethodOffset(method.name);

    // Align the stack pointer to a 16-byte multiple, if needed
    if (itemsOnStack % 2 != 0)
      printInsn("subq", "$8", "%rsp");

    printInsn("movq", "(%rdi)", "%rax");  // look up vtbl pointer
    printInsn("callq", "*"+offset+"(%rax)");  // call method

    // Restore the stack pointer
    if (itemsOnStack % 2 != 0)
      printInsn("addq", "$8", "%rsp");

    if (method.returnType == Primitive.DOUBLE) {
      genPushDouble("%xmm0");
    } else {
      printInsn("pushq", "%rax");
    }
    itemsOnStack++;
  }

  /**
   * Generates code to load a value off the expression stack into the integer 
   * argument register for the given argument position.
   */
  public void genIntegerActual(int position) {
    if (position > 5) {
      System.err.println("Encountered a function with more than 5 explicit arguments.");
      System.exit(0);
    }
    String register = PARAM_INTEGER_REGISTERS[position];
    printInsn("popq", register);
    itemsOnStack--;
  }

  /**
   * Generates code to load a value off the expression stack into the double
   * argument register for the given argument position.
   */
  public void genDoubleActual(int position) {
    if (position > 5) {
      System.err.println("Encountered a function with more than 5 explicit arguments.");
      System.exit(0);
    }
    String register = PARAM_DOUBLE_REGISTERS[position];
    genPopDouble(register);
    itemsOnStack--;
  }

  public void genPopDouble(String register) {
    printInsn("movsd", "(%rsp)", register);  // read from the top of the stack
    printInsn("addq", "$8", "%rsp");  // manually pop the stack pointer
  }

  public void genPushDouble(String register) {
    printInsn("subq", "$8", "%rsp");  // manually pop the stack pointer
    printInsn("movsd", register, "(%rsp)");  // write to the top of the stack
  }

  /**
   * Pushes a pointer to the current `this` object onto the expression stack.
   */
  public void genThis() {
    printInsn("movq", "-8(%rbp)", "%rdi");
    printInsn("pushq", "%rdi");
    itemsOnStack++;
  }

  public void genIntegerConstant(int value) {
    printInsn("movq", String.format("$%d", value), "%rax");
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  public void genDoubleConstant(double value) {
    long bits = Double.doubleToLongBits(value);

    printInsn("movabsq", String.format("$%d", bits), "%rax");
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  /**
   * Generates and returns unique label name containing the given string.
   */
  public String newLabel(String labelName) {
    String label = "L" + labelCounter + labelName;
    labelCounter += 1;
    return label;
  }

  /**
   * Prints the given label with the platform-specific assembler prefix.
   */
  public void printLabel(String labelName) {
    outputStream.println(assemblerPrefixName + labelName + ":");
  }

  /**
   * Prints the given label without the platform-specific assembler prefix.
   * Useful for printing local asm labels.
   */
  public void printLocalLabel(String labelName) {
    outputStream.println(labelName + ":");
  }

  private void printSection(String directive) {
    outputStream.println(directive);
  }

  private void printGlobalName(String name) {
    outputStream.println(".globl " + assemblerPrefixName + name);
  }

  private void printInsn(String opcode) {
    outputStream.print("\t");
    outputStream.println(opcode);
  }

  private void printInsn(String opcode, String op1) {
    outputStream.print("\t");
    outputStream.print(opcode);
    outputStream.print("\t");
    outputStream.print(op1);
    outputStream.println("");
  }

  private void printInsn(String opcode, String op1, String op2) {
    outputStream.print("\t");
    outputStream.print(opcode);
    outputStream.print("\t");
    outputStream.print(op1);
    outputStream.print(",");
    outputStream.print(op2);
    outputStream.println("");
  }

  public void printComment(String comment) {
    outputStream.println("# " + comment);
  }

  public void genJmpIfFalse(String label) {
    printInsn("popq", "%rbx");  // boolean expression
    printInsn("cmpq", "$0", "%rbx");  // eval if false
    printInsn("je", label);  // if false, jump to label
    itemsOnStack--;
  }

  public void genJmpIfTrue(String label) {
    printInsn("popq", "%rbx");  // boolean expression
    printInsn("cmpq", "$0", "%rbx");  // eval if false
    printInsn("jne", label);  // if not false, jump to label
    itemsOnStack--;
  }

  public void genJmp(String label) {
    printInsn("jmp", label);  // jump to label
  }

  public void genAssign(ClassVarType currentClass, String identifier) {
    printComment("assign to " + identifier);

    printInsn("popq", "%rax"); // read expression result

    if (localOffsets.containsKey(identifier)) {
      // identifier refers to a parameter or local variable
      int offset = localOffsets.get(identifier);
      printInsn("movq", "%rax", "-"+offset+"(%rbp)");
    } else {
      // identifier refers to a field
      int offset = currentClass.getFieldOffset(identifier);
      // get the `this` pointer
      printInsn("movq", "-8(%rbp)", "%rdx");
      printInsn("movq", "%rax", offset+"(%rdx)");
    }
    itemsOnStack--;
  }

  public void genLookup(ClassVarType currentClass, String identifier) {
    printComment("lookup " + identifier);

    if (localOffsets.containsKey(identifier)) {
      // identifier refers to a parameter or local variable
      int offset = localOffsets.get(identifier);
      printInsn("movq", "-"+offset+"(%rbp)", "%rax");
    } else {
      // identifier refers to a field
      int offset = currentClass.getFieldOffset(identifier);
      // get the `this` pointer
      printInsn("movq", "-8(%rbp)", "%rdx");
      printInsn("movq", offset+"(%rdx)", "%rax");
    }
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  public void genArrayAssign(ClassVarType currentClass, String identifier) {
    printComment("array assign");

    printInsn("popq", "%rcx"); // value to assign
    printInsn("popq", "%rbx"); // array offset
    genLookup(currentClass, identifier);
    printInsn("popq", "%rax"); // array pointer
    genBoundsCheck("%rax", "%rbx");
    printInsn("movq", "%rcx", "(%rax,%rbx,8)");  // store value
    itemsOnStack -= 3;
  }

  public void genArrayLookup() {
    printComment("array lookup");

    printInsn("popq", "%rbx");  // array offset
    printInsn("popq", "%rax");  // array pointer
    genBoundsCheck("%rax", "%rbx");
    printInsn("movq", "(%rax,%rbx,8)", "%rbx");  // lookup value
    printInsn("pushq", "%rbx");
    itemsOnStack--;
  }

  public void genArrayLength() {
    printComment("array length");

    printInsn("popq", "%rax");  // array pointer
    printInsn("movq", "-8(%rax)", "%rax");  // lookup length
    printInsn("pushq", "%rax");
  }

  /**
   * Reads the length of the array referred to by the pointer in the given
   * register. Emits code that causes a runtime error if the offset in the given
   * register is less than zero or greater than or equal to the length.
   * Overwrites the value in %rdx.
   *
   * @param array A register containing a pointer to the array to check.
   * @param offset A register containing the offset into the array.
   */
  private void genBoundsCheck(String array, String offset) {
    String labelError = newLabel("out_of_bounds");
    String labelPass = newLabel("bounds_ok");

    printInsn("movq", "-8("+array+")", "%rdx");  // lookup length
    printInsn("cmpq", offset, "%rdx");  // compare offset to length
    printInsn("jle", labelError);  // error if length <= offset

    printInsn("movq", "$0", "%rdx");
    printInsn("cmpq", offset, "%rdx");  // compare offset to 0
    printInsn("jle", labelPass); // ok if 0 <= offset

    printLocalLabel(labelError);
    genRuntimeError(ERR_OUT_OF_BOUNDS);
    printLocalLabel(labelPass);
}

  /**
   * ShortCircuitAnd and ShortCircuitOr need a way to indicate that only one of
   * the constants they generate is actually pushed onto the stack.
   */
  public void decrementStackItemCount() {
    itemsOnStack--;
  }

  private void genCmpDoubleTemplate(String function) {
    genDoubleActual(1);
    genDoubleActual(0);
    genCall(function);
    printInsn("pushq", "%rax");  // the return value
    itemsOnStack++;
  }

  public void genEqual() {
    printComment("equal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("sete", "%al");  // set %al to 1 if equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genEqualDouble() {
    printComment("equal operation");
    genCmpDoubleTemplate("cmp_eq_double");
  }

  public void genNotEqual() {
    printComment("notequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setne", "%al");  // set %al to 1 if not equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genNotEqualDouble() {
    printComment("notequal operation");
    genCmpDoubleTemplate("cmp_ne_double");
  }

  public void genLessThan() {
    printComment("lessthan operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setl", "%al");  // set %al to 1 if less
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genLessThanDouble() {
    printComment("lessthan operation");
    genCmpDoubleTemplate("cmp_lt_double");
  }

  public void genGreaterThan() {
    printComment("greaterthan operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setg", "%al");  // set %al to 1 if greater
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genGreaterThanDouble() {
    printComment("greaterthan operation");
    genCmpDoubleTemplate("cmp_gt_double");
  }

  public void genLessEqual() {
    printComment("lessorequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setle", "%al");  // set %al to 1 if less or equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genLessEqualDouble() {
    printComment("lessorequal operation");
    genCmpDoubleTemplate("cmp_le_double");
  }

  public void genGreaterEqual() {
    printComment("greaterorequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setge", "%al");  // set %al to 1 if greater or equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genGreaterEqualDouble() {
    printComment("greaterorequal operation");
    genCmpDoubleTemplate("cmp_ge_double");
  }

  public void genNot() {
    printComment("negation operation");
    printInsn("popq", "%rax");
    printInsn("testq", "%rax", "%rax"); // sets ZF if %rax = 0
    printInsn("sete", "%al"); // set %al to ZF
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genAdd() {
    printComment("add operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("addq", "%rbx", "%rax");  // %rax += %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genAddDouble() {
    printComment("add operation");
    genPopDouble("%xmm1");  // right operand
    genPopDouble("%xmm0");  // left operand
    printInsn("addsd", "%xmm1", "%xmm0");  // %xmm0 *= %xmm1  (2nd operand is dst)
    genPushDouble("%xmm0");
    itemsOnStack--;
  }

  public void genSub() {
    printComment("sub operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("subq", "%rbx", "%rax");  // %rax -= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genSubDouble() {
    printComment("sub operation");
    genPopDouble("%xmm1");  // right operand
    genPopDouble("%xmm0");  // left operand
    printInsn("subsd", "%xmm1", "%xmm0");  // %xmm0 -= %xmm1  (2nd operand is dst)
    genPushDouble("%xmm0");
    itemsOnStack--;
  }

  public void genMul() {
    printComment("mul operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("imulq", "%rbx", "%rax");  // %rax *= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genMulDouble() {
    printComment("mul operation");
    genPopDouble("%xmm1");  // right operand
    genPopDouble("%xmm0");  // left operand
    printInsn("mulsd", "%xmm1", "%xmm0");  // %xmm0 *= %xmm1  (2nd operand is dst)
    genPushDouble("%xmm0");
    itemsOnStack--;
  }

  public void genDiv() {
    printComment("div operation");
    printInsn("popq", "%rbx");  // divisor
    printInsn("popq", "%rax");  // dividend
    printInsn("movq", "%rax", "%rdx");  // copy dividend to extension register
    printInsn("sarq", "$63", "%rdx");  // extend sign bit to fill register
    printInsn("idivq", "%rbx");  // %rdx:%rax /= %rbx (%rax is dest)
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genDivDouble() {
    printComment("div operation");
    genPopDouble("%xmm1");  // right operand
    genPopDouble("%xmm0");  // left operand
    printInsn("divsd", "%xmm1", "%xmm0");  // %xmm0 /= %xmm1  (2nd operand is dst)
    genPushDouble("%xmm0");
    itemsOnStack--;
  }

  public void genMod() {
    printComment("mod operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("movq", "%rax", "%rdx");  // copy left to extension register
    printInsn("sarq", "$63", "%rdx");  // extend sign bit to fill register
    printInsn("idivq", "%rbx");  // %rdx:%rax /= %rbx (%rdx contains remainder)
    printInsn("pushq", "%rdx");
    itemsOnStack--;
  }

  public void genDisplay() {
    printInsn("popq", "%rdi");  // single operand
    itemsOnStack--;
    genCall("put");
  }

  public void genPrintInteger() {
    printComment("print int statement");
    genIntegerActual(0);
    genCall("put");
  }

  public void genPrintDouble() {
    printComment("print double statement");
    genDoubleActual(0);
    genCall("put_double");
  }

  public void genAllocateArray() {
    printComment("allocate array");

    printInsn("popq", "%rbx");  // get the size of the array

    String labelPass = newLabel("size_ok");

    printInsn("movq", "$0", "%rdx");
    printInsn("cmpq", "%rbx", "%rdx");  // compare size to 0
    printInsn("jle", labelPass); // ok if 0 <= size

    genRuntimeError(ERR_NEG_ARRAY_SIZE);
    printLocalLabel(labelPass);

    printInsn("movq", "%rbx", "%rdi");  // copy to first argument position
    printInsn("addq", "$1", "%rdi");  // increment by 1 (space for array length)
    printInsn("shlq", "$3", "%rdi");  // multiply by 8 to get byte count

    genCall("mjmalloc");

    printInsn("movq", "%rbx", "(%rax)");  // put the length
    printInsn("addq", "$8", "%rax"); // add 8 (so a[-1] is the length)

    printInsn("pushq", "%rax");  // push the array pointer
  }

  public void genAllocateObject(ClassVarType type) {
    printComment("allocate object");

    printInsn("movq", "$"+type.size(), "%rdi");  // prepare the size of the object
    genCall("mjmalloc");
    printInsn("leaq", assemblerPrefixName + vtblName(type.name) + "(%rip)", "%rbx"); // get the vtbl address
    printInsn("movq", "%rbx", "(%rax)"); // initialize the object's vtbl pointer
    printInsn("pushq", "%rax");  // push the address of the new heap space
    itemsOnStack++;
  }

  public void genVtables(Collection<ClassVarType> classes) {
    for (ClassVarType c : classes) {
      printComment("vtbl for " + c);
      printSection(".data");
      printGlobalName(vtblName(c.name));
      printLabel(vtblName(c.name));

      Map<String, MethodMetadata> vtable = getVtableEntriesForClass(c);
      for (MethodMetadata method : vtable.values()) {
        printInsn(".quad", assemblerPrefixName + mangle(method.enclosingClass.name, method));
      }
    }
  }

  private Map<String, MethodMetadata> getVtableEntriesForClass(ClassVarType c) {
    Map<String, MethodMetadata> vtable;
    if (c.superclass != null) {
      vtable = getVtableEntriesForClass(c.superclass);
    } else {
      vtable = new LinkedHashMap<String, MethodMetadata>();
    }

    for (MethodMetadata method : c.methods.values())
      vtable.put(method.name, method);

    return vtable;
  }

  public void genStatementCountsDeclaration() {
    if (!statementCounting)
      return;

    printInsn(".zerofill __DATA,__bss," + assemblerPrefixName + "statement_counts,"
        + (8 * maxLineNumber) + ",8");
  }

  public void genStatementCountIncrement(int lineNumber) {
    printComment("line " + lineNumber);

    if (!statementCounting)
      return;

    maxLineNumber = (lineNumber > maxLineNumber) ? lineNumber : maxLineNumber;
    printInsn("leaq", assemblerPrefixName + "statement_counts(%rip)", "%rax");
    printInsn("movq", "$"+(lineNumber-1), "%rbx");
    printInsn("incq", "(%rax,%rbx,8)");
  }

  public void genPrintStatementCounts() {
    if (!statementCounting)
      return;

    genCall("generated_print_statement_counts");
  }

  public void genPrintStatementCountsDeclaration() {
    if (!statementCounting)
      return;

    genFunctionEntry("generated_print_statement_counts");
    printInsn("leaq", assemblerPrefixName + "statement_counts(%rip)", "%rdi");
    printInsn("movq", "$"+maxLineNumber, "%rsi");
    genCall("print_statement_counts");
    genFunctionExit("psc");

  }
}
