package AST.Visitor;

import AST.ASTNode;
import AST.ArrayAssign;
import AST.ArrayLength;
import AST.ArrayLookup;
import AST.Assign;
import AST.Block;
import AST.BooleanType;
import AST.Call;
import AST.ClassDecl;
import AST.ClassDeclExtends;
import AST.ClassDeclList;
import AST.ClassDeclSimple;
import AST.ConstantExp;
import AST.Display;
import AST.Divide;
import AST.DoubleArrayType;
import AST.DoubleLiteral;
import AST.DoubleType;
import AST.Equal;
import AST.Exp;
import AST.ExpList;
import AST.False;
import AST.Formal;
import AST.FormalList;
import AST.GreaterEqual;
import AST.GreaterThan;
import AST.Identifier;
import AST.IdentifierExp;
import AST.IdentifierType;
import AST.If;
import AST.IntArrayType;
import AST.IntegerLiteral;
import AST.IntegerType;
import AST.LessEqual;
import AST.LessThan;
import AST.MainClass;
import AST.MethodDecl;
import AST.MethodDeclList;
import AST.Minus;
import AST.Modulo;
import AST.NewIntArray;
import AST.NewDoubleArray;
import AST.NewObject;
import AST.Not;
import AST.NotEqual;
import AST.Plus;
import AST.Print;
import AST.Program;
import AST.ShortCircuitAnd;
import AST.ShortCircuitOr;
import AST.Statement;
import AST.StatementList;
import AST.This;
import AST.Times;
import AST.True;
import AST.Type;
import AST.VarDecl;
import AST.VarDeclList;
import AST.While;

public interface Visitor {
  // Display added for toy example language.  Not used in MiniJava AST
  public void visit(Display n);
  public void visit(Program n);
  public void visit(MainClass n);
  public void visit(ClassDeclSimple n);
  public void visit(ClassDeclExtends n);
  public void visit(VarDecl n);
  public void visit(MethodDecl n);
  public void visit(Formal n);
  public void visit(IntArrayType n);
  public void visit(DoubleArrayType n);
  public void visit(BooleanType n);
  public void visit(IntegerType n);
  public void visit(DoubleType n);
  public void visit(IdentifierType n);
  public void visit(Block n);
  public void visit(If n);
  public void visit(While n);
  public void visit(Print n);
  public void visit(Assign n);
  public void visit(ArrayAssign n);
  public void visit(Equal n);
  public void visit(NotEqual n);
  public void visit(LessThan n);
  public void visit(GreaterThan n);
  public void visit(LessEqual n);
  public void visit(GreaterEqual n);
  public void visit(ShortCircuitAnd n);
  public void visit(ShortCircuitOr n);
  public void visit(Plus n);
  public void visit(Minus n);
  public void visit(Times n);
  public void visit(Divide n);
  public void visit(Modulo n);
  public void visit(ArrayLookup n);
  public void visit(ArrayLength n);
  public void visit(Call n);
  public void visit(IntegerLiteral n);
  public void visit(DoubleLiteral n);
  public void visit(True n);
  public void visit(False n);
  public void visit(IdentifierExp n);
  public void visit(ConstantExp n);
  public void visit(This n);
  public void visit(NewIntArray n);
  public void visit(NewDoubleArray n);
  public void visit(NewObject n);
  public void visit(Not n);
  public void visit(Identifier n);
}
