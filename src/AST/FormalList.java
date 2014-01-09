package AST;

import java.util.ArrayList;
import java.util.List;

public class FormalList extends ASTNode {
  private List<Formal> list;

  public FormalList(int lineNumber) {
    super(lineNumber);
    this.list = new ArrayList<Formal>();
  }

  public void add(Formal n) {
    list.add(n);
  }

  public Formal get(int i)  {
    return list.get(i);
  }

  public int size() {
    return list.size();
  }
}
