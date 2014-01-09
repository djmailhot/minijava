package AST;
import AST.Visitor.Visitor;

public class Program extends ASTNode {
  public MainClass m;
  public ClassDeclList cl;

  public Program(MainClass am, ClassDeclList acl, int lineNumber) {
    super(lineNumber);
    this.m = am;
    this.cl = acl;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
