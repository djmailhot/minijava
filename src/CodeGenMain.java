/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.io.FileInputStream;
import java.util.Map;

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

      setClassMemberOffsets(pm);

      CodeGenerator cg = new CodeGenerator(outputFileName);
      program.accept(new CodeGeneratorVisitor(cg, pm));

      System.exit(0);

    } catch (Exception e) {
      System.err.println("Unexpected internal compiler error: " + e.toString());
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void setClassMemberOffsets(ProgramMetadata pm) {
    for (ClassVarType cvt : pm.classes.values()) {

      // indexes are 0-indexed
      int index = cvt.size() - cvt.fields.size();
      for (String name : cvt.fields.keySet()) {
        cvt.fieldOffsets.put(name, index);
        index += 8;
      }

      // Set this class type's method offsets to refer to the lowest override in
      // the class hierarchy for each method on this class type.
      setMethodOffsets(cvt, cvt.methodOffsets);
    }
  }

  private static int setMethodOffsets(ClassVarType c, Map<String, Integer> offsets) {
    int offset = 0;

    // Add superclass methods to the offsets table
    if (c.superclass != null)
      offset = setMethodOffsets(c.superclass, offsets);

    // Add c's method offsets, and overwrite offsets of overridden methods
    for (String name : c.methods.keySet()) {
      offsets.put(name, offset);
      offset += 8;
    }

    return offset;
  }
}
