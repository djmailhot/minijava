package AST;
import AST.Visitor.Visitor;

public class True extends Exp {
  public True(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
