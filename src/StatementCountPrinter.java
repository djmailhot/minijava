/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.io.*;
import java.util.*;

import java_cup.runtime.Symbol;
import AST.Program;
import AST.Visitor.PrettyPrintVisitor;
import Parser.parser;
import Scanner.scanner;

public class StatementCountPrinter {

  public static void main(String[] args) throws FileNotFoundException {
    String inputFileName = null;
    String outputFileName = null;
    String profileFileName = null;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-i")) {
        inputFileName = args[i + 1];
        i += 1;
      } else if (args[i].equals("-o")) {
        outputFileName = args[i + 1];
        i += 1;
      } else if (args[i].equals("-p")) {
        profileFileName = args[i + 1];
        i += 1;
      } else {
        System.err.println("Unknown argument <" + args[i] + ">");
      }
    }

    if (inputFileName == null) {
      System.err.println("No input file specified");
      System.exit(1);
    }

    if (profileFileName == null) {
      System.err.println("No profile specified");
      System.exit(1);
    }

    Scanner source;
    Scanner profile;
    PrintStream out;

    source = new Scanner(new File(inputFileName));
    profile = new Scanner(new File(profileFileName));

    if (outputFileName == null)
      out = System.out;
    else
      out = new PrintStream(new File(outputFileName));

    Set<Integer> validStatements = getValidStatementLines(inputFileName);

    System.out.println(validStatements);

    int lineNumber = 1;
    String formatString = "Line %-3d: %6s  %s\n";

    while (profile.hasNextInt()) {
      int count = profile.nextInt();
      String countString = Integer.toString(count);

      if (count == 0) {
        if (validStatements.contains(lineNumber))
          countString = "-";
        else
          countString = " ";
      }

      out.printf(formatString, lineNumber++, countString, source.nextLine());
    }

    while (source.hasNextLine()) {
      String countString = null;

      if (validStatements.contains(lineNumber))
        countString = "-";
      else
        countString = " ";

      out.printf(formatString, lineNumber++, countString, source.nextLine());
    }
  }

  /**
   * Parse the given file and return a set of line numbers representing the
   * lines containing valid statements.
   */
  public static Set<Integer> getValidStatementLines(String sourceFilename) {
    Set<Integer> statements = null;

    try {
      scanner s = new scanner(new FileInputStream(sourceFilename));
      parser p = new parser(s);
      Symbol root;

      root = p.parse();
      Program program = (Program) root.value;
      StatementVisitor statementVisitor = new StatementVisitor();
      program.accept(statementVisitor);
      statements = statementVisitor.statementLines;

    } catch (Exception e) {
      System.err.println("Unexpected internal compiler error: " + e.toString());
      e.printStackTrace();
    }

    return statements;
  }

}
