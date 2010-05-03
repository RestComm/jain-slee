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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class LegID implements Serializable {
    public final static int RECEIVING_SIDE_ID = 0x81;
    public final static int SENDING_SIDE_ID = 0x80;
    
    public final static int FIRST_LEG = 1;
    public final static int SECOND_LEG = 2;
    
    private int legID;
    private int sideID;
    
    /** Creates a new instance of LegID */
    public LegID(int sideID, int legID) {
        this.sideID = sideID;
        this.legID = legID;
    }
    
    public LegID(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        sideID = in.read() & 0xff;
        in.read();
        legID = in.read() & 0xff;
    }
    
    public int getSideID() {
        return sideID;
    }
    
    public int getLegID() {
        return legID;
    }
        
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        //Tag legID, len
        //bout.write(0xA3);
        //bout.write(0x03);
        
        //Tag side ID,len
        bout.write(sideID);
        bout.write(0x01);
        bout.write(legID);
        
        return bout.toByteArray();
    }
    
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("LegID[");
        if (sideID == SENDING_SIDE_ID) {
            s.append("Sending Side ID:");
        } else {
            s.append("Receiving Side ID:");
        }
        
        if (legID == FIRST_LEG) {
            s.append("First leg");
        } else {
            s.append("Second Leg");
        }
        
        s.append("]");
        return s.toString();
    }
}
