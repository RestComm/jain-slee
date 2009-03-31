/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.logging;

public interface Logable {
    /** Write a message to a log */
    public void writeToLog( int level, String message );

    public void debug( String message );
    public void info( String message );
    public void normal( String message );
    public void warning( String message );
    public void error( String message );
    public void critical( String message );

    public void severe( String message );
    public void config( String message );
    public void fine( String message );
    public void finer( String message );
    public void finest( String message );

    /** Write a throwable to a log */
    public void writeToLog( int level, Throwable t );

    public void debug( Throwable t );
    public void info( Throwable t );
    public void normal( Throwable t );
    public void warning( Throwable t );
    public void error( Throwable t );
    public void critical( Throwable t );

    public void severe( Throwable t );
    public void config( Throwable t );
    public void fine( Throwable t );
    public void finer( Throwable t );
    public void finest( Throwable t );

    /** change the ignore/filter level */
    public void setIgnoreLevel(int level);

    /**
     * Determine if a message logged at a particular level will actually be
     * output to the log, ie. the specified level is greater than or equal to
     * the ignore level.
     * @param level the level to check.
     * @return <code>true</code> if the specified level is greater than or equal
     *        to the log's ignore level, <code>false</code> otherwise.
     */
    public boolean isLogable(int level);
}

