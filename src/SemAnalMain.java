import AST.*;
import SemanticAnalyzer.*;
import SemanticAnalyzer.SemanticTypes.ProgramMetadata;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

import java.util.*;

/**
 * Entry point for semantic analysis. Feel free to use this as your compiler's
 * entry point, and feel free to modify it as you see fit.
 */
public class SemAnalMain {
  public static void main(String[] args) {
    try {
      // create a scanner on the input file
      scanner s = new scanner(System.in);
      // parse it
      parser p = new parser(s);
      Symbol root = p.parse();
      Program program = (Program)root.value;


      // initialize the semantic analysis type graph
      ProgramMetadata pm = new ProgramMetadata();

      // First pass:  initialize types for all declared classes
      program.accept(new ClassDeclarationVisitor(pm));


      // Second pass:  for all declared classes fill in class and method details
      // program.accept(new ClassInternalsVisitor(pt));

      // First verification:  verify method override relationships

      //
      // System.out.print("\n" + "Parsing completed");
      //
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
