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

import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class Cause extends RequestedInformationValue {
    
    public final static int NORMAL = 0x90;
    private int cause;
    
    /** Creates a new instance of Cause */
    public Cause(int cause) {
        this.cause = cause;
    }

    public Cause(byte[] bin) throws IOException {
        try {
            cause = bin[1] & 0xff;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public int getCause() {
        return cause;
    }
    
    public byte[] toByteArray() {
        byte[] buff = new byte[4];
        buff[0] = (byte)0x9E;
        buff[1] = (byte)0x02;
        buff[2] = (byte)0x80;
        buff[3] = (byte)cause;
        return buff;
    }
    
    public String toString() {
        switch (cause) {
            case NORMAL : return "normal";
            default : return "unknown";
        }
    }
}
