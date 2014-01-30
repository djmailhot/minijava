package AST;

public class ClassBody extends ASTNode {
  public VarDeclList vl;
  public MethodDeclList ml;

  public ClassBody(VarDeclList avl, MethodDeclList aml, int lineNumber) {
    super(lineNumber);
    this.vl = avl;
    this.ml = aml;
  }
}
