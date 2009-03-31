/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Logable implementation which logs to a PrintWriter.
 */
public class PrintWriterLog extends SimpleLog {

    /**
     * Initialise the logable
     */
    public PrintWriterLog(PrintWriter writer) {
        this( writer, LogLevel.INFO, true, true );
    }

    /**
     * Initialise the logable
     */
    public PrintWriterLog(PrintWriter writer, int logLevel, boolean showTime, boolean showLevel) {
        super( logLevel, showTime, showLevel );
        this.writer = writer;
    }

    /**
     * Write a log message
     */
    public void persistLogMessage( StringWriter logMessage ) {
        synchronized( clientLock ) {
            writer.write( logMessage.toString() + '\n');
            writer.flush();
        }
    }

    public String toString() { return "PrintWriterLog"; }

    private PrintWriter writer;
    private final Object clientLock = new Object();

}
