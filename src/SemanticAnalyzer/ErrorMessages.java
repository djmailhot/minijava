package SemanticAnalyzer;

public class ErrorMessages {

  private ErrorMessages() {}

  public static void errDuplicateClass(int lineNum, String name) {
    System.err.println("Line "+lineNum+":  "+name+" is a duplicate class name");
  }

  public static void errDuplicateVariable(int lineNum, String name) {
    System.err.println("Line "+lineNum+":  "+name+" is a duplicate varible name");
  }

  public static void errSymbolNotFound(int lineNum, String symbol) {
    System.err.println("Line "+lineNum+":  Symbol "+symbol+" is undefined");
  }
}
