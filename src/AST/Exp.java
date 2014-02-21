package AST;
import AST.Visitor.Visitor;
import SemanticAnalyzer.SemanticTypes.VarType;

public abstract class Exp extends ASTNode {
  public VarType type;

  public Exp(int lineNumber) {
    super(lineNumber);
  }
  public abstract void accept(Visitor v);
}
