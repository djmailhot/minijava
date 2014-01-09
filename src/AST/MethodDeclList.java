package AST;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclList extends ASTNode {
  private List<MethodDecl> list;

  public MethodDeclList(int lineNumber) {
    super(lineNumber);
    list = new ArrayList<MethodDecl>();
  }

  public void add(MethodDecl n) {
    list.add(n);
  }

  public MethodDecl get(int i)  {
    return list.get(i);
  }

  public int size() {
    return list.size();
  }
}
