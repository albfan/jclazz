#!/bin/sh
export LIB_PATH=../../bin
export JAVA="java"

if [ "$JAVA_HOME" != "" ]
then
    export JAVA="$JAVA_HOME/bin/java"
fi

$JAVA -classpath $LIB_PATH/jclazz-core.jar ru.andrew.jclazz.core.infoj.InfoJ $@