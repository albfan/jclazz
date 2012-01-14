@ECHO OFF

set LIB_PATH=../../bin

IF "%JAVA_HOME%"=="" GOTO NOJH

    set JPATH="%JAVA_HOME%\bin\"
    goto EX

:NOJH

    set JPATH=""
    goto EX

:EX

    %JPATH%java -classpath %LIB_PATH%/jclazz-core.jar;%LIB_PATH%/jclazz-decomp.jar ru.andrew.jclazz.decompiler.ClassDecompiler %*