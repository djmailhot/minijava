package AST;
import AST.Visitor.Visitor;

public class Identifier extends ASTNode {
  public String s;

  public Identifier(String as, int lineNumber) {
    super(lineNumber);
    this.s = as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public String toString() {
    return s;
  }
}
