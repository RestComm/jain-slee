/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.logging;

import com.opencloud.logging.LogLevel;

/**
 * Utilities for log levels
 */

public final class LogLevelUtil implements LogLevel
{
    /**
     * Translate a string into a log level
     */
    public static final int toLogLevel( String _s )
    {
        for( int c=DEBUG; c <= FINEST; c++ )
        {
            if( _asString[ c ].length() > 0 && _asString[ c ].regionMatches( true, 0, _s, 0, _s.length() ) )
                return c;
        }
        return UNKNOWN_LOG_LEVEL;
    }

    /**
     * Translate a log level to a string representation
     */
    public static final String toString( int level )
    {
        return _asString[ isValid( level ) ? level : UNKNOWN_LOG_LEVEL ];
    }

    public static final boolean isValid( int level )
    {
        return (level >= DEBUG && level <= CRITICAL)
            || (level >= SEVERE && level <= FINEST);
    }

    /**
     * Determine if log messages generated at level <code>level</code> would be logged 
     * if the corresponding log's ignore level was set to <code>ignoreLevel</code>.
     * This method takes into consideration the differences between old-style and Java 1.4
     * style log levels and attempts to convert between the two.
     * @return <code>true</code> if log messages generated at level <code>level</code>
     *        would be logged if the log's ignore level was set to <code>ignoreLevel</code>.
     */
    public static boolean isLogable(int level, int ignoreLevel) {
        if (level < 10 && ignoreLevel < 10) {
            // use old rules
            return level >= ignoreLevel;
        }
        else if (level > 10 && ignoreLevel > 10) {
            // use new rules only
            return level <= ignoreLevel;
        }
        else if (level < 10 && ignoreLevel > 10) {
            // convert to new rules
            switch (level) {
                case LogLevel.DEBUG: level = LogLevel.FINE; break;
                case LogLevel.INFO: level = LogLevel._INFO; break;
                case LogLevel.NORMAL: level = LogLevel._INFO; break;
                case LogLevel.WARNING: level = LogLevel._WARNING; break;
                case LogLevel.ERROR: level = LogLevel._WARNING; break;
                case LogLevel.CRITICAL: level = LogLevel.SEVERE; break;
            }
            return level <= ignoreLevel;
        }
        else {
            // convert to new rules
            int tmp = ignoreLevel;
            switch (tmp) {
                case LogLevel.DEBUG: tmp = LogLevel.FINEST; break;
                case LogLevel.INFO: tmp = LogLevel._INFO; break;
                case LogLevel.NORMAL: tmp = LogLevel._INFO; break;
                case LogLevel.WARNING: tmp = LogLevel._WARNING; break;
                case LogLevel.ERROR: tmp = LogLevel._WARNING; break;
                case LogLevel.CRITICAL: tmp = LogLevel.SEVERE; break;
            }
            return level <= tmp;
        }
    }

    /** translation table */
    private static final String [] _asString =
    {
        "DEBUG   ",   // 0
        "INFO    ",   // 1
        "NORMAL  ",   // 2
        "WARNING ",   // 3
        "ERROR   ",   // 4
        "CRITICAL",   // 5
        "UNKNOWN ",   // 6
        "",           // 7
        "",           // 8
        "",           // 9
        "",           // 10
        "SEVERE  ",   // 11
        "",           // 12
        "",           // 13
        "CONFIG  ",   // 14
        "FINE    ",   // 15
        "FINER   ",   // 16
        "FINEST  "    // 17
    };
}
