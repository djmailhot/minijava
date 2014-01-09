package AST;
import AST.Visitor.Visitor;

public class MethodDecl extends ASTNode {
  public Type t;
  public Identifier i;
  public FormalList fl;
  public VarDeclList vl;
  public StatementList sl;
  public Exp e;

  public MethodDecl(Type at, Identifier ai, FormalList afl, VarDeclList avl,
                    StatementList asl, Exp ae, int lineNumber) {
    super(lineNumber);
    this.t = at;
    this.i = ai;
    this.fl = afl;
    this.vl = avl;
    this.sl = asl;
    this.e = ae;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
