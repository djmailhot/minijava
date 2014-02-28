/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.io.FileInputStream;

import AST.*;
import CodeGenerator.*;
import Parser.parser;
import Scanner.scanner;
import SemanticAnalyzer.SemanticTypes.ProgramMetadata;
import SemanticAnalyzer.SemanticTypes.ClassVarType;

import java_cup.runtime.Symbol;

/**
 * Entry point for code generation.
 */
public class CodeGenMain {
  public static void main(String[] args) {
    String outputFileName = null;
    String inputFileName = null;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-i")) {
        inputFileName = args[i+1];
        i += 1;
      } else if (args[i].equals("-o")) {
        outputFileName = args[i+1];
        i += 1;
      } else {
        System.err.println("Unknown argument <" + args[i] + ">");
      }
    }

    try {
      System.out.println("input: "+inputFileName);
      System.out.println("output: "+outputFileName);
      // create a scanner on the input file and parse it
      scanner s;
      if (inputFileName == null)
        s = new scanner(System.in);
      else
        s = new scanner(new FileInputStream(inputFileName));

      parser p = new parser(s);
      Symbol root = p.parse();
      Program program = (Program) root.value;

      // Build symbol tables and annotate AST.Exp nodes with types
      ProgramMetadata pm = TypeChecker.gatherSymbols(program);
      TypeChecker.typeCheck(program, pm);

      setClassFieldOffsets(pm);

      CodeGenerator cg = new CodeGenerator(outputFileName);
      program.accept(new CodeGeneratorVisitor(cg, pm));

      System.exit(0);

    } catch (Exception e) {
      System.err.println("Unexpected internal compiler error: " + e.toString());
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void setClassFieldOffsets(ProgramMetadata pm) {
    for (ClassVarType cvt : pm.classes.values()) {

      // indexes are 0-indexed
      int index = cvt.size() - cvt.fields.size();
      for (String name : cvt.fields.keySet()) {
        cvt.fieldOffsets.put(name, index);
        index += 1;
      }
    }
  }
}
