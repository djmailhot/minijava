/*
 * Group cse401h
 * Jake Bailey, David Mailhot
 */

import java.io.*;
import java.util.Scanner;

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

    if (profileFileName == null) {
      System.err.println("No profile specified");
      System.exit(1);
    }

    Scanner source;
    Scanner profile;
    PrintStream out;

    if (inputFileName == null)
      source = new Scanner(System.in);
    else
      source = new Scanner(new File(inputFileName));

    profile = new Scanner(new File(profileFileName));

    if (outputFileName == null)
      out = System.out;
    else
      out = new PrintStream(new File(outputFileName));

    int lineNumber = 0;
    while (profile.hasNextInt()) {
      int count = profile.nextInt();
      if (count == 0)
        out.printf("Line %-3d: %6s  %s\n", lineNumber++, "-", source.nextLine());
      else
        out.printf("Line %-3d: %6d  %s\n", lineNumber++, count, source.nextLine());
    }
    
    while (source.hasNextLine()) {
      out.printf("Line %-3d: %6s  %s\n", lineNumber++, "-", source.nextLine());
    }
  }

}
