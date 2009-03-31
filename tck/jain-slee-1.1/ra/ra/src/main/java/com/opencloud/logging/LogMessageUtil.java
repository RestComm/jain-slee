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
import com.opencloud.util.SimpleDateFormat;

/**
 * Logable that displays log messages to stderr
 */

public final class LogMessageUtil
{
    /**
     * Write a log message
     */
    public static final String makeLogMessage( int level, String message )
    {
        return makeLogMessage( level, message, true, true );
    }

    /** write a header to the log message being constructed in the string writer */
    public static final void writeLogMessageHeader( StringWriter sw, int level, boolean showTime, boolean showLevel ) {
        writeLogMessageHeader( sw, System.currentTimeMillis(), level, showTime, showLevel );
    }

    /** write a header to the log message being constructed in the string writer */
    public static final void writeLogMessageHeader( StringWriter sw, long time, int level, boolean showTime, boolean showLevel ) {
        if( showTime )  { sw.write( SimpleDateFormat.toString( time ) ); sw.write( COL_SEPERATOR ); }
        if( showLevel ) { sw.write( LogLevelUtil.toString( level ) ); sw.write( COL_SEPERATOR ); }
    }

    /** write a message to the log message being constructed in the string writer */
    public static final void writeLogMessage( StringWriter sw, String message ) {
        sw.write( message );
    }

    /** write a throwable to the log message being constructed in the string writer */
    public static final void writeLogMessage( StringWriter sw, Throwable t ) {
        sw.write( (t instanceof Error) ? "Error    : " : "Exception: " );
        sw.write( t.getClass().getName() );
        sw.write( " => " );
        sw.write( t.getMessage() );
        sw.write( "\nStack Trace:\n " );
        final PrintWriter pw = new PrintWriter( sw );
        t.printStackTrace( pw );
    }

    /** add a sub header to the log message being constructed in the string writer */
    public static final void writeLogMessageSubHeader( StringWriter sw, String id ) {
        sw.write( '[' );
        sw.write( id );
        sw.write( ']' );
        sw.write( COL_SEPERATOR );
    }

    /** add a thread header to the log message being constructed in the string writer */
    public static final void writeLogMessageThreadHeader( StringWriter sw, Thread t ) {
        sw.write( '<' );
        sw.write( t.getName() );
        sw.write( '>' );
        sw.write( COL_SEPERATOR );
    }

    private static final String COL_SEPERATOR = "  ";

    /**
     * Write a log message
     */
    public static final String makeLogMessage( int level, String message, boolean showTime, boolean showLevel )
    {
        StringBuffer _sb = new StringBuffer( 256 );
        if (showTime) {
            _sb=SimpleDateFormat.toStringBuffer(System.currentTimeMillis());
            _sb.append(' ');

        }

        if (showLevel) {
            switch(level) {
            case LogLevel.CRITICAL:
                _sb.append( "CRITICAL " );
                break;
            case LogLevel.ERROR:
                _sb.append( "ERROR    " );
                break;
            case LogLevel.WARNING:
                _sb.append( "WARNING  " );
                break;
            case LogLevel.NORMAL:
                _sb.append( "NORMAL   " );
                break;
            case LogLevel.INFO:
                _sb.append( "INFO     " );
                break;
            case LogLevel.DEBUG:
                _sb.append( "<DEBUG>  " );
                break;
            case LogLevel.SEVERE:
                _sb.append( "SEVERE   " );
                break;
            case LogLevel.CONFIG:
                _sb.append( "CONFIG   " );
                break;
            case LogLevel.FINE:
                _sb.append( "FINE     " );
                break;
            case LogLevel.FINER:
                _sb.append( "FINER    " );
                break;
            case LogLevel.FINEST:
                _sb.append( "FINEST   " );
                break;
            default:
                _sb.append( "** UNKNOWN LOGLEVEL ** : " );
            }
        }

        _sb.append( message );
        return _sb.toString();
    }

    /**
     * Write a throwable (and stack trace) to the log
     */
    public final static String makeLogMessage( int level, Throwable t )
    {
        return makeLogMessage( level, t, true, true );
    }

    /**
     * Write a throwable (and stack trace) to the log
     */
    public static String makeLogMessage( int level, Throwable t, boolean showTime, boolean showLevel )
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw );
        t.printStackTrace( pw );//send the printstream to
        StringBuffer _sb = new StringBuffer( 256 );
        _sb.append( (t instanceof Error) ? "Error    : " : "Exception: " )
            .append( t.getClass().getName() )
            .append( " => " )
            .append( t.getMessage() )
            .append( "\nStack Trace:\n " )
            .append( sw.toString() );
        return makeLogMessage( level, _sb.toString(), showTime, showLevel );
    }


}
