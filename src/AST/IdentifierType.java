package AST;
import AST.Visitor.Visitor;

public class IdentifierType extends Type {
  public String s;

  public IdentifierType(String as, int lineNumber) {
    super(lineNumber);
    this.s = as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
