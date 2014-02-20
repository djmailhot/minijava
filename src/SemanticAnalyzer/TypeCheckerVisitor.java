/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package SemanticAnalyzer;

import java.util.Iterator;
import java.util.Map;

import AST.*;
import AST.Visitor.Visitor;
import SemanticAnalyzer.SemanticTypes.*;

public class TypeCheckerVisitor implements Visitor {

  private final Map<String, ClassVarType> classes;

  private ClassVarType currentClass;
  private MethodMetadata currentMethod;

  /** Contains the type evaluated by an invocation of visit().
   *  This lets us avoid implementing a new Visitor that returns a VarType. */
  private VarType evaluatedType;

  public TypeCheckerVisitor(ProgramMetadata pm) {
    this.classes = pm.classes;
  }

  private void assertEqualType(VarType expectedType, VarType actualType, int lineNum) {
    if (!actualType.equals(expectedType))
      ErrorMessages.errIncompatibleTypes(lineNum, expectedType, actualType);
  }

  private void assertSupertype(VarType expectedType, VarType actualType, int lineNum) {
    if (!actualType.subtypeOrEqual(expectedType))
      ErrorMessages.errIncompatibleTypes(lineNum, expectedType, actualType);
  }

  private VarType getTypeOfVariable(String id, int lineNum) {
    VarType type = currentClass.fields.get(id);

    if (type == null)
      type = currentMethod.localVars.get(id);

    if (type == null)
      type = currentMethod.params.get(id);

    if (type == null)
      ErrorMessages.errSymbolNotFound(lineNum, id);

    return type;
  }

  private boolean isNumber(VarType t) {
    return (t instanceof IntegerVarType) || (t instanceof DoubleVarType);
  }

