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
      case sym.BECOMES:
        return "BECOMES";
      case sym.SEMICOLON:
        return "SEMICOLON";
      case sym.PLUS:
        return "PLUS";
      case sym.LPAREN:
        return "LPAREN";
      case sym.RPAREN:
        return "RPAREN";
      case sym.DISPLAY:
        return "DISPLAY";
      case sym.IDENTIFIER:
        return "ID(" + (String)s.value + ")";
      case sym.CONSTANT:
        return "CONSTANT(" + (String)s.value + ")";
      case sym.COMMENT:
        return "COMMENT";
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
any = [^\r\n]

%%

/* Token definitions */

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */
"display" { return symbol(sym.DISPLAY); }
"System.out.print" { return symbol(sym.PRINT); }

"class" { return symbol(sym.CLASS); };
"extends" { return symbol(sym.EXTENDS); };
"new" { return symbol(sym.NEW;); }

"public" { return symbol(sym.PUBLIC); };
"static" { return symbol(sym.STATIC); };
"void" { return symbol(sym.VOID); };
"main" { return symbol(sym.MAIN); };
"String" { return symbol(sym.STRING); };

"int" { return symbol(sym.INT;
"double" { return symbol(sym.DOUBLE); };
"boolean" { return symbol(sym.BOOLEAN); };

"if" { return symbol(sym.IF;
"while" { return symbol(sym.WHILE); };

"length" { return symbol(sym.LENGTH); };

"true" { return symbol(sym.TRUE); };
"false" { return symbol(sym.FALSE); };

/* operators */
"+" { return symbol(sym.PLUS); }
"=" { return symbol(sym.BECOMES); }

/* delimiters */
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
";" { return symbol(sym.SEMICOLON); }

/* identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* constants */
{digit}+ { return symbol(sym.CONSTANT, yytext()); }

/* comment */
"//"{any}*{eol} { return symbol(sym.COMMENT, yytext()); }


/* whitespace */
{white}+ { /* ignore whitespace */ }

/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
    "\nunexpected character in input: '" + yytext() + "' at line " +
    (yyline+1) + " column " + (yycolumn+1));
  }
