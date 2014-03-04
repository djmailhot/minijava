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
    boolean statementCounting = false;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-i")) {
        inputFileName = args[i+1];
        i += 1;
      } else if (args[i].equals("-o")) {
        outputFileName = args[i+1];
        i += 1;
      } else if (args[i].equals("-c")) {
        statementCounting = true;
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

      CodeGenerator cg = new CodeGenerator(outputFileName, statementCounting);
      program.accept(new CodeGeneratorVisitor(cg, pm));

      System.exit(0);

    } catch (Exception e) {
      System.err.println("Unexpected internal compiler error: " + e.toString());
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Initializes the fieldOffsets and methodOffsets tables for each class type
   * in the compiled program.
   */
  private static void setClassMemberOffsets(ProgramMetadata pm) {
    for (ClassVarType cvt : pm.classes.values()) {

      // indexes are 0-indexed
      int index = cvt.size() - 8 * cvt.fields.size();
      for (String name : cvt.fields.keySet()) {
        cvt.fieldOffsets.put(name, index);
        index += 8;
      }

      setMethodOffsets(cvt, cvt.methodOffsets);
    }
  }

  /**
   * Populates the given offsets table with the method offsets of all of the
   * given class's methods, including inherited methods.
   *
   * @param c The class whose methods will be added to the given offsets table.
   * @param offsets The method offsets table to populate.
   * @return The size of c's vtable in bytes.
   */
  private static int setMethodOffsets(ClassVarType c, Map<String, Integer> offsets) {
    int offset = 0;

    // Add superclass methods to the offsets table
    if (c.superclass != null)
      offset = setMethodOffsets(c.superclass, offsets);

    // Add c's method offsets
    for (String name : c.methods.keySet()) {
      if (!offsets.containsKey(name)) {
        offsets.put(name, offset);
        offset += 8;
      }
    }

    return offset;
  }
}
