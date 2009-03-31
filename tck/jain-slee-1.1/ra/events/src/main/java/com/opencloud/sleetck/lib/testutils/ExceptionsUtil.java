/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.testutils;

import java.io.StringWriter;
import java.io.PrintWriter;

public class ExceptionsUtil {

    /**
     * Returns a throwable and its stack trace as a String.
     */
    public static String formatThrowable(Throwable t) {
        String msg=null;
        if(t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter( sw );
            t.printStackTrace( pw );//send the printstream to the writer
            StringBuffer _sb = new StringBuffer( 256 );
            _sb.append( (t instanceof Error) ? "Error    : " : "Exception: " )
                .append( t.getClass().getName() )
                .append( " => " )
                .append( t.getMessage() )
                .append( "\nStack Trace:\n " )
                .append( sw.toString() );
            msg=_sb.toString();
        } return msg;
    }

}