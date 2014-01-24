package AST;
import AST.Visitor.Visitor;

public class IntegerLiteral extends Exp {
  public int i;

  public IntegerLiteral(String ai, int lineNumber) {
    super(lineNumber);
    this.i = Integer.parseInt(ai);
  }

  public IntegerLiteral(int ai, int lineNumber) {
    super(lineNumber);
    this.i = ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
