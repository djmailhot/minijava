package AST;
import AST.Visitor.Visitor;

public class ClassDeclSimple extends ClassDecl {
  public Identifier i;
  public VarDeclList vl;
  public MethodDeclList ml;

  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml, int lineNumber) {
    super(lineNumber);
    this.i = ai;
    this.vl = avl;
    this.ml = aml;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
