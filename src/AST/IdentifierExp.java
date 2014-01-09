package AST;
import AST.Visitor.Visitor;

public class IdentifierExp extends Exp {
  public String s;

  public IdentifierExp(String as, int lineNumber) {
    super(lineNumber);
    this.s = as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
