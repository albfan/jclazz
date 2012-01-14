@ECHO OFF

set LIB_PATH=../../bin

IF "%JAVA_HOME%"=="" GOTO NOJH

    set JPATH="%JAVA_HOME%\bin\"
    goto EX

:NOJH

    set JPATH=""
    goto EX

:EX

    %JPATH%javaw -jar %LIB_PATH%/jclazz-gui.jar