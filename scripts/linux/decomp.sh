#!/bin/sh

LIB="$(pwd)/$(dirname $0)/../../bin"

java -classpath $LIB/jclazz-core.jar:$LIB/jclazz-decomp.jar ru.andrew.jclazz.decompiler.ClassDecompiler $@
