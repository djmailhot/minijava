#!/bin/bash

# remove the .java extension, if present.
file=`echo $1 | sed s/\.java$//`;

ant build -Dfile=$file;
./$file.x;
ant printprofile -Dfile=$file;
