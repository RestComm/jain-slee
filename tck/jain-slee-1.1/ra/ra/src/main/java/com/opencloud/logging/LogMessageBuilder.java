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
 * Log implementations implement this interface to utilise log message generation
 * protocol defined by SimpleLog
 */

interface LogMessageBuilder extends Logable {
    /** generate a log message as a StringWriter that contains the log message header */
    public void buildLogMessageHeader( StringWriter logMessage, int logLevel );
    /** persist the log message in the string writer */
    public void persistLogMessage( StringWriter logMessage );
}
