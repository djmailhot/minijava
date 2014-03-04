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
import AST.Visitor.Visitor;
import AST.Visitor.PrettyPrintVisitor;
import AST.While;
import CodeGenerator.CodeGenerator;
import CodeGenerator.CodeGeneratorVisitor;
import Parser.parser;
import Scanner.scanner;

import java_cup.runtime.Symbol;

import java.util.List;

public class TestParser {

  public static void main(String [] args) {
    String outputFileName = null;
    String inputFileName = null;

    boolean tracePrettyPrint = false;
    boolean traceParser = false;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-P")) {
        tracePrettyPrint = true;
      } else if (args[i].equals("-i")) {
        inputFileName = args[i+1];
        i += 1;
      } else if (args[i].equals("-o")) {
        outputFileName = args[i+1];
        i += 1;
      } else if (args[i].equals("-p")) {
        traceParser = true;
      } else {
        System.err.println("Unknown argument <" + args[i] + ">");
      }
    }

    try {
      //
      // create a scanner on the input file
      //
      scanner s = new scanner(System.in);
      parser p = new parser(s);
      CodeGenerator cg = new CodeGenerator(outputFileName, false);
      Symbol root;

      cg.genFunctionEntry("asm_main");
      //
      // replace p.parse() with p.debug_parse() in next line to see trace of
      // parser shift/reduce actions during parse
      //
      root = traceParser ? p.debug_parse() : p.parse();
      Program program = (Program) root.value;
      program.accept(new PrettyPrintVisitor());
      System.out.println();

      //
      // System.out.print("\n" + "Parsing completed");
      //
      cg.genFunctionExit("asm_main");
    } catch (Exception e) {
      //
      // yuck: some kind of error in the compiler implementation
      // that we're not expecting (a bug!)
      //
      System.err.println("Unexpected internal compiler error: " + e.toString());

      //
      // print out a stack dump
      //
      e.printStackTrace();
    }
  }
}
