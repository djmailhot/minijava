package AST;
import AST.Visitor.Visitor;

public class Call extends Exp {
  public Exp e;
  public Identifier i;
  public ExpList el;

  public Call(Exp ae, Identifier ai, ExpList ael, int lineNumber) {
    super(lineNumber);
    this.e = ae;
    this.i = ai;
    this.el = ael;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
