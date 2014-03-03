#
# A Makefile that knows how to compile and run the sample programs.
# This knows how to run the regular Java engine on a sample program.
# This knows how to run the Mini Java pipeline on a sample program.
# 
# To run the regular Java engine on a sample program "Pi" to produce known-good expected output:
#   make SamplePrograms/SampleMiniJavaPrograms/Pi.expect
#
# To run the    mini Java engine on a sample program "Pi" to produce presumed-good actual output:
#   make SamplePrograms/SampleMiniJavaPrograms/Pi.actual
# The assembly and executable output of the compiler pipeline to compile "Pi" end up in:
#       SamplePrograms/SampleMiniJavaPrograms/Pi.s
#       SamplePrograms/SampleMiniJavaPrograms/Pi.x
#
# To compare the output of regular Java and mini Java on "Pi", do:
#   make SamplePrograms/SampleMiniJavaPrograms/Pi.diff
#
# The differences are saved into the file .../Pi.diff, and also printed to stdout.
# If there are differences, the execution of make exits with a failure code.
#
# This Makefile is a built of a Frankenstein.
#
# It calls upon ant to compile the mini Java compiler,
# as ant is really good at figuring out how to run javac.
#
# Ant is terrible at wildcards and merely awful at C compilation;
# make is much better at this than ant is.
#
SAMPLEDIR = SamplePrograms/SampleMiniJavaPrograms

SAMPLEPROGS = \
  BinarySearch \
  BinaryTree \
  Factorial \
  LinearSearch \
  LinkedList \
  QuickSort \
  BubbleSort \
  Pi \
  ExerciseDouble \
  Sqrt \
  Asin \
  $(NULL)

HARDSAMPLEPROGS = \
  TreeVisitor \
  DoubleOutput \
  $(NULL)

CLASSPATH=$(SAMPLEDIR)/classes
CLASSPATH_T=$(SAMPLEDIR)/classes.T

.PHONY: diffs
diffs: $(SAMPLEPROGS:%=$(SAMPLEDIR)/%.diff)

.PHONY: expects
expects: $(SAMPLEPROGS:%=$(SAMPLEDIR)/%.expect)

$(CLASSPATH_T):
	mkdir -p $(CLASSPATH)
	touch $@

.PRECIOUS: $(SAMPLEDIR)/%.s
.PRECIOUS: $(SAMPLEDIR)/%.o
.PRECIOUS: $(SAMPLEDIR)/%.x
.PRECIOUS: $(SAMPLEDIR)/%.class
.PRECIOUS: $(SAMPLEDIR)/%.actual
.PRECIOUS: $(SAMPLEDIR)/%.expect
.PRECIOUS: $(SAMPLEDIR)/%.diff

#
# Run the standard Java system to produce known-good expected output.
#
$(SAMPLEDIR)/%.class: $(SAMPLEDIR)/%.java Makefile $(CLASSPATH_T)
	javac -d $(CLASSPATH) -classpath $(CLASSPATH) $<
$(SAMPLEDIR)/%.expect: $(SAMPLEDIR)/%.class $(CLASSPATH_T)
	java -classpath $(CLASSPATH) $(basename $(notdir $<)) > $@

#
# Run the minijava compiler to produce the actual output
# We use ant to compile the mini java compiler
#
$(SAMPLEDIR)/%.s: $(SAMPLEDIR)/%.java compileMJ.T Makefile
	java -classpath ./build/classes:./lib/CUP.jar CodeGenMain -o $@ -i $< < $<
$(SAMPLEDIR)/%.x: $(SAMPLEDIR)/%.s boot.c Makefile
	gcc -Wall -fno-pic -m64 -g $< boot.c number_converter.o -o $@ -lm
$(SAMPLEDIR)/%.actual: $(SAMPLEDIR)/%.x
	$< > $@
number_converter.o: number_converter.c
	gcc -c -Wall -fno-pic -m64 -g $< -o $@ -lm

compileMJ.T: src/*.java src/*/*.java build.xml Makefile
	ant compile
	touch $@

$(SAMPLEDIR)/%.diff: $(SAMPLEDIR)/%.expect $(SAMPLEDIR)/%.actual
	diff $(SAMPLEDIR)/$*.expect $(SAMPLEDIR)/$*.actual | tee $@

.PHONY: clean
clean:
	-rm *.T
	-rm -f $(SAMPLEDIR)/*.expect
	-rm -f $(SAMPLEDIR)/*.actual
	-rm -f $(SAMPLEDIR)/*.diff
	-rm -f $(SAMPLEDIR)/*.x
	-rm -f $(SAMPLEDIR)/*.s
	-rm -rf $(SAMPLEDIR)/*.dSYM
	-rm -rf $(SAMPLEDIR)/*.prof
	-rm -rf $(SAMPLEDIR)/*.mjprof
	-rm -rf $(CLASSPATH_T) $(CLASSPATH)
