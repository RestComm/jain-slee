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
 * base implementation of all log types
 */

abstract class AbstractLog implements Logable, LogLevel
{
    AbstractLog() {
        this( LogLevel.INFO );
    }

    AbstractLog( int ignoreLevel ) {
        this( ignoreLevel, true, true );
    }

    AbstractLog( int ignoreLevel, boolean showTime, boolean showLevel ) {
        this.ignoreLevel = LogLevelUtil.isValid(ignoreLevel) ? ignoreLevel : INFO;
        this.showTime = showTime;
        this.showLevel = showLevel;
    }

    /** Write a message to the log */

    public final void debug( String message )    { writeToLog( DEBUG, message ); }
    public final void info( String message )     { writeToLog( INFO, message ); }
    public final void normal( String message )   { writeToLog( NORMAL, message ); }
    public final void warning( String message )  { writeToLog( WARNING, message ); }
    public final void error( String message )    { writeToLog( ERROR, message ); }
    public final void critical( String message ) { writeToLog( CRITICAL, message ); }

    /** Write a throwable to the log */

    public final void debug( Throwable t )    { writeToLog( DEBUG, t ); }
    public final void info( Throwable t )     { writeToLog( INFO, t ); }
    public final void normal( Throwable t )   { writeToLog( NORMAL, t ); }
    public final void warning( Throwable t )  { writeToLog( WARNING, t ); }
    public final void error( Throwable t )    { writeToLog( ERROR, t ); }
    public final void critical( Throwable t ) { writeToLog( CRITICAL, t ); }

    public void severe(String message) { writeToLog(SEVERE, message); }
    public void config(String message) { writeToLog(CONFIG, message); }
    public void fine(String message) { writeToLog(FINE, message); }
    public void finer(String message) { writeToLog(FINER, message); }
    public void finest(String message) { writeToLog(FINEST, message); }

    public void severe(Throwable t) { writeToLog(SEVERE, t); }
    public void config(Throwable t) { writeToLog(CONFIG, t); }
    public void fine(Throwable t) { writeToLog(FINE, t); }
    public void finer(Throwable t) { writeToLog(FINER, t); }
    public void finest(Throwable t) { writeToLog(FINEST, t); }


    /**
     * Alter the ignore level
     */
    public final synchronized void setIgnoreLevel(int level)
    {
        ignoreLevel=level;
    }

    protected final int getIgnoreLevel()
    {
        return ignoreLevel;
    }

    /**
     * Determine if a message logged at a particular level will actually be
     * output to the log, ie. the specified level is greater than or equal to
     * the ignore level.
     * @param level the level to check.
     * @return <code>true</code> if the specified level is greater than or equal
     *        to the log's ignore level, <code>false</code> otherwise.
     */
    public synchronized final boolean isLogable(int level) {
        return LogLevelUtil.isLogable(level, ignoreLevel);
    }


    protected final boolean shouldShowTime() { return showTime; }
    protected final boolean shouldShowLevel() { return showLevel; }

    /** The current ignore level. Only messages with a loglevel greater tham this are processed */
    private int ignoreLevel = LogLevel.INFO;
    private final boolean showTime;  // whether time is shown per log message
    private final boolean showLevel; // wether the log level is shown per log message
}
