#!/bin/sh

LIB="$(pwd)/$(dirname $0)/../../bin"

java -classpath $LIB/jclazz-core.jar ru.andrew.jclazz.core.infoj.InfoJ $@