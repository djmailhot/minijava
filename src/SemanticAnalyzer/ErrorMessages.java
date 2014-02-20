package SemanticAnalyzer;

import SemanticAnalyzer.SemanticTypes.VarType;

public class ErrorMessages {

  private static void printErrorMessage(int lineNum, String message) {
    System.out.println("Line " + lineNum + ": " + message);
    System.exit(0);
  }

  public static void errDuplicateClass(int lineNum, String name) {
    printErrorMessage(lineNum, name+" is a duplicate class name");
  }

  public static void errDuplicateVariable(int lineNum, String name) {
    printErrorMessage(lineNum, name+" is a duplicate variable name");
  }

  public static void errSymbolNotFound(int lineNum, String symbol) {
    printErrorMessage(lineNum, "Symbol "+symbol+" is undefined");
  }

  public static void errClassHierarchyCycle(int lineNum, String symbol) {
    printErrorMessage(lineNum, "Cycle in class hierarchy: "+symbol+" is a superclass of itself");
  }

  public static void errInvalidOverride(int lineNum, String methodName, String childName, String parentName) {
    printErrorMessage(lineNum, "Override of "+methodName+" in class "+childName
        +" doesn't match definition in superclass "+parentName+".");
  }

  public static void errIncompatibleTypes(int lineNum, VarType expectedType, VarType actualType) {
    printErrorMessage(lineNum, "Incompatible types. Required "+expectedType+", but found "+actualType);
  }

  public static void errBadOperandTypes(int lineNum, String operator, VarType type1, VarType type2) {
    printErrorMessage(lineNum, "Bad operand types for binary operator '"+operator
        +"'. First type: "+type1+", second type: "+type2);
  }

  public static void errInvalidPrintArgument(int lineNum, VarType actualType) {
    printErrorMessage(lineNum, "Incompatible types. Required int or double, but found "+actualType);
  }
}
