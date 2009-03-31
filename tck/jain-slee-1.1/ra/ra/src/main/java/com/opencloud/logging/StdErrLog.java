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
 * Logable that displays log messages to stderr
 */

public final class StdErrLog extends SimpleLog
{
    /**
     * Initialise the logable
     */
    public StdErrLog()
    {
        this( LogLevel.INFO, true, true );
    }

    /**
     * Initialise the logable
     */
    public StdErrLog( int logLevel, boolean showTime, boolean showLevel )
    {
        super( logLevel, showTime, showLevel );
    }

    /**
     * Write a log message
     */
    public final void persistLogMessage( StringWriter logMessage )
    {
        //we write it to stderr
        System.err.println( logMessage.toString() );
        System.err.flush();
    }

    public String toString() { return "StdErrLog"; }
}

