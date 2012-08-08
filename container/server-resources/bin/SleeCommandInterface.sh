#!/bin/ksh
### ===================================================
##  SLEE Command Interface Script
### ===================================================

DIRNAME=`dirname $0`
PROGNAME=`basename $0`
GREP="grep"


# Setup the JVM
if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA="java"
    fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
#
#
#if $cygwin ; then
#    [ -n "$JBOSS_HOME" ] &&
#        JBOSS_HOME=`cygpath --unix "$JBOSS_HOME"`
#    [ -n "$JAVA_HOME" ] &&
#        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
#    [ -n "$JAVAC_JAR" ] &&
#        JAVAC_JAR=`cygpath --unix "$JAVAC_JAR"`
#fi

# Setup JBOSS_HOME
if [ "x$JBOSS_HOME" = "x" ]; then
    # get the full path (without any relative bits)
    JBOSS_HOME=`cd $DIRNAME/..; pwd`
fi
export JBOSS_HOME


JBOSS_CLASSPATH="$CLASSPATH"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/client/jmx-client.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/client/jbossall-client.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/client/getopt.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/client/log4j.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/lib/jboss-jmx.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/lib/xml-apis.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/lib/xercesImpl.jar"


#Add mobicents class and jain slee jar
export SLEE_CLASSPATH=$JBOSS_HOME/server/all/deploy/mobicents.sar:$SLEE_CLASSPATH
#JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/server/all/deploy/mobicents.sar/slee.jar"
JBOSS_CLASSPATH="$JBOSS_CLASSPATH:$JBOSS_HOME/server/all/deploy/mobicents.sar/slee_1_1.jar"

exec $JAVA \
   -classpath "$JBOSS_CLASSPATH:.:$SLEE_CLASSPATH" \
   -Djava.security.policy=java.policy \
   -Djava.naming.factory.initial=org.jnp.interfaces.NamingContextFactory \
   -Djava.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces \
   org.mobicents.slee.container.management.jmx.SleeCommandInterface "$@"

