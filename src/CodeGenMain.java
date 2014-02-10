import AST.*;
import CodeGenerator.*;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

import java.util.*;

/**
 * Entry point for code generation. Feel free to use this as your compiler's
 * entry point, and feel free to modify it as you see fit.
 */
public class CodeGenMain {
  public static void main(String[] args) {
    try {
      //
      // create a scanner on the input file
      //
      scanner s = new scanner(System.in);
      parser p = new parser(s);
      // print to stdout
      CodeGenerator cg = new CodeGenerator(null);
      Symbol root;

      cg.genFunctionEntry("asm_main");
      //
      // replace p.parse() with p.debug_parse() in next line to see trace of
      // parser shift/reduce actions during parse
      //
      root = p.parse();
      Program program = (Program)root.value;
      program.accept(new CodeGeneratorVisitor(cg));
      //
      // System.out.print("\n" + "Parsing completed");
      //
      cg.genFunctionExit("asm_main");
    } catch (Exception e) {
      //
      // yuck: some kind of error in the compiler implementation
      // that we're not expecting (a bug!)
      //
      System.err.println("Unexpected internal compiler error: " + e.toString());

      //
      // print out a stack dump
      //
      e.printStackTrace();
    }
  }
}
