/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package CodeGenerator;

import java.util.Map;

import AST.*;
import AST.Visitor.Visitor;
import SemanticAnalyzer.SemanticTypes.*;

public class CodeGeneratorVisitor implements Visitor {

  private final CodeGenerator cg;
  private final Map<String, ClassVarType> classes;

  private ClassVarType currentClass;

  public CodeGeneratorVisitor(CodeGenerator cg, ProgramMetadata pm) {
    this.cg = cg;
    this.classes = pm.classes;
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    n.e.accept(this);
    cg.genDisplay();
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
    cg.genPrintStatementCountsDeclaration();
    cg.genStatementCountsDeclaration();
    cg.genVtables(classes.values());
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    currentClass = classes.get(n.i1.s);
    cg.genFunctionEntry("asm_main");
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
    cg.genPrintStatementCounts();
    cg.genFunctionExit("asm_main");
    currentClass = null;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    currentClass = classes.get(n.i.s);
    n.i.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
    currentClass = null;
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    currentClass = classes.get(n.i.s);
    n.i.accept(this);
    n.j.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.get(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.get(i).accept(this);
    }
    currentClass = null;
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    n.t.accept(this);
    n.i.accept(this);
    cg.genMethodEntry(currentClass.name, currentClass.getMethod(n.i.s));
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
    cg.genMethodExit(currentClass.name, currentClass.getMethod(n.i.s));
  }

  // Type t;
  // Identifier i;
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

  // String s;
  public void visit(IdentifierType n) {
  }

  // StatementList sl;
  public void visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.get(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    cg.printComment("If statement");
    String labelFalse = cg.newLabel("false");
    String labelEnd = cg.newLabel("end");

    cg.genStatementCountIncrement(n.getLineNumber());
    n.e.accept(this);
    cg.genJmpIfFalse(labelFalse);
    n.s1.accept(this);
    cg.genJmp(labelEnd);
    cg.printLocalLabel(labelFalse);
    n.s2.accept(this);
    cg.printLocalLabel(labelEnd);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    cg.printComment("While statement");
    String labelTop = cg.newLabel("top");
    String labelBot = cg.newLabel("bot");

    cg.genStatementCountIncrement(n.getLineNumber());
    cg.printLocalLabel(labelTop);
    n.e.accept(this);
    cg.genJmpIfFalse(labelBot);
    n.s.accept(this);
    cg.genJmp(labelTop);
    cg.printLocalLabel(labelBot);
  }

  // Exp e;
  public void visit(Print n) {
    cg.genStatementCountIncrement(n.getLineNumber());
    n.e.accept(this);
    if (n.e.type == Primitive.DOUBLE) {
      cg.genPrintDouble();
    } else {
      cg.genPrintInteger();
    }
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.e.accept(this);
    cg.genStatementCountIncrement(n.getLineNumber());
    cg.genAssign(currentClass, n.i.s);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genStatementCountIncrement(n.getLineNumber());
    cg.genArrayAssign(currentClass, n.i.s);
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    cg.printComment("and operator");
    String labelFalse = cg.newLabel("false");
    String labelEnd = cg.newLabel("end");

    n.e1.accept(this);
    cg.genJmpIfFalse(labelFalse);
    n.e2.accept(this);
    cg.genJmpIfFalse(labelFalse);
    cg.genIntegerConstant(1);
    cg.genJmp(labelEnd);
    cg.printLocalLabel(labelFalse);
    cg.decrementStackItemCount();
    cg.genIntegerConstant(0);
    cg.printLocalLabel(labelEnd);
  }

  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    cg.printComment("or operator");
    String labelTrue = cg.newLabel("true");
    String labelEnd = cg.newLabel("end");

    n.e1.accept(this);
    cg.genJmpIfTrue(labelTrue);
    n.e2.accept(this);
    cg.genJmpIfTrue(labelTrue);
    cg.genIntegerConstant(0);
    cg.genJmp(labelEnd);
    cg.printLocalLabel(labelTrue);
    cg.decrementStackItemCount();
    cg.genIntegerConstant(1);
    cg.printLocalLabel(labelEnd);
  }

  // Exp e1,e2;
  public void visit(Equal n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genEqual();
  }

  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genNotEqual();
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genLessThan();
  }

  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genGreaterThan();
  }

  // Exp e1,e2;
  public void visit(LessEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genLessEqual();
  }

  // Exp e1,e2;
  public void visit(GreaterEqual n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genGreaterEqual();
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genAdd();
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genSub();
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genMul();
  }

  // Exp e1,e2;
  public void visit(Divide n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genDiv();
  }

  // Exp e1,e2;
  public void visit(Modulo n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genMod();
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    cg.genArrayLookup();
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
    cg.genArrayLength();
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    n.i.accept(this);
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
        cg.genIntegerActual(i + 1);  // the first register rdi is reserved
    }
    cg.genIntegerActual(0);  // push the current class
    ClassVarType classType = (ClassVarType) n.e.type;
    cg.genMethodCall(classType, classType.getMethod(n.i.s));
  }

  // int i;
  public void visit(IntegerLiteral n) {
    cg.genIntegerConstant(n.i);
  }

  // double d;
  public void visit(DoubleLiteral n) {
    cg.genDoubleConstant(n.d);
  }

  public void visit(True n) {
    cg.genIntegerConstant(1);
  }

  public void visit(False n) {
    cg.genIntegerConstant(0);
  }

  public void visit(IdentifierExp n) {
    cg.genLookup(currentClass, n.s);
  }

  public void visit(ConstantExp n) {
    cg.genIntegerConstant(n.value);
  }

  public void visit(This n) {
    cg.genThis();
  }

  // Exp e;
  public void visit(NewIntArray n) {
    n.e.accept(this);
    cg.genAllocateArray();
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    n.e.accept(this);
    cg.genAllocateArray();
  }

  // Identifier i;
  public void visit(NewObject n) {
    ClassVarType newClass = classes.get(n.i.s);

    cg.genAllocateObject(newClass);
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
    cg.genNot();
  }

  // String s;
  public void visit(Identifier n) {
  }
}
