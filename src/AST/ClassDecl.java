package AST;
import AST.Visitor.Visitor;

public abstract class ClassDecl extends ASTNode {
  public ClassDecl(int lineNumber) {
    super(lineNumber);
  }

  public abstract void accept(Visitor v);
}
