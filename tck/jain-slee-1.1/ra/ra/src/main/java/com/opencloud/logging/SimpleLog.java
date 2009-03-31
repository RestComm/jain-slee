/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.logging;

import java.io.StringWriter;

/**
 * simple logs represent a single log to the client
 */

abstract class SimpleLog extends AbstractLog implements LogMessageBuilder
{
    SimpleLog() { super( LogLevel.INFO ); }
    SimpleLog( int ignoreLevel ) { super( ignoreLevel, true, true ); }
    SimpleLog( int ignoreLevel, boolean showTime, boolean showLevel ) { super( ignoreLevel, showTime, showLevel ); }

    public final void writeToLog( int logLevel, String message ) {
        if (isLogable(logLevel)) {
            synchronized( writeToLogLock ) {
                logMessage.getBuffer().delete( 0, logMessage.getBuffer().length() );
                buildLogMessageHeader( logMessage, logLevel );
                LogMessageUtil.writeLogMessage( logMessage, message );
                persistLogMessage( logMessage );
            }
        }
    }

    public final void writeToLog( int logLevel, Throwable t ) {
        if (isLogable(logLevel)) {
            synchronized( writeToLogLock ) {
                logMessage.getBuffer().delete( 0, logMessage.getBuffer().length() );
                buildLogMessageHeader( logMessage, logLevel );
                LogMessageUtil.writeLogMessage( logMessage, t );
                persistLogMessage( logMessage );
            }
        }
    }

    public void buildLogMessageHeader( StringWriter logMessage, int logLevel ) {
        LogMessageUtil.writeLogMessageHeader( logMessage, logLevel, shouldShowTime(), shouldShowLevel() );
    }

    public void persistLogMessage( StringWriter logMessage ) {}

    private final StringWriter logMessage = new StringWriter( 256 ); // where I build log messages
    private volatile Object writeToLogLock = new Object();           // used to order writes
}

