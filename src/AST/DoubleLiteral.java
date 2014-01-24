package AST;
import AST.Visitor.Visitor;

public class DoubleLiteral extends Exp {
  public double d;

  public DoubleLiteral(String ad, int lineNumber) {
    super(lineNumber);
    d = Double.parseDouble(ad);
  }

  public DoubleLiteral(double ad, int lineNumber) {
    super(lineNumber);
    this.d = ad;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
