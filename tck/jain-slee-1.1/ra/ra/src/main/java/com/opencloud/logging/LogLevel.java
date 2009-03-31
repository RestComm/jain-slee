/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.logging;

public interface LogLevel{

    public final static int DEBUG = 0;
    public final static int INFO = 1;
    public final static int NORMAL = 2;
    public final static int WARNING = 3;
    public final static int ERROR = 4;
    public final static int CRITICAL = 5;
    public final static int UNKNOWN_LOG_LEVEL = 6;

    public static final int SEVERE = 11;
    static final int _WARNING = 12;
    static final int _INFO = 13;
    public static final int CONFIG = 14;
    public static final int FINE = 15;
    public static final int FINER = 16;
    public static final int FINEST = 17;

    /**
     * Used for 'conditional' compilation
     */
    public final static boolean __DEBUG__ = false;
}
