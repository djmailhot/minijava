package AST;
import AST.Visitor.Visitor;

public class IntegerType extends Type {
  public IntegerType(int lineNumber) {
    super(lineNumber);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
}
