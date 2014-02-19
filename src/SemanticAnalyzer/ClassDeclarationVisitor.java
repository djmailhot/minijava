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

public class ClassDeclarationVisitor implements Visitor {
  
  private Map<String, ClassVarType> classScope;

  public ClassDeclarationVisitor(ProgramMetadata pm) {
    this.classScope = pm.classes;
  }


  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
        n.cl.get(i).accept(this);
    }
  }

  private void addClass(String name, int lineNumber) {
    // check if we have a class name conflict
    if (classScope.containsKey(name)) {
      ErrorMessages.errDuplicateClass(lineNumber, name);
    }
    classScope.put(name, new ClassVarType());
  }

  // Identifier i1 -> the MainClass declared name
  // Identifier i2 -> args
  // Statement s;
  public void visit(MainClass n) {
    String name = n.i1.s;

    addClass(name, n.i1.getLineNumber());
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    String name = n.i.s;

    addClass(name, n.i.getLineNumber());
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    String name = n.i.s;

    addClass(name, n.i.getLineNumber());
    // Don't bother with the super class type
  }

  // The ClassDeclarationVisitor is only interested in class declarations
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
