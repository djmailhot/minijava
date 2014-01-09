package AST;
import AST.Visitor.Visitor;

public abstract class Statement extends ASTNode {
  public Statement(int lineNumber) {
    super(lineNumber);
  }
  public abstract void accept(Visitor v);
}
