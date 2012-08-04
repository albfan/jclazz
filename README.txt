jclazz 1.2.2 Release
--------------------

Release Date: 30 April, 2009

This is the release of jclazz version 1.2.2.

1. About

jclazz is a crossplatform powerful Java bytecode viewer and decompiler.
It supports latest Java versions (from 1.4 till 1.6).
It can be used both as command line tool and user application with Swing interface.
    * InfoJ can be used to generate information about Java class.
      The output includes all possible data that can be extracted from class file: 
          fields, methods, attributes, access flags, signatures, debug information, opcodes etc.
    * Decompiler can be used to reproduce Java source code from compiled Java class file. 
      It uses debug information to produce Java code which is nearly the same as original source file. 
      Nevertheless, there are several restrictions and Java code constructions that prevent decompiler
      from producing the same code as original and even correct Java code. You can find out more about
      these cases below on this page.
    * jclazz-GUI is user-friendly interface for quick start and easy to use.
      
2. Changes

Changes made comparing to previous version see inputStream Changelog file.

3. Requirements

a. Download jclazz binary
b. Download and install JRE version 1.4 or greater (if you don't already have), try this
c. Set up JAVA_HOME environment variable or appropriate PATH environment variable, where java
   executable can be found (optional, if you want to use scripts for launching jclazz)

4. Invokation

a. jclazz-GUI can be launched by double clicking on jclazz-gui.jar file (if jar files
   are associated with java inputStream your system)
b. use .sh (on Linux) or .cmd (on Windows) scripts to launch InfoJ, Decompiler or jclazz-GUI
c. on Windows you can use native executable 'jclazz.exe' to launch jclazz-GUI
d. execute Java classes as usual. Main classes for InfoJ and decompiler are 
   ru.andrew.jclazz.core.infoj.InfoJ and ru.andrew.jclazz.decompiler.ClassDecompiler

4. Reporting bugs

After you successfully launched jclazz it should work without any errors. However this can
happen sometimes. Even if everything works fine you may notice that decompiled code is incorrect
or have ideas how to make jclazz more convenient and stable, send us report with your suggestions
or description of problem at jclazz.sf@gmail.com.
In case of finding bugs (e.g. unexpected Exception is thrown) you can report the following
information needed for resolving bug:

    * Description
    * jclazz package version
    * JRE version used to run utils
    * Compiled Java class
    * Output files produced by utility (optional)
    * Expected output (optional)

5. Thanks

Thanks to Janel sourceforge project for providing windows executable for running jclazz GUI:
    http://sourceforge.net/projects/janel/   
