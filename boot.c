/*
 *  boot.c: Main program for CSE minijava compiled code
 *          Robert R. Henry
 *
 *  Contents:
 *    Main program that calls the compiled code as a function;
 *    Functions get and put that can be used by compiled code
 *      for integer I/O;
 *    Function mjmalloc to allocate bytes for minijava's new operator
 *
 *  Additional functions used by compiled code can be added as desired.
 */

#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>

#include "number_converter.h"

static char *executable_name;

/*
 * This is the main function in compiled code
 * Change the function name here if your
 * compiled MiniJava main entry point has a different label.
 */
extern void asm_main(void);

/*
 * Prompt for input, then return
 * next integer from standard input.
 * (This is not used in MiniJava.)
 */
int64_t get(void)
{
  int64_t k;
  printf("get: ");
  scanf("%" PRId64, &k);
  return k;
}

/*
 * Write an int64_t x to standard output.
 * This emulates what java's System.out.println does when given an integer,
 * so we can diff the output of the known-good expect run
 * with the actual output of the mini java compiler.
 */
void put(int64_t x)
{
  printf("%" PRId64 "\n", x);
}

void put_double(double d)
{
  char buf[BUFSIZ];
  convert_double(d, buf, sizeof(buf));
  printf("%s\n", buf);
}

/*
 * The function mjmalloc returns a pointer to a chunk of memory
 * at least num_bytes large.
 * Returns NULL if there is insufficient memory available.
 */
int64_t *mjmalloc(size_t num_bytes)
{
  return (int64_t *)calloc(num_bytes, sizeof(char));
}

/*
 * Execute the compiled mini Java program asm_main.
 */
int main(int argc, char **argv)
{
  executable_name = argv[0];
  asm_main();
  return 0;
}

void print_statement_counts(int64_t *array, int64_t length)
{
  int i;
  int filename_length;
  char *filename;
  char *extension = "profile";

  // Add the extension .profile to the executable filename for our output file.
  filename_length = strlen(executable_name) + 1 + strlen(extension);
  filename = (char *) malloc(filename_length + 1);
  if (!filename)
    exit(20);
  snprintf(filename, filename_length + 1, "%s.%s", executable_name, extension);

  // open file
  FILE *file;
  file = fopen(filename, "w");

  for (i = 0; i < length; ++i) {
    fprintf(file, "%lld\n", array[i]);
  }

  fclose(file);
}
