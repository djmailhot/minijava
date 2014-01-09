package AST;
import AST.Visitor.Visitor;

public class Print extends Statement {
  public Exp e;

  public Print(Exp ae, int lineNumber) {
    super(lineNumber);
    this.e = ae;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
