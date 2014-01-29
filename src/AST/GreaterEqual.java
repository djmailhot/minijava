package AST;
import AST.Visitor.Visitor;

public class GreaterEqual extends Exp {
  public Exp e1;
  public Exp e2;

  public GreaterEqual(Exp ae1, Exp ae2, int lineNumber) {
    super(lineNumber);
    this.e1 = ae1;
    this.e2 = ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
