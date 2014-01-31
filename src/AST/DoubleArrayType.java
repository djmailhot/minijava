package AST;
import AST.Visitor.Visitor;

public class DoubleArrayType extends Type {
  public DoubleArrayType(int lineNumber) {
    super(lineNumber);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
