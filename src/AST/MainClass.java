package AST;
import AST.Visitor.Visitor;

public class MainClass extends ASTNode {
  public Identifier i1;
  public Identifier i2;
  public Statement s;

  public MainClass(Identifier ai1, Identifier ai2, Statement as, int lineNumber) {
    super(lineNumber);
    this.i1 = ai1;
    this.i2 = ai2;
    this.s = as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

