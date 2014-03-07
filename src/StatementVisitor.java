/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.util.HashSet;
import java.util.Set;

import AST.*;
import AST.Visitor.Visitor;

public class StatementVisitor implements Visitor {

  public Set<Integer> statementLines;

  public StatementVisitor() {
    statementLines = new HashSet<Integer>();
  }

  public void visit(Display n) {
    n.e.accept(this);
  }

  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
  }

  public void visit(MainClass n) {
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
  }

  public void visit(ClassDeclSimple n) {
    n.i.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  public void visit(ClassDeclExtends n) {
    n.i.accept(this);
    n.j.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
  }

  public void visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
  }

  public void visit(MethodDecl n) {
    n.t.accept(this);
    n.i.accept(this);
    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.get(i).accept(this);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
  }

  public void visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
  }

  public void visit(DoubleArrayType n) {
  }

  public void visit(BooleanType n) {
  }

  public void visit(IntegerType n) {
  }

  public void visit(DoubleType n) {
  }

  public void visit(IdentifierType n) {
  }

  public void visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }

  public void visit(If n) {
    statementLines.add(n.getLineNumber());
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
  }

  public void visit(While n) {
    statementLines.add(n.getLineNumber());
    n.e.accept(this);
    n.s.accept(this);
  }

  public void visit(Print n) {
    statementLines.add(n.getLineNumber());
    n.e.accept(this);
  }

  public void visit(Assign n) {
    statementLines.add(n.getLineNumber());
    n.i.accept(this);
    n.e.accept(this);
  }

  public void visit(ArrayAssign n) {
    statementLines.add(n.getLineNumber());
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(ShortCircuitAnd n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(ShortCircuitOr n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Equal n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(LessEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(GreaterEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(Modulo n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  public void visit(ArrayLength n) {
    n.e.accept(this);
  }

  public void visit(Call n) {
    n.e.accept(this);
    n.i.accept(this);
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
      if (i+1 < n.el.size()) {
      }
    }
  }

  public void visit(IntegerLiteral n) {
  }

  public void visit(DoubleLiteral n) {
  }

  public void visit(True n) {
  }

  public void visit(False n) {
  }

  public void visit(IdentifierExp n) {
  }

  public void visit(ConstantExp n) {
  }

  public void visit(This n) {
  }

  public void visit(NewIntArray n) {
    n.e.accept(this);
  }

  public void visit(NewDoubleArray n) {
    n.e.accept(this);
  }

  public void visit(NewObject n) {
  }

  public void visit(Not n) {
    n.e.accept(this);
  }

  public void visit(Identifier n) {
  }
}
