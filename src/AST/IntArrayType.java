package AST;
import AST.Visitor.Visitor;

public class IntArrayType extends Type {
  public IntArrayType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
