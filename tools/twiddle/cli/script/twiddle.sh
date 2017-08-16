#!/bin/sh
### ====================================================================== ###
##  Twiddle Standalone Script                                               ##
### ====================================================================== ###
# $Id: twiddle.sh 2012-04-14 Arnold Johansson #
#
# Worklog:
#
# 2012-02-14 Arnold Johansson
# Using components (JARs, scripts, etc) from JBoss AS 6.1.0.Final 
# Creating a base directory as TWIDDLER_HOME - twiddler-standalone.
# All dependent JARs put in a common lib directory.
# Script (this) is modified and put in bin directory
# Tested on OSX 10.7.3 with JavaSE 1.6.0_31 against JBoss AS 7.1.1.Final (localhost:1090)
#
# 2013-11-17 Arnold Johansson
# Enable the remoting-jmx protocol by adapting the TWIDDLE_CLASSPATH to utilise JBoss AS7 / WildFly modules.
#

JBOSS_MODULEPATH=$JBOSS_HOME/modules/system/layers/base/
MODULES="org/jboss/remoting-jmx org/jboss/logging org/jboss/xnio org/jboss/xnio/nio org/jboss/sasl org/jboss/marshalling org/jboss/marshalling/river org/jboss/as/cli org/jboss/staxmapper org/jboss/as/protocol org/jboss/dmr org/jboss/as/controller-client org/jboss/threads org/jboss/as/controller org/telestax/slee/container/lib"
for MODULE in $MODULES
do
    for JAR in `cd "$JBOSS_MODULEPATH/$MODULE/main/" && ls -1 *.jar`
    do
        TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$JBOSS_MODULEPATH/$MODULE/main/$JAR"
    done
done

# Extract the directory and the program name
# takes care of symlinks
PRG="$0"
DIR=`dirname "$PRG"`
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done
DIRNAME=`pwd`
PROGNAME=`basename "$PRG"`
GREP="grep"

#
# Helper to complain.
#
die() {
    echo "${PROGNAME}: $*"
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
	[ -n "$JBOSS_HOME" ] &&
        JBOSS_HOME=`cygpath --unix "$JBOSS_HOME"`
    [ -n "$TWIDDLE_HOME" ] &&
        TWIDDLE_HOME=`cygpath --unix "$TWIDDLE_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

# Setup the JVM
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

# Setup the classpath
TWIDDLE_HOME=$JSLEE_HOME/tools/twiddle
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/twiddle.jar"
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jbossall-client.jar"
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jboss-jmx.jar"
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/cli-twiddle.jar"
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib"
TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$JBOSS_HOME/bin/client/jboss-client.jar"

SLEE_TWIDDLE_CONF="$TWIDDLE_HOME/lib/slee-twiddle.properties"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    TWIDDLE_HOME=`cygpath --path --windows "$TWIDDLE_HOME"`
    TWIDDLE_CLASSPATH=`cygpath --path --windows "$TWIDDLE_CLASSPATH"`
    JBOSS_CLASSPATH=`cygpath --path --windows "$JBOSS_CLASSPATH"`
	SLEE_TWIDDLE_CONF=`cygpath --path --windows "$SLEE_TWIDDLE_CONF"`
fi

exec "$JAVA" \
    $JAVA_OPTS \
    -Dprogram.name="$PROGNAME" \
    -classpath $TWIDDLE_CLASSPATH \
    org.jboss.console.twiddle.Twiddle -c file:///$SLEE_TWIDDLE_CONF $@