import java.util.Iterator;

import AST.*;
import SemanticAnalyzer.*;
import SemanticAnalyzer.SemanticTypes.*;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

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

      // Debug: print all class declarations
      System.out.println("Pass 1: Class declarations");
      for (String className : pm.classes.keySet()) {
        System.out.println(className + " is a class");
      }
      System.out.println();

      // Second pass: Link subclasses to superclasses
      program.accept(new ClassHierarchyVisitor(pm));

      // Debug: print all class hierarchy relationships
      System.out.println("Pass 2: Class hierarchy");
      for (ClassVarType c : pm.classes.values()) {
        if (c.superclass == null)
          System.out.println(c + " is a base class");
        else
          System.out.println(c + " extends " + c.superclass);
      }
      System.out.println();

      // Third pass:  for all declared classes fill in class and method details
      program.accept(new ClassInternalsVisitor(pm));

      // Debug: print all class declarations
      System.out.println("Pass 3: Class internals");
      for (ClassVarType ct: pm.classes.values()) {
        System.out.println("Class: " + ct);
        for (String fieldName : ct.fields.keySet()) {
          System.out.println("  Field: " + ct.fields.get(fieldName) + " " + fieldName);
        }

        for (MethodMetadata mm : ct.methods.values()) {
          System.out.println("  Method: " + mm);
          for (String argName : mm.params.keySet()) {
            System.out.println("    Formal: " + mm.params.get(argName) + " " + argName);
          }

          for (String localVar : mm.localVars.keySet()) {
            System.out.println("    Local: " + mm.localVars.get(localVar) + " " + localVar);
          }
        }
      }
      System.out.println();

      // First verification:  verify method override relationships
      verifyOverrides(pm);

      // Type check statements and expressions
      program.accept(new TypeCheckerVisitor(pm));

      //
      // System.out.print("\n" + "Parsing completed");
      //
      exit(0);

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
