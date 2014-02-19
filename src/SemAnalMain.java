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
      for (String className : pm.classes.keySet()) {
        ClassVarType childClass = pm.classes.get(className);
        if (childClass.superclass == null) {
          System.out.println(className + " is a base class");
        } else {
          String parentName = null;
          // This loop is a hack, but it's just for debug purposes; we don't need a bimap.
          for (String prospectiveParent : pm.classes.keySet()) {
            ClassVarType parentClass = pm.classes.get(prospectiveParent);
            if (parentClass == childClass.superclass) {
              parentName = prospectiveParent;
              break;
            }
          }
          System.out.println(className + " extends " + parentName);
        }
      }
      System.out.println();

      // Third pass:  for all declared classes fill in class and method details
      program.accept(new ClassInternalsVisitor(pm));

      // Debug: print all class declarations
      System.out.println("Pass 3: Class internals");
      for (String className : pm.classes.keySet()) {
        System.out.println("Class: " + className);
        ClassVarType ct = pm.classes.get(className);
        for (String fieldName : ct.fields.keySet()) {
          System.out.println("  Field: " + ct.fields.get(fieldName) + " " + fieldName);
        }

        for (String methodName : ct.methods.keySet()) {
          System.out.println("  Method: " + methodName);
          MethodMetadata mm = ct.methods.get(methodName);
          for (String argName : mm.args.keySet()) {
            System.out.println("    Formal: " + mm.args.get(argName) + " " + argName);
          }

          for (String localVar : mm.localVars.keySet()) {
            System.out.println("    Local: " + mm.localVars.get(localVar) + " " + localVar);
          }
        }
      }
      System.out.println();

      // First verification:  verify method override relationships
      verifyOverrides(pm);

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

  private static void verifyOverrides(ProgramMetadata pm) {
    for (String className : pm.classes.keySet()) {
      ClassVarType childClass = pm.classes.get(className);
      ClassVarType superclass = childClass.superclass;

      while (superclass != null) {
        for (String methodName : childClass.methods.keySet()) {
          if (superclass.methods.containsKey(methodName)) {
            MethodMetadata childMethod = childClass.methods.get(methodName);
            MethodMetadata parentMethod = superclass.methods.get(methodName);

            if (!childMethod.args.equals(parentMethod.args)
                || !childMethod.returnType.equals(parentMethod.returnType)) {
              // TODO: Print line number, possibly superclass name.
              // Maybe we should include that information in ClassVarType.
              System.err.println("Override of "+methodName+" in "+className
                  + " doesn't match definition in superclass.");
              System.exit(0);
            }
          }
        }

        superclass = superclass.superclass;
      }
    }
  }

}
