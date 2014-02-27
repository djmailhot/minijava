/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import AST.*;
import CodeGenerator.*;
import Parser.parser;
import Scanner.scanner;
import SemanticAnalyzer.SemanticTypes.ProgramMetadata;

import java_cup.runtime.Symbol;

/**
 * Entry point for code generation.
 */
public class CodeGenMain {
  public static void main(String[] args) {
    try {
      // create a scanner on the input file and parse it
      scanner s = new scanner(System.in);
      parser p = new parser(s);
      Symbol root = p.parse();
      Program program = (Program) root.value;

      // Build symbol tables and annotate AST.Exp nodes with types
      ProgramMetadata pm = TypeChecker.gatherSymbols(program);
      TypeChecker.typeCheck(program, pm);

      CodeGenerator cg = new CodeGenerator(null);
      program.accept(new CodeGeneratorVisitor(cg, pm));

      System.exit(0);

    } catch (Exception e) {
      System.err.println("Unexpected internal compiler error: " + e.toString());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
