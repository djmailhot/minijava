package AST;
import AST.Visitor.Visitor;

public class Not extends Exp {
  public Exp e;

  public Not(Exp ae, int lineNumber) {
    super(lineNumber);
    this.e = ae;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
