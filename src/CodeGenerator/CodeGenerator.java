package CodeGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class CodeGenerator {

  private PrintStream outputStream;
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

  public void genConstant(int value) {
    printInsn("movq", String.format("$%d", value), "%rax");
    printInsn("pushq", "%rax");
  }

  private void printLabel(String labelName) {
    outputStream.println(assemblerPrefixName + labelName + ":");
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

  public void genIf() {
    printComment("if statement");
    printInsn("popq", "%r8");  // false case statement
    printInsn("popq", "%r9");  // true case statement
    printInsn("popq", "%rbx");  // boolean expression
    printInsn("cmpq", "$0", "%rbx");  // eval if false
    printInsn("je", ".Lelse");  // if false, jump to else case
    printInsn("pushq", "%r8");  // push the true statement
    printInsn("jmp", ".Lifend");  // if true, jump to end
    printLabel(".Lelse");
    printInsn("pushq", "%r9");  // push the false statement
    printLabel(".Lifend");
  }

  public void genWhile() {
    printComment("while statement");
    printInsn("popq", "%r8");  // loop statement
    printInsn("popq", "%rbx");  // boolean expression
    printLabel(".Ltop");
    printInsn("cmpq", "$0", "%rbx");  // eval if false
    printInsn("je", ".Lbot");  // if false, jump to else case
    printInsn("pushq", "%r8");  // execute
    printInsn("jmp", ".Ltop");  // loop
    printLabel(".Lbot");
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
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("sarq", "$63", "%rax");  // populate the sign
    printInsn("idivq", "%rbx", "%rax");  // %rax /= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rax");
  }

  // TODO:  does this really do mod?
  public void genMod() {
    printComment("mod operation");
    printInsn("popq", "%rbx");  // right operand
    printInsn("popq", "%rax");  // left operand
    printInsn("sarq", "$63", "%rax");  // populate the sign
    printInsn("idivq", "%rbx", "%rax");  // %rax /= %rbx  (2nd operand is dst)
    printInsn("pushq", "%rdx");  // pull from the extended accumulator
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
