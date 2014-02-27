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

  private PrintStream outputStream;
  private int labelCounter;
  private Map<String, Integer> localOffsets;

  public String assemblerPrefixName;

  public CodeGenerator(String outputFileName) {
    if (outputFileName != null && outputFileName != "stdout") {
      try {
        outputStream = new PrintStream(outputFileName);
      } catch (FileNotFoundException e) {
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

  public void genFunctionEntry(String functionName) {
    printComment("entry point for " + assemblerPrefixName + functionName);
    printSection(".text");
    printGlobalName(functionName);
    printLabel(functionName);

    printInsn("pushq", "%rbp");
    printInsn("movq", "%rsp", "%rbp");
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

    // Allocate space for `this` pointer, parameters, and local variables
    int numLocals = 1 + method.params.size() + method.localVars.size();
    printInsn("subq", String.format("$%d", 8 * numLocals), "%rsp");

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
  }

  public void genMethodExit(String className, MethodMetadata method) {
    printComment("return point for " + className + "." + method + "()");

    // Pop return value
    printInsn("popq", "%rax");

    // Free the space used by arguments and local variables, +1 for `this`.
    printInsn("addq", String.format("$%d", 8 * (localOffsets.size() + 1)), "%rsp");
    localOffsets = null;

    // Restore callee-saved registers
    printInsn("popq", "%rbx");

    printInsn("popq", "%rbp");
    printInsn("ret");
  }

  public void genMethodCall(String className, MethodMetadata method) {
    printInsn("call", assemblerPrefixName + mangle(className, method));
    printInsn("pushq", "%rax");
  }

  public void genActual(int position) {
    if (position > 5) {
      System.err.println("Encountered a function with more than 5 explicit arguments.");
      System.exit(0);
    }
    String register = PARAM_REGISTERS[position];
    printInsn("popq", register);
  }

  public void genConstant(int value) {
    printInsn("movq", String.format("$%d", value), "%rax");
    printInsn("pushq", "%rax");
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
  }

  public void genJmpIfTrue(String label) {
    printInsn("popq", "%rbx");  // boolean expression
    printInsn("cmpq", "$0", "%rbx");  // eval if false
    printInsn("jne", label);  // if not false, jump to label
  }

  public void genJmp(String label) {
    printInsn("jmp", label);  // jump to label
  }

  public void genAssign(String identifier) {
    printComment("assign to " + identifier);
    printInsn("popq", "%rax"); // read expression result
    int offset = localOffsets.get(identifier);
    printInsn("movq", "%rax", String.format("-%d(%%rbp)", offset));
  }

  public void genLookup(String identifier) {
    printComment("lookup " + identifier);
    int offset = localOffsets.get(identifier);
    printInsn("movq", String.format("-%d(%%rbp)", offset), "%rax");
    printInsn("pushq", "%rax");
  }

  public void genEqual() {
    printComment("equal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("sete", "%al");  // set %al to 1 if equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genNotEqual() {
    printComment("notequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setne", "%al");  // set %al to 1 if not equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genLessThan() {
    printComment("lessthan operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setl", "%al");  // set %al to 1 if less
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genGreaterThan() {
    printComment("greaterthan operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setg", "%al");  // set %al to 1 if greater
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genLessEqual() {
    printComment("lessorequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setle", "%al");  // set %al to 1 if less or equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
  }

  public void genGreaterEqual() {
    printComment("greaterorequal operation");
    printInsn("popq", "%rax");  // right operand
    printInsn("popq", "%rbx");  // left operand
    printInsn("cmpq", "%rax", "%rbx");  // compare %rax to %rbx
    printInsn("setge", "%al");  // set %al to 1 if greater or equal
    printInsn("movzbq", "%al", "%rax");  // pad with zeros
    printInsn("pushq", "%rax");
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
  }

  public void genSub() {
    printComment("sub operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("subq", "%rbx", "%rax");  // %rax -= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }

  public void genMul() {
    printComment("mul operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("imulq", "%rbx", "%rax");  // %rax *= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }

  public void genDiv() {
    printComment("div operation");
    printInsn("popq", "%rbx");  // divisor
    printInsn("popq", "%rax");  // dividend
    printInsn("movq", "%rax", "%rdx");  // copy dividend to extension register
    printInsn("sarq", "$63", "%rdx");  // extend sign bit to fill register
    printInsn("idivq", "%rbx");  // %rdx:%rax /= %rbx (%rax is dest)
    printInsn("pushq", "%rax");
  }

  public void genMod() {
    printComment("mod operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("movq", "%rax", "%rdx");  // copy left to extension register
    printInsn("sarq", "$63", "%rdx");  // extend sign bit to fill register
    printInsn("idivq", "%rbx");  // %rdx:%rax /= %rbx (%rdx contains remainder)
    printInsn("pushq", "%rdx");
  }

  public void genDisplay() {
    printInsn("popq", "%rdi");  // single operand
    printInsn("call", assemblerPrefixName + "put");
  }

  public void genPrint() {
    printComment("print statement");
    printInsn("popq", "%rdi");  // single operand
    printInsn("call", assemblerPrefixName + "put");
  }

}
