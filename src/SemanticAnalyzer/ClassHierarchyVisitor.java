/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package SemanticAnalyzer;

import AST.*;
import AST.Visitor.Visitor;
import SemanticAnalyzer.ErrorMessages;
import SemanticAnalyzer.SemanticTypes.*;

public class ClassHierarchyVisitor implements Visitor {

  private ProgramMetadata pm;

  public ClassHierarchyVisitor(ProgramMetadata pm) {
    this.pm = pm;
  }

  public void visit(Program n) {
    for (int i = 0; i < n.cl.size(); i++)
      n.cl.get(i).accept(this);
  }

  public void visit(ClassDeclExtends n) {
    String childName = n.i.s;
    String parentName = n.j.s;

    ClassVarType child = pm.classes.get(childName);
    ClassVarType parent = pm.classes.get(parentName);

    child.superclass = parent;

    // Detect cycles. If one is detected, exit.
    while (parent != null) {
      if (parent == child)
        ErrorMessages.errClassHierarchyCycle(n.getLineNumber(), childName);
      parent = parent.superclass;
    }
  }

  // The ClassHierarchyVisitor is only interested in class extends declarations.
  public void visit(MainClass n) {}
  public void visit(ClassDeclSimple n) {}
  public void visit(Display n) {}
  public void visit(VarDecl n) {}
  public void visit(MethodDecl n) {}
  public void visit(Formal n) {}
  public void visit(IntArrayType n) {}
  public void visit(DoubleArrayType n) {}
  public void visit(BooleanType n) {}
  public void visit(IntegerType n) {}
  public void visit(DoubleType n) {}
  public void visit(IdentifierType n) {}
  public void visit(Block n) {}
  public void visit(If n) {}
  public void visit(While n) {}
  public void visit(Print n) {}
  public void visit(Assign n) {}
  public void visit(ArrayAssign n) {}
  public void visit(Equal n) {}
  public void visit(NotEqual n) {}
  public void visit(LessThan n) {}
  public void visit(GreaterThan n) {}
  public void visit(LessEqual n) {}
  public void visit(GreaterEqual n) {}
  public void visit(ShortCircuitAnd n) {}
  public void visit(ShortCircuitOr n) {}
  public void visit(Plus n) {}
  public void visit(Minus n) {}
  public void visit(Times n) {}
  public void visit(Divide n) {}
  public void visit(Modulo n) {}
  public void visit(ArrayLookup n) {}
  public void visit(ArrayLength n) {}
  public void visit(Call n) {}
  public void visit(IntegerLiteral n) {}
  public void visit(DoubleLiteral n) {}
  public void visit(True n) {}
  public void visit(False n) {}
  public void visit(IdentifierExp n) {}
  public void visit(ConstantExp n) {}
  public void visit(This n) {}
  public void visit(NewIntArray n) {}
  public void visit(NewDoubleArray n) {}
  public void visit(NewObject n) {}
  public void visit(Not n) {}
  public void visit(Identifier n) {}
}
