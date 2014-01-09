package AST;
import AST.Visitor.Visitor;

abstract public class ASTNode {
  //
  // The line number where the node is in the source file, for use
  // in printing error messages about this AST node
  //
  protected int line_number;

  public ASTNode(int lineNumber) {
    this.line_number = lineNumber;
  }
}
