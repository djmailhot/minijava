package AST;
import AST.Visitor.Visitor;

public class NewArray extends Exp {
  public Exp e;

  public NewArray(Exp ae, int lineNumber) {
    super(lineNumber);
    this.e = ae;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
