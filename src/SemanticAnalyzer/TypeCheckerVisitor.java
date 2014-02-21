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

  public TypeCheckerVisitor(ProgramMetadata pm) {
    this.classes = pm.classes;
  }

  private void assertEqualType(VarType expectedType, VarType actualType, int lineNum) {
    if (!actualType.equals(expectedType))
      ErrorMessages.errIncompatibleTypes(lineNum, expectedType, actualType);
  }

  private void assertSupertype(VarType expectedType, VarType actualType, int lineNum) {
    if (!expectedType.supertypeOrEqual(actualType))
      ErrorMessages.errIncompatibleTypes(lineNum, expectedType, actualType);
  }

  private VarType getTypeOfVariable(String id, int lineNum) {
    VarType type = currentClass.getFieldType(id);

    if (type == null)
      type = currentMethod.localVars.get(id);

    if (type == null)
      type = currentMethod.params.get(id);

    if (type == null)
      ErrorMessages.errSymbolNotFound(lineNum, id);

    return type;
  }

  private boolean isNumber(VarType t) {
    return (t == Primitive.INT) || (t == Primitive.DOUBLE);
  }

  private boolean isArray(VarType t) {
    return (t == Primitive.INT_ARRAY || t == Primitive.DOUBLE_ARRAY);
  }

  private VarType evalBooleanOperator(Exp e1, Exp e2, String operator) {
    e1.accept(this);
    e2.accept(this);

    if (!(e1.type == Primitive.BOOLEAN) || !(e2.type == Primitive.BOOLEAN))
      ErrorMessages.errBadOperandTypes(e1.getLineNumber(), operator, e1.type, e2.type);

    return Primitive.BOOLEAN;
  }

  private VarType evalEqualityOperator(Exp e1, Exp e2, String operator) {
    e1.accept(this);
    e2.accept(this);

    if (e1.type instanceof Primitive)
      assertEqualType(e1.type, e2.type, e1.getLineNumber());
    else if (!e1.type.subtypeOrEqual(e2.type) && !e2.type.subtypeOrEqual(e1.type))
      ErrorMessages.errBadOperandTypes(e1.getLineNumber(), operator, e1.type, e2.type);

    return Primitive.BOOLEAN;
  }

  private VarType evalNumericOperator(Exp e1, Exp e2, String operator) {
    e1.accept(this);
    e2.accept(this);

    if (!isNumber(e1.type) || !isNumber(e2.type) || e1.type != e2.type)
      ErrorMessages.errBadOperandTypes(e1.getLineNumber(), operator, e1.type, e2.type);

    return e1.type;
}

  private VarType evalComparisonOperator(Exp e1, Exp e2, String operator) {
    evalNumericOperator(e1, e2, operator);
    return Primitive.BOOLEAN;
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
    assertSupertype(currentMethod.returnType, n.e.type, n.e.getLineNumber());
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
    assertEqualType(Primitive.BOOLEAN, n.e.type, n.getLineNumber());

    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    n.e.accept(this);
    assertEqualType(Primitive.BOOLEAN, n.e.type, n.getLineNumber());

    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    n.e.accept(this);
    if (!isNumber(n.e.type))
      ErrorMessages.errInvalidPrintArgument(n.getLineNumber(), n.e.type);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    VarType expectedType = getTypeOfVariable(n.i.s, n.i.getLineNumber());

    n.e.accept(this);
    assertSupertype(expectedType, n.e.type, n.getLineNumber());
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    VarType arrayType = getTypeOfVariable(n.i.s, n.i.getLineNumber());
    if (!isArray(arrayType))
      ErrorMessages.errIllegalArrayLookup(n.i.getLineNumber(), arrayType);

    n.e1.accept(this);
    assertEqualType(Primitive.INT, n.e1.type, n.e1.getLineNumber());

    n.e2.accept(this);
    if (arrayType == Primitive.INT_ARRAY)
      assertEqualType(Primitive.INT, n.e2.type, n.e2.getLineNumber());
    else
      assertEqualType(Primitive.DOUBLE, n.e2.type, n.e2.getLineNumber());
  }

  // Exp e1,e2;
  public void visit(ShortCircuitAnd n) {
    n.type = evalBooleanOperator(n.e1, n.e2, "&&");
  }

  // Exp e1,e2;
  public void visit(ShortCircuitOr n) {
    n.type = evalBooleanOperator(n.e1, n.e2, "||");
  }

  // Exp e1,e2;
  public void visit(Equal n) {
    n.type = evalEqualityOperator(n.e1, n.e2, "==");
  }

  // Exp e1,e2;
  public void visit(NotEqual n) {
    n.type = evalEqualityOperator(n.e1, n.e2, "!=");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.type = evalComparisonOperator(n.e1, n.e2, "<");
  }

  // Exp e1,e2;
  public void visit(GreaterThan n) {
    n.type = evalComparisonOperator(n.e1, n.e2, ">");
  }

  // Exp e1,e2;
  public void visit(LessEqual n) {
    n.type = evalComparisonOperator(n.e1, n.e2, "<=");
  }

  // Exp e1,e2;
  public void visit(GreaterEqual n) {
    n.type = evalComparisonOperator(n.e1, n.e2, ">=");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.type = evalNumericOperator(n.e1, n.e2, "+");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.type = evalNumericOperator(n.e1, n.e2, "-");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.type = evalNumericOperator(n.e1, n.e2, "*");
  }

  // Exp e1,e2;
  public void visit(Divide n) {
    n.type = evalNumericOperator(n.e1, n.e2, "/");
  }

  // Exp e1,e2;
  public void visit(Modulo n) {
    n.type = evalNumericOperator(n.e1, n.e2, "%");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    VarType arrayType = n.e1.type;
    if (!isArray(arrayType))
      ErrorMessages.errIllegalArrayLookup(n.e1.getLineNumber(), arrayType);

    n.e2.accept(this);
    assertEqualType(Primitive.INT, n.e2.type, n.e2.getLineNumber());

    if (arrayType == Primitive.INT_ARRAY)
      n.type = Primitive.INT;
    else
      n.type = Primitive.DOUBLE;
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
    VarType arrayType = n.e.type;
    if (!isArray(arrayType))
      ErrorMessages.errIllegalArrayLookup(n.e.getLineNumber(), arrayType);

    n.type = Primitive.INT;
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    if (!(n.e.type instanceof ClassVarType))
      ErrorMessages.errIllegalDereference(n.getLineNumber(), n.e.type);
    ClassVarType receiver = (ClassVarType) n.e.type;

    MethodMetadata method = receiver.getMethod(n.i.s);
    if (method == null)
      ErrorMessages.errSymbolNotFound(n.i.getLineNumber(), n.i.s);

    if (method.params.size() != n.el.size())
      ErrorMessages.errArgListLength(n.el.getLineNumber(), receiver, method);

    Iterator<VarType> formals = method.params.values().iterator();
    for (int i = 0; i < n.el.size(); i++) {
      n.el.get(i).accept(this);
      assertSupertype(formals.next(), n.el.get(i).type, n.el.get(i).getLineNumber());
    }

    n.type = method.returnType;
  }

  // int i;
  public void visit(IntegerLiteral n) {
    n.type = Primitive.INT;
  }

  public void visit(DoubleLiteral n) {
    n.type = Primitive.DOUBLE;
  }

  public void visit(True n) {
    n.type = Primitive.BOOLEAN;
  }

  public void visit(False n) {
    n.type = Primitive.BOOLEAN;
  }

  public void visit(IdentifierExp n) {
    n.type = getTypeOfVariable(n.s, n.getLineNumber());
  }

  public void visit(ConstantExp n) {
    n.type = Primitive.INT;
  }

  public void visit(This n) {
    n.type = currentClass;
  }

  // Exp e;
  public void visit(NewIntArray n) {
    n.e.accept(this);
    assertEqualType(Primitive.INT, n.e.type, n.getLineNumber());

    n.type = Primitive.INT_ARRAY;
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    n.e.accept(this);
    assertEqualType(Primitive.INT, n.e.type, n.getLineNumber());

    n.type = Primitive.DOUBLE_ARRAY;
  }

  // Identifier i;
  public void visit(NewObject n) {
    ClassVarType objectType = classes.get(n.i.s);
    if (objectType == null)
      ErrorMessages.errSymbolNotFound(n.getLineNumber(), n.i.s);

    n.type = objectType;
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
    assertEqualType(Primitive.BOOLEAN, n.e.type, n.getLineNumber());

    n.type = Primitive.BOOLEAN;
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
