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


      // initialize the root symbol table
      ProgramMetadata pm = new ProgramMetadata();

      // First pass:  initialize types for all declared classes
      program.accept(new ClassDeclarationVisitor(pm));

      // Second pass: Link subclasses to superclasses
      program.accept(new ClassHierarchyVisitor(pm));

      // Third pass:  for all declared classes fill in class and method details
      program.accept(new ClassInternalsVisitor(pm));

      // Debug: print all gathered symbols
      // printClassInternals(pm);

      // Verify method override relationships
      verifyOverrides(pm);

      // Type check statements and expressions
      program.accept(new TypeCheckerVisitor(pm));

      System.exit(0);

    } catch (Exception e) {
      System.exit(1);
    }
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

  private static void printClassInternals(ProgramMetadata pm) {
    for (ClassVarType ct: pm.classes.values()) {
      System.out.print("Class: " + ct);
      System.out.println(ct.superclass != null ? " (extends " + ct.superclass + ")" : "");

      for (String fieldName : ct.fields.keySet())
        System.out.println("  Field: " + ct.fields.get(fieldName) + " " + fieldName);

      for (MethodMetadata mm : ct.methods.values()) {
        System.out.println("  Method: " + mm);

        for (String argName : mm.params.keySet())
          System.out.println("    Formal: " + mm.params.get(argName) + " " + argName);

        for (String localVar : mm.localVars.keySet())
          System.out.println("    Local: " + mm.localVars.get(localVar) + " " + localVar);
      }
    }
    System.out.println();
  }

}
