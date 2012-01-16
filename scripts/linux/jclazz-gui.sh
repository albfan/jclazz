#!/bin/sh
export LIB_PATH=../../bin
export JAVA="java"

if [ "$JAVA_HOME" != "" ]
then
    export JAVA="$JAVA_HOME/bin/java"
fi

$JAVA -jar $LIB_PATH/jclazz-gui.jar