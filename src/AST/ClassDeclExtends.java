package AST;
import AST.Visitor.Visitor;

public class ClassDeclExtends extends ClassDecl {
  public Identifier i;
  public Identifier j;
  public VarDeclList vl;
  public MethodDeclList ml;

  public ClassDeclExtends(Identifier ai, Identifier aj,
                  VarDeclList avl, MethodDeclList aml, int lineNumber) {
    super(lineNumber);
    this.i = ai;
    this.j = aj;
    this.vl = avl;
    this.ml = aml;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
