package AST;
import AST.Visitor.Visitor;

public class ArrayAssign extends Statement {
  public Identifier i;
  public Exp e1;
  public Exp e2;

  public ArrayAssign(Identifier ai, Exp ae1, Exp ae2, int lineNumber) {
    super(lineNumber);
    this.i = ai;
    this.e1 = ae1;
    this.e2 = ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

