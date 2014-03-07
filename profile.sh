#!/bin/bash
ant build -Dfile=$1;
./$1.x;
ant printprofile -Dfile=$1;
