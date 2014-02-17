package SemanticAnalyzer;

public class ErrorMessages {

  private static void printErrorMessage(int lineNum, String message) {
    System.err.println("Line " + lineNum + ": " + message);
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
}
