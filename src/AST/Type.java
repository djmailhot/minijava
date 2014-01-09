package AST;
import AST.Visitor.Visitor;

public abstract class Type extends ASTNode {
  public Type(int lineNumber) {
    super(lineNumber);
  }

  public abstract void accept(Visitor v);
}
