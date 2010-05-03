/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.jcc.inap.protocol.parms;

import java.io.Serializable;

/**
 * Nature Of Address indicator.
 * 
 * @author kulikov
 */
public class NoA implements Serializable {
    public final static int SPARE = 0;
    public final static int SUBSCRIBER = 1;
    public final static int UNKNOWN = 2;
    public final static int NATIONAL = 3;
    public final static int INTERNATIONAL = 4;
    public final static int NETWORK_SPECIFIC = 5;

    private final static String[] IND = new String[] {
        "SPARE", "SUBSCRIBER", "UNKNOWN", "NATIONAL", "INTERNATIONAL", "NETWORK_SPECIFIC"
    };
    
    public static synchronized int parse(String value) {
        String s = value.substring(value.indexOf("=") +  1);
        if (s.equals("subscriber")) {
            return SUBSCRIBER;
        } else if (s.equals("national")) {
            return NATIONAL;
        } else if (s.equals("international")) {
            return INTERNATIONAL;
        } else if (s.equals("unknown")) {
            return UNKNOWN;
        } else {
            return 0;
        }
    }
    
    public static String toString(int code) {
        return IND[code];
    }
    
}
