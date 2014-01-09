package AST;
import AST.Visitor.Visitor;

public class This extends Exp {
  public This(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
