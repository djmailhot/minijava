package AST;
import AST.Visitor.Visitor;

public class While extends Statement {
  public Exp e;
  public Statement s;

  public While(Exp ae, Statement as, int lineNumber) {
    super(lineNumber);
    this.e = ae;
    this.s = as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

