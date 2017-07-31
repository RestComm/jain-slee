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

# JBoss AS7
#JBOSS_MODULEPATH=$JBOSS_HOME/modules
#MODULES="org/jboss/remoting3/remoting-jmx org/jboss/remoting3 org/jboss/logging org/jboss/xnio org/jboss/xnio/nio org/jboss/sasl org/jboss/marshalling org/jboss/marshalling/river org/jboss/as/cli org/jboss/staxmapper org/jboss/as/protocol org/jboss/dmr org/jboss/as/controller-client org/jboss/threads org/jboss/as/controller"
#TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$JBOSS_HOME/bin/client/jboss-client.jar"

# JBoss AS72
JBOSS_MODULEPATH=$JBOSS_HOME/modules/system/layers/base/
MODULES="org/jboss/remoting-jmx org/jboss/remoting3 org/jboss/logging org/jboss/xnio org/jboss/xnio/nio org/jboss/sasl org/jboss/marshalling org/jboss/marshalling/river org/jboss/as/cli org/jboss/staxmapper org/jboss/as/protocol org/jboss/dmr org/jboss/as/controller-client org/jboss/threads org/jboss/as/controller org/telestax/slee/container/lib"
# WildFly
#JBOSS_MODULEPATH=$JBOSS_HOME/modules/system/layers/base/
#MODULES="org/jboss/remoting-jmx org/jboss/remoting org/jboss/logging org/jboss/xnio org/jboss/xnio/nio org/jboss/sasl org/jboss/marshalling org/jboss/marshalling/river org/jboss/as/cli org/jboss/staxmapper org/jboss/as/protocol org/jboss/dmr org/jboss/as/controller-client org/jboss/threads org/wildfly/security/manager org/telestax/slee/container/lib" 

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

# Setup TWIDDLE_HOME
if [ "x$TWIDDLE_HOME" = "x" ]; then
    #TWIDDLE_HOME=`cd $DIRNAME/..; pwd`
    TWIDDLE_HOME=$DIRNAME
fi
export TWIDDLE_HOME

# Setup the JVM
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

#debug
#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"

# Setup the classpath

TWIDDLE_BOOT_CLASSPATH="$TWIDDLE_HOME/lib/twiddle.jar"

#if [ "x$TWIDDLE_CLASSPATH" = "x" ]; then
#    TWIDDLE_CLASSPATH="$TWIDDLE_BOOT_CLASSPATH"
    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_BOOT_CLASSPATH"
    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jbossall-client.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jboss-logging.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/log4j.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/dom4j.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/getopt.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jboss-common-core.jar"    
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jbosssx-client.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jboss-jmx.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jnp-client.jar"
    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/cli-twiddle.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jain-slee-1.1.jar"
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib/jmx-property-editors.jar"
    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_HOME/lib"
#else
#    TWIDDLE_CLASSPATH="$TWIDDLE_CLASSPATH:$TWIDDLE_BOOT_CLASSPATH"
#fi

#if [ "x$JBOSS_CLASSPATH" = "x" ]; then

 	#jboss
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/twiddle.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/getopt.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/log4j.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-jmx.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-common-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jmx-invoker-adaptor-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jmx-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jnp-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-serialization.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-minimal.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/javaee-api.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-security-spi.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-transaction-spi.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/concurrent.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/dom4j.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jbossx-security-client.jar"
    
    #restcomm
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/cli-twiddle.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jain-slee-1.1.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jmx-property-editors.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/activities.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/spi.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib"
   
#fi

SLEE_TWIDDLE_CONF="$DIRNAME/lib/slee-twiddle.properties"
#SLEE_TWIDDLE_CONF="slee-twiddle.properties"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    TWIDDLE_HOME=`cygpath --path --windows "$TWIDDLE_HOME"`
    TWIDDLE_CLASSPATH=`cygpath --path --windows "$TWIDDLE_CLASSPATH"`
    JBOSS_CLASSPATH=`cygpath --path --windows "$JBOSS_CLASSPATH"`
	SLEE_TWIDDLE_CONF=`cygpath --path --windows "$SLEE_TWIDDLE_CONF"`
fi

# Display our environment
#     echo "========================================================================="
#     echo ""
#     echo "  CLI Bootstrap Environment"
#     echo ""
#     echo "  DIRNAME  : $DIRNAME"
#     echo ""
#     echo "  JAVA     : $JAVA"
#     echo ""
#     echo "  JAVA_OPTS: $JAVA_OPTS"
#     echo ""
#     echo "  JBOSS_CLASSPATH: $JBOSS_CLASSPATH"
#     echo ""
#     echo "  TWIDDLE_HOME: $TWIDDLE_HOME"
#     echo ""
#     echo "  TWIDDLE_CLASSPATH: $TWIDDLE_CLASSPATH"
#     echo ""
#     echo "  OPTS     : $*"
#     echo ""
#     echo "  CONF     : $SLEE_TWIDDLE_CONF"
#     echo ""
#     echo "========================================================================="
#     echo ""

# Execute the JVM
#exec "$JAVA" \
#    $JAVA_OPTS \
#    -Dprogram.name="$PROGNAME" \
#    -classpath $JBOSS_CLASSPATH \
#    org.jboss.console.twiddle.Twiddle -c file:///$SLEE_TWIDDLE_CONF $@

echo "classpash: $TWIDDLE_CLASSPATH"
    
exec "$JAVA" \
    $JAVA_OPTS \
    -Dprogram.name="$PROGNAME" \
    -classpath $TWIDDLE_CLASSPATH \
    org.jboss.console.twiddle.Twiddle -c file:///$SLEE_TWIDDLE_CONF $@
