package AST;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclList extends ASTNode {
  private List<ClassDecl> list;

  public ClassDeclList(int lineNumber) {
    super(lineNumber);
    list = new ArrayList<ClassDecl>();
  }

  public void add(ClassDecl n) {
    list.add(n);
  }

  public ClassDecl get(int i)  {
    return list.get(i);
  }

  public int size() {
    return list.size();
  }
}
