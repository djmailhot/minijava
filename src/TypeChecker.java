/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.util.Iterator;

import AST.*;
import SemanticAnalyzer.*;
import SemanticAnalyzer.SemanticTypes.*;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

/**
 * Entry point for semantic analysis.
 */
public class TypeChecker {
  public static void main(String[] args) {
    try {
      // create a scanner on the input file
      scanner s = new scanner(System.in);
      // parse it
      parser p = new parser(s);
      Symbol root = p.parse();
      Program program = (Program)root.value;

      ProgramMetadata pm = gatherSymbols(program);
      typeCheck(program, pm);

      System.exit(0);

    } catch (Exception e) {
      System.exit(1);
    }
  }

  /**
   * Generates and returns the symbol tables for the given AST. If the AST is
   * invalid, prints an error message and exits with status code 1.
   *
   * @param program The AST to collect symbols from.
   * @return The root of the symbol table tree.
   */
  public static ProgramMetadata gatherSymbols(Program program) {
    // initialize the root symbol table
    ProgramMetadata pm = new ProgramMetadata();

    // First pass: initialize types for all declared classes
    program.accept(new ClassDeclarationVisitor(pm));

    // Second pass: Link subclasses to superclasses
    program.accept(new ClassHierarchyVisitor(pm));

    // Third pass: for all declared classes fill in class and method details
    program.accept(new ClassInternalsVisitor(pm));

    return pm;
  }

  /**
   * Typechecks the given program, annotating AST nodes representing expressions
   * with their evaluated types. If the AST is invalid, prints an error message
   * and exits with status code 1.
   *
   * @param program The program to typecheck.
   * @param pm The root symbol table for the program.
   */
  public static void typeCheck(Program program, ProgramMetadata pm) {
    // Verify method override relationships
    verifyOverrides(pm);

    // Type check statements and expressions
    program.accept(new TypeCheckerVisitor(pm));
  }

  private static void verifyOverrides(ProgramMetadata pm) {
    for (ClassVarType child : pm.classes.values()) {
      ClassVarType parent = child.superclass;

      while (parent != null) {
        for (String methodName : child.methods.keySet()) {
          if (parent.methods.containsKey(methodName)) {
            MethodMetadata childMethod = child.methods.get(methodName);
            MethodMetadata parentMethod = parent.methods.get(methodName);

            if (childMethod.params.size() != parentMethod.params.size()) {
              ErrorMessages.errInvalidOverride(child.lineNumber, methodName,
                  child.name, parent.name);
            }

            Iterator<VarType> pArgs = parentMethod.params.values().iterator();
            for (VarType cArg : childMethod.params.values()) {
              if (!cArg.equals(pArgs.next())) {
                ErrorMessages.errInvalidOverride(child.lineNumber, methodName,
                    child.name, parent.name);
              }
            }

            if (!childMethod.returnType.subtypeOrEqual(parentMethod.returnType)) {
              ErrorMessages.errInvalidOverride(child.lineNumber, methodName,
                  child.name, parent.name);
            }
          }
        }

        parent = parent.superclass;
      }
    }
  }

}
