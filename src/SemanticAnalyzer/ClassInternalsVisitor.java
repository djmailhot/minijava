/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

package SemanticAnalyzer;

import java.util.Map;

import AST.*;
import AST.Visitor.Visitor;
import SemanticAnalyzer.ErrorMessages;
import SemanticAnalyzer.SemanticTypes.*;

public class ClassInternalsVisitor implements Visitor {

  private final Map<String, ClassVarType> classScope;
  private Map<String, VarType> localScope;
  private Map<String, MethodMetadata> methodScope;
  private Map<String, VarType> paramsScope;

  private ClassVarType currentClass;

  public ClassInternalsVisitor(ProgramMetadata pm) {
    this.classScope = pm.classes;
    this.localScope = null;
    this.methodScope = null;
    this.paramsScope = null;
  }

  /**
   * Converts the specifed AST node type to the corresponding VarType
   *
   * Returns the VarType, otherwise null if no type found
   */
  private VarType deriveVarType(Type type, int lineNumber) {
    if (type instanceof IntegerType) {
      return Primitive.INT;
    } else if (type instanceof DoubleType) {
      return Primitive.DOUBLE;
    } else if (type instanceof BooleanType) {
      return Primitive.BOOLEAN;
    } else if (type instanceof IntArrayType) {
      return Primitive.INT_ARRAY;
    } else if (type instanceof DoubleArrayType) {
      return Primitive.DOUBLE_ARRAY;
    } else if (type instanceof IdentifierType) {
      // This is a class name, so look for the class definition
      String identifierName = ((IdentifierType)type).s;
      if (classScope.containsKey(identifierName)) {
        ClassVarType ct = classScope.get(identifierName);
        return ct;
      } else {
        // variable type not found
        ErrorMessages.errSymbolNotFound(lineNumber, identifierName);
      }
    }
    return null;
  }


  /**
   * Searches up the stack of variable scopes for the specified identifier
   *
   * Returns the VarType of the variable, otherwise null if no variable found
   */
  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.get(i).accept(this);
    }
  }

  // Identifier i1;  the class name
  // Identifier i2;  params
  // Statement s;
  public void visit(MainClass n) {
    String className = n.i1.s;
    int lineNumber = n.getLineNumber();

    if (!classScope.containsKey(className)) {
      ErrorMessages.errSymbolNotFound(lineNumber, className);
    } else {
      // nothing, we're done
    }
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    String className = n.i.s;
    int lineNumber = n.getLineNumber();

    if (!classScope.containsKey(className)) {
      ErrorMessages.errSymbolNotFound(lineNumber, className);
    } else {

      currentClass = classScope.get(className);
      localScope = currentClass.fields;
      for (int i = 0; i < n.vl.size(); i++) {
        n.vl.get(i).accept(this);
      }
      localScope = null;

      methodScope = currentClass.methods;
      for (int i = 0; i < n.ml.size(); i++) {
        n.ml.get(i).accept(this);
      }
      currentClass = null;
      methodScope = null;
    }
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    String className = n.i.s;
    String baseName = n.j.s;
    int lineNumber = n.getLineNumber();

    if (!classScope.containsKey(className)) {
      ErrorMessages.errSymbolNotFound(lineNumber, className);
    } else {
      currentClass = classScope.get(className);
      localScope = currentClass.fields;
      for (int i = 0; i < n.vl.size(); i++) {
        n.vl.get(i).accept(this);
      }
      localScope = null;

      methodScope = currentClass.methods;
      for (int i = 0; i < n.ml.size(); i++) {
        n.ml.get(i).accept(this);
      }
      currentClass = null;
      methodScope = null;
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    int lineNumber = n.getLineNumber();
    String varName = n.i.s;

    // check the variable type
    VarType varType = deriveVarType(n.t, lineNumber);

    // check the name
    if (localScope.containsKey(varName)) {
      // duplicate name
      ErrorMessages.errDuplicateVariable(lineNumber, varName);
    } else if (varType != null) {
      // its actually good!
      localScope.put(varName, varType);
    } // forget about it and move on
  }

  // Type t;  the return type
  // Identifier i;  method name
  // FormalList fl;  parameters
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;  return expression
  public void visit(MethodDecl n) {
    int lineNumber = n.getLineNumber();
    String methodName = n.i.s;

    VarType returnType = deriveVarType(n.t, lineNumber);

    MethodMetadata mm = new MethodMetadata(currentClass, returnType, methodName, lineNumber);

    paramsScope = mm.params;
    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.get(i).accept(this);
    }

    localScope = mm.localVars;
    for (int i = 0; i < n.vl.size(); i++) {
      VarDecl vd = n.vl.get(i);
      String localVar = vd.i.s;
      // check whether any local var has the same name as a formal parameter
      if (paramsScope.containsKey(localVar)) {
        ErrorMessages.errDuplicateVariable(vd.getLineNumber(), localVar);
      }
      vd.accept(this);
    }
    paramsScope = null;
    localScope = null;

    methodScope.put(methodName, mm);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    int lineNumber = n.getLineNumber();
    String varName = n.i.s;

    // check the variable type
    VarType varType = deriveVarType(n.t, lineNumber);

    // check the name
    if (paramsScope.containsKey(varName)) {
      // duplicate name
      ErrorMessages.errDuplicateVariable(lineNumber, varName);
    } else if (varType != null) {
      // its actually good!
      paramsScope.put(varName, varType);
    } // forget about it and move on
  }

 // The ClassInternalsVisitor is only interested in class fields and method declarations
  public void visit(Display n) {}
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
