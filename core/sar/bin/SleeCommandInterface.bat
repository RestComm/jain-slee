@echo off
rem -------------------------------------------------------------------------
rem JAIN SLEE Command Line Script for Win32
rem -------------------------------------------------------------------------


@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
set PROGNAME=run.bat
if "%OS%" == "Windows_NT" set PROGNAME=%~nx0%

rem Read all command line arguments

REM
REM The %ARGS% env variable commented out in favor of using %* to include
REM all args in java command line. See bug #840239. [jpl]
REM
REM set ARGS=
REM :loop
REM if [%1] == [] goto endloop
REM        set ARGS=%ARGS% %1
REM        shift
REM        goto loop
REM :endloop

if not "%JAVA_HOME%" == "" goto ADD_TOOLS

set JAVA=java

echo JAVA_HOME is not set.  Unexpected results may occur.
echo Set JAVA_HOME to the directory of your local JDK to avoid this message.
goto SKIP_TOOLS

:ADD_TOOLS

set JAVA=%JAVA_HOME%\bin\java

if exist "%JAVA_HOME%\lib\tools.jar" goto SKIP_TOOLS
echo Could not locate %JAVA_HOME%\lib\tools.jar. Unexpected results may occur.
echo Make sure that JAVA_HOME points to a JDK and not a JRE.

:SKIP_TOOLS

set JBOSS_HOME=%DIRNAME%\..

set JBOSS_CLASSPATH=%CLASSPATH%
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\client\jmx-client.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\client\jbossall-client.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\client\getopt.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\client\log4j.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\lib\jboss-jmx.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\lib\xml-apis.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\lib\xercesImpl.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jboss.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jnpserver.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\lib\jboss-common.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jboss-transaction.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jboss-transaction.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jboss-j2ee.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jbossha.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\sleeRA.zip
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jmx-adaptor-plugin.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\jta.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\lib\javassist.jar


rem Add mobicents class and jain slee jar
set SLEE_CLASSPATH=%JBOSS_HOME%\server\all\deploy\mobicents.sar;%SLEE_CLASSPATH%
rem set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\deploy\mobicents.sar\slee.jar
set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%JBOSS_HOME%\server\all\deploy\mobicents.sar\slee_1_1.jar

%JAVA% -classpath %JBOSS_CLASSPATH%;.;%SLEE_CLASSPATH% -Djava.security.policy=java.policy  -Djava.naming.factory.initial=org.jnp.interfaces.NamingContextFactory  -Djava.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces org.mobicents.slee.container.management.jmx.SleeCommandInterface %*

:END
