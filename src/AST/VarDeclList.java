package AST;

import java.util.ArrayList;
import java.util.List;

public class VarDeclList extends ASTNode {
  private List<VarDecl> list;

  public VarDeclList(int lineNumber) {
    super(lineNumber);
    list = new ArrayList<VarDecl>();
  }

  public void add(VarDecl n) {
    list.add(n);
  }

  public VarDecl get(int i) {
    return list.get(i);
  }

  public int size() {
    return list.size();
  }
}