  private void checkNumericOperator(Exp e1, Exp e2, String operator) {
    e1.accept(this);
    VarType type1 = evaluatedType;

    e2.accept(this);
    VarType type2 = evaluatedType;

    if (!isNumber(type1) || !isNumber(type2) || !type1.equals(type2))
      ErrorMessages.errBadOperandTypes(e1.getLineNumber(), operator, type1, type2);
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
        n.cl.get(i).accept(this);
    }
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
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
    currentMethod = currentClass.methods.get(n.i.s);
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
    assertSupertype(currentMethod.returnType, evaluatedType, n.e.getLineNumber());
    currentMethod = null;
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);
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
    n.e.accept(this);
    assertEqualType(BooleanVarType.singleton(), evaluatedType, n.getLineNumber());

    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    n.e.accept(this);
    assertEqualType(BooleanVarType.singleton(), evaluatedType, n.getLineNumber());

    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    n.e.accept(this);
    if (!isNumber(evaluatedType))
      ErrorMessages.errInvalidPrintArgument(n.getLineNumber(), evaluatedType);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    VarType expectedType = getTypeOfVariable(n.i.s, n.i.getLineNumber());

    n.e.accept(this);
    assertSupertype(expectedType, evaluatedType, n.getLineNumber());
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    VarType expectedType = getTypeOfVariable(n.i.s, n.i.getLineNumber());

    n.e1.accept(this);
    assertEqualType(IntegerVarType.singleton(), evaluatedType, n.e1.getLineNumber());

    n.e2.accept(this);
    assertEqualType(expectedType, evaluatedType, n.e2.getLineNumber());
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!(type1 instanceof BooleanVarType) || !(type2 instanceof BooleanVarType))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "&&", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!(type1 instanceof BooleanVarType) || !(type2 instanceof BooleanVarType))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "&&", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(Equal n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (type1 instanceof PrimitiveVarType)
      assertEqualType(type1, type2, n.getLineNumber());
    else if (!type1.subtypeOrEqual(type2) && !type2.subtypeOrEqual(type1))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "==", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (type1 instanceof PrimitiveVarType)
      assertEqualType(type1, type2, n.getLineNumber());
    else if (!type1.subtypeOrEqual(type2) && !type2.subtypeOrEqual(type1))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "!=", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!isNumber(type1) || !isNumber(type2) || !type1.equals(type2))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "<", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!isNumber(type1) || !isNumber(type2) || !type1.equals(type2))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), ">", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(LessEqual n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!isNumber(type1) || !isNumber(type2) || !type1.equals(type2))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), "<=", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(GreaterEqual n) {
    n.e1.accept(this);
    VarType type1 = evaluatedType;

    n.e2.accept(this);
    VarType type2 = evaluatedType;

    if (!isNumber(type1) || !isNumber(type2) || !type1.equals(type2))
      ErrorMessages.errBadOperandTypes(n.getLineNumber(), ">=", type1, type2);

    evaluatedType = BooleanVarType.singleton();
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    checkNumericOperator(n.e1, n.e2, "+");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    checkNumericOperator(n.e1, n.e2, "-");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    checkNumericOperator(n.e1, n.e2, "*");
  }

  // Exp e1,e2;
  public void visit(Divide n) {
    checkNumericOperator(n.e1, n.e2, "/");
  }

  // Exp e1,e2;
  public void visit(Modulo n) {
    checkNumericOperator(n.e1, n.e2, "%");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    if (!(evaluatedType instanceof ClassVarType))
      ErrorMessages.errIllegalDereference(n.getLineNumber(), evaluatedType);
    ClassVarType callee = (ClassVarType) evaluatedType;

    MethodMetadata method = callee.methods.get(n.i.s);
    if (method == null)
      ErrorMessages.errSymbolNotFound(n.i.getLineNumber(), n.i.s);

    if (method.params.size() != n.el.size())
      ErrorMessages.errArgListLength(n.el.getLineNumber(), callee, method);

    Iterator<VarType> formals = method.params.values().iterator();
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
      assertEqualType(formals.next(), evaluatedType, n.el.get(i).getLineNumber());
    }

    evaluatedType = method.returnType;
  }

  // int i;
  public void visit(IntegerLiteral n) {
    evaluatedType = IntegerVarType.singleton();
  }

  public void visit(DoubleLiteral n) {
    evaluatedType = DoubleVarType.singleton();
  }

  public void visit(True n) {
    evaluatedType = BooleanVarType.singleton();
  }

  public void visit(False n) {
    evaluatedType = BooleanVarType.singleton();
  }

  public void visit(IdentifierExp n) {
    evaluatedType = getTypeOfVariable(n.s, n.getLineNumber());
  }

  public void visit(ConstantExp n) {
    evaluatedType = IntegerVarType.singleton();
  }

  public void visit(This n) {
    evaluatedType = currentClass;
  }

  // Exp e;
  public void visit(NewIntArray n) {
    n.e.accept(this);
    assertEqualType(IntegerVarType.singleton(), evaluatedType, n.getLineNumber());

    evaluatedType = IntegerArrayVarType.singleton();
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    n.e.accept(this);
    assertEqualType(IntegerVarType.singleton(), evaluatedType, n.getLineNumber());

    evaluatedType = DoubleArrayVarType.singleton();
  }

  // Identifier i;
  public void visit(NewObject n) {
    ClassVarType objectType = classes.get(n.i.s);
    if (objectType == null)
      ErrorMessages.errSymbolNotFound(n.getLineNumber(), n.i.s);

    evaluatedType = objectType;
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
    assertEqualType(BooleanVarType.singleton(), evaluatedType, n.getLineNumber());

    evaluatedType = BooleanVarType.singleton();
  }

  public void visit(Identifier n) {}
  public void visit(Display n) {}
  public void visit(IntArrayType n) {}
  public void visit(DoubleArrayType n) {}
  public void visit(BooleanType n) {}
  public void visit(IntegerType n) {}
  public void visit(DoubleType n) {}
  public void visit(IdentifierType n) {}
}
