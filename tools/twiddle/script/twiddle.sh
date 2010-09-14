#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  JBoss Shutdown Script                                                   ##
##                                                                          ##
### ====================================================================== ###

### $Id: twiddle.sh 88145 2009-05-04 12:24:46Z ispringer $ ###

DIRNAME=`pwd`
PROGNAME=`basename $0`
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
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi


# Setup the JVM
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

#debug
#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"

# Setup the classpath


#if [ "x$CLASSPATH" = "x" ]; then
 	

 	#jboss
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/twiddle.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/getopt.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/log4j.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-jmx.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-common-client.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jmx-invoker-adaptor-client.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jmx-client.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jnp-client.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-serialization.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-minimal.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/javaee-api.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-security-spi.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jboss-transaction-spi.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/concurrent.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/dom4j.jar"
    
    #mobicents
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/cli-twiddle.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jain-slee-1.1.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/jmx-property-editors.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/activities.jar"
    CLASSPATH="$CLASSPATH:${DIRNAME}/lib/spi.jar"
    CLASSPATH="$CLASSPATH:$DIRNAME/lib"
   
#fi

SLEE_TWIDDLE_CONF="$DIRNAME/lib/slee-twiddle.properties"
#SLEE_TWIDDLE_CONF="slee-twiddle.properties"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
	SLEE_TWIDDLE_CONF=`cygpath --path --windows "$SLEE_TWIDDLE_CONF"`
fi
 # Display our environment
 #     echo "========================================================================="
 #     echo ""
 #     echo "  CLI Bootstrap Environment"
 #     echo ""
 #     echo "  DIR      : $DIRNAME"
 #     echo ""
 #     echo "  JAVA     : $JAVA"
 #     echo ""
 #     echo "  JAVA_OPTS: $JAVA_OPTS"
 #     echo ""
 #     echo "  CLASSPATH: $CLASSPATH"
 #     echo ""
 #     echo "  OPTS     : $*"
 #     echo ""
 #     echo "  CONF     : $SLEE_TWIDDLE_CONF"
 #     echo ""
 #     echo "========================================================================="
 #     echo ""

# Execute the JVM
exec "$JAVA" \
    $JAVA_OPTS \
    -Dprogram.name="$PROGNAME" \
    -classpath $CLASSPATH \
    org.jboss.console.twiddle.Twiddle -c file:///$SLEE_TWIDDLE_CONF $@ 
	#
