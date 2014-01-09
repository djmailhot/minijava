package AST;
import AST.Visitor.Visitor;

public abstract class Exp extends ASTNode {
  public Exp(int lineNumber) {
    super(lineNumber);
  }
  public abstract void accept(Visitor v);
}
