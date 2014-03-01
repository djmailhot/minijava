/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import SemanticAnalyzer.SemanticTypes.*;

public class CodeGenerator {

  private static final String[] PARAM_REGISTERS =
    { "%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9" };

  // The runtime error codes that our compiled program can exit with.
  private static final int ERR_OUT_OF_BOUNDS = 10;
  private static final int ERR_NEG_ARRAY_SIZE = 11;

  private PrintStream outputStream;
  private int labelCounter;
  private Map<String, Integer> localOffsets;

  /** The number of bytes allocated for arguments and locals. */
  private int localSegmentSize;

  /** The number of 8-byte values on the current function's expression stack. */
  private int itemsOnStack;

  public String assemblerPrefixName;

  public CodeGenerator(String outputFileName) {
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
   * Debug method for printing the value of a register or a literal.
   */
  private void debugPrint(String s) {
    for (String reg : PARAM_REGISTERS)
      printInsn("pushq", reg);

    printInsn("pushq", "%rax");
    printInsn("movq", s, "%rdi");
    genCall("put");
    printInsn("popq", "%rax");

    for (int i = PARAM_REGISTERS.length - 1; i >= 0; --i)
      printInsn("popq", PARAM_REGISTERS[i]);
  }

  /**
   * Exits with the given error code.
   */
  private void genRuntimeError(int errorCode) {
    switch (errorCode) {
    case ERR_OUT_OF_BOUNDS:
      printComment("index out of bounds");
      break;
    case ERR_NEG_ARRAY_SIZE:
      printComment("negative array size");
    default:
      printComment("unknown error");
      break;
    }
    printInsn("movq", "$"+errorCode, "%rdi");
    genCall(assemblerPrefixName + "exit");
  }

  public void genFunctionEntry(String functionName) {
    printComment("entry point for " + assemblerPrefixName + functionName);
    printSection(".text");
    printGlobalName(functionName);
    printLabel(functionName);

    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");

    itemsOnStack = 0;
  }

  public void genFunctionExit(String functionName) {
    printComment("return point for " + assemblerPrefixName + functionName);
    printInsn("popq", "%rbp");
    printInsn("ret");
  }

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
    printInsn("movq", PARAM_REGISTERS[0], String.format("-%d(%%rbp)", 8));

    // Store arguments and keep track of their offsets
    localOffsets = new HashMap<String, Integer>();
    for (String argName : method.params.keySet()) {
      int offset = 8 * (localOffsets.size() + 2);
      printInsn("movq", PARAM_REGISTERS[localOffsets.size() + 1],
          String.format("-%d(%%rbp)", offset));
      localOffsets.put(argName, offset);
    }

    // Track offsets for locals
    for (String localName : method.localVars.keySet()) {
      int offset = 8 * (localOffsets.size() + 2);
      localOffsets.put(localName, offset);
    }

    itemsOnStack = 0;
}

  public void genMethodExit(String className, MethodMetadata method) {
    printComment("return point for " + className + "." + method + "()");

    // Pop return value
    printInsn("popq", "%rax");
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

  private void genCall(String functionName) {
    if (itemsOnStack % 2 != 0)
      printInsn("subq", "$8", "%rsp");

    printInsn("call", assemblerPrefixName + functionName);

    if (itemsOnStack % 2 != 0)
      printInsn("addq", "$8", "%rsp");
  }

  public void genMethodCall(String className, MethodMetadata method) {
    genCall(mangle(className, method));
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  public void genActual(int position) {
    if (position > 5) {
      System.err.println("Encountered a function with more than 5 explicit arguments.");
      System.exit(0);
    }
    String register = PARAM_REGISTERS[position];
    printInsn("popq", register);
    itemsOnStack--;
  }

  public void genThis() {
    printInsn("movq", "-8(%rbp)", "%rdi");
    printInsn("pushq", "%rdi");
    itemsOnStack++;
  }

  public void genConstant(int value) {
    printInsn("movq", String.format("$%d", value), "%rax");
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  public String newLabel(String labelName) {
    String label = "L" + labelCounter + labelName;
    labelCounter += 1;
    return label;
  }

  public void printLabel(String labelName) {
    outputStream.println(assemblerPrefixName + labelName + ":");
  }

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

  public void genAssign(String identifier) {
    printComment("assign to " + identifier);

    // TODO: assignment to fields
    printInsn("popq", "%rax"); // read expression result
    int offset = localOffsets.get(identifier);
    printInsn("movq", "%rax", String.format("-%d(%%rbp)", offset));
    itemsOnStack--;
  }

  public void genLookup(String identifier) {
    printComment("lookup " + identifier);

    // TODO: lookup of fields
    int offset = localOffsets.get(identifier);
    printInsn("movq", String.format("-%d(%%rbp)", offset), "%rax");
    printInsn("pushq", "%rax");
    itemsOnStack++;
  }

  public void genArrayAssign(String identifier) {
    printComment("array assign");

    printInsn("popq", "%rcx"); // value to assign
    printInsn("popq", "%rbx"); // array offset
    genLookup(identifier);
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

  public void genNot() {
    printComment("negation operation");
    printInsn("popq", "%rax");
    printInsn("testq", "%rax", "%rax"); // sets ZF if %rax = 0
    printInsn("sete", "%al"); // set %al to ZF
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
    itemsOnStack--;
  }

  public void genAdd() {
    printComment("add operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("addq", "%rbx", "%rax");  // %rax += %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
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

  public void genMul() {
    printComment("mul operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("imulq", "%rbx", "%rax");  // %rax *= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
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

  public void genPrint() {
    printComment("print statement");
    printInsn("popq", "%rdi");  // single operand
    itemsOnStack--;
    genCall("put");
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

  public void genAllocateObject(int size) {
    printComment("allocate object");

    printInsn("movq", "$"+size, "%rdi");  // prepare the size of the object
    genCall("mjmalloc");
    printInsn("pushq", "%rax");  // push the address of the new heap space
    itemsOnStack++;
  }
}
