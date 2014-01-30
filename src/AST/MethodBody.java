package AST;

public class MethodBody extends ASTNode {
  public VarDeclList vl;
  public StatementList sl;
  public Exp e;

  public MethodBody(VarDeclList avl, StatementList asl, Exp ae, int lineNumber) {
    super(lineNumber);
    this.vl = avl;
    this.sl = asl;
    this.e = ae;
  }

}
