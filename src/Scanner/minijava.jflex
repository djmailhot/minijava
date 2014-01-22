/*
 * JFlex specification for the lexical analyzer for a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 * CSE 401/P501 Au11
 */

package Scanner;

import Parser.sym;

import java_cup.runtime.Symbol;

%%

%public
%final
%class scanner
%unicode
%cup
%line
%column

/* Code copied into the generated scanner class.  */
/* Can be referenced in scanner action code. */
%{
  // Return new symbol objects with line and column numbers in the symbol
  // left and right fields. This abuses the original idea of having left
  // and right be character positions, but is is more useful and
  // follows an example in the JFlex documentation.
  private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Return a readable representation of symbol s (aka token)
  public String symbolToString(Symbol s) {
    String rep;
    switch (s.sym) {
      case sym.STRING:
        return "STRING";
      case sym.FALSE:
        return "FALSE";
      case sym.TRUE:
        return "TRUE";
      case sym.LENGTH:
        return "LENGTH";
      case sym.WHILE:
        return "WHILE";
      case sym.IF:
        return "IF";
      case sym.BOOLEAN:
        return "BOOLEAN";
      case sym.DOUBLE:
        return "DOUBLE";
      case sym.INT:
        return "INT";
      case sym.CLASS:
        return "CLASS";
      case sym.EXTENDS:
        return "EXTENDS";
      case sym.NEW:
        return "NEW";
      case sym.PUBLIC:
        return "PUBLIC";
      case sym.MAIN:
        return "MAIN";
      case sym.VOID:
        return "VOID";
      case sym.STATIC:
        return "STATIC";
      case sym.NOT:
        return "NOT";
      case sym.EQUAL:
        return "EQUAL";
      case sym.NOTEQUAL:
        return "NOTEQUAL";
      case sym.LESSTHAN:
        return "LESSTHAN";
      case sym.GREATERTHAN:
        return "GREATERTHAN";
      case sym.LESSEQUAL:
        return "LESSEQUAL";
      case sym.GREATEREQUAL:
        return "GREATEREQUAL";
      case sym.AND:
        return "AND";
      case sym.OR:
        return "OR";
      case sym.BECOMES:
        return "BECOMES";
      case sym.SEMICOLON:
        return "SEMICOLON";
      case sym.PLUS:
        return "PLUS";
      case sym.MINUS:
        return "MINUS";
      case sym.STAR:
        return "STAR";
      case sym.FSLASH:
        return "FSLASH";
      case sym.MOD:
        return "MOD";
      case sym.DOT:
        return "DOT";
      case sym.LPAREN:
        return "LPAREN";
      case sym.RPAREN:
        return "RPAREN";
      case sym.LBRACKET:
        return "LBRACKET";
      case sym.RBRACKET:
        return "RBRACKET";
      case sym.LBRACE:
        return "LBRACE";
      case sym.RBRACE:
        return "RBRACE";
      case sym.DISPLAY:
        return "DISPLAY";
      case sym.PRINT:
        return "PRINT";
      case sym.IDENTIFIER:
        return "ID(" + (String)s.value + ")";
      case sym.CONSTANT:
        return "CONSTANT(" + (String)s.value + ")";
      case sym.EOF:
        return "<EOF>";
      case sym.error:
        return "<ERROR>";
      default:
        return "<UNEXPECTED TOKEN " + s.toString() + ">";
    }
  }
%}

/* Helper definitions */
letter = [a-zA-Z]
digit = [0-9]
eol = [\r\n]
white = {eol}|[ \t]

%%

/* Token definitions */

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */
"display" { return symbol(sym.DISPLAY); }
"System.out.println" { return symbol(sym.PRINT); }

"class" { return symbol(sym.CLASS); }
"extends" { return symbol(sym.EXTENDS); }
"new" { return symbol(sym.NEW); }

"public" { return symbol(sym.PUBLIC); }
"static" { return symbol(sym.STATIC); }
"void" { return symbol(sym.VOID); }
"main" { return symbol(sym.MAIN); }
"String" { return symbol(sym.STRING); }

"int" { return symbol(sym.INT); }
"double" { return symbol(sym.DOUBLE); }
"boolean" { return symbol(sym.BOOLEAN); }

"if" { return symbol(sym.IF); }
"while" { return symbol(sym.WHILE); }

"length" { return symbol(sym.LENGTH); }

"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }

/* operators */
"!"  { return symbol(sym.NOT); }
"==" { return symbol(sym.EQUAL); }
"!=" { return symbol(sym.NOTEQUAL); }
"<"  { return symbol(sym.LESSTHAN); }
">"  { return symbol(sym.GREATERTHAN); }
"<=" { return symbol(sym.LESSEQUAL); }
">=" { return symbol(sym.GREATEREQUAL); }

"&&" { return symbol(sym.AND); }
"||" { return symbol(sym.OR); }

"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.STAR); }
"/" { return symbol(sym.FSLASH); }
"%" { return symbol(sym.MOD); }
"=" { return symbol(sym.BECOMES); }
"." { return symbol(sym.DOT); }

/* delimiters */
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }
"{" { return symbol(sym.LBRACE); }
"}" { return symbol(sym.RBRACE); }
";" { return symbol(sym.SEMICOLON); }

/* identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* constants */
{digit}+ { return symbol(sym.CONSTANT, yytext()); }

/* comment */
"//" .* {eol} { /* ignore comments */ }

/* whitespace */
{white}+ { /* ignore whitespace */ }

/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
    "\nunexpected character in input: '" + yytext() + "' at line " +
    (yyline+1) + " column " + (yycolumn+1));
  }
