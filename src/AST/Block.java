package AST;
import AST.Visitor.Visitor;

public class Block extends Statement {
  public StatementList sl;

  public Block(StatementList asl, int lineNumber) {
    super(lineNumber);
    this.sl = asl;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

