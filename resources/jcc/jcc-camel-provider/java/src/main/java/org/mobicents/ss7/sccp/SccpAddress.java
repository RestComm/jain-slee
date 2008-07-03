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

package org.mobicents.ss7.sccp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



/**
 *
 * @author Oleg Kulikov
 */
public class SccpAddress {
    
    protected int pointCodeIndicator;
    protected int ssnIndicator;
    protected int globalTitleIndicator;
    protected int routingIndicator;
    
    protected int signalingPointCode;
    protected int ssn;
    protected GlobalTitle globalTitle;
    
    /** Creates a new instance of UnitDataMandatoryVariablePart */
    public SccpAddress() {
    }
    
    public SccpAddress(int pointCodeIndicator,
            int ssnIndicator, int gtIndicator, int routingIndicator,
            int signalingPointCode, int ssn, GlobalTitle globalTitle) {
        
        this.pointCodeIndicator = pointCodeIndicator;
        this.ssnIndicator = ssnIndicator;
        this.globalTitleIndicator = gtIndicator;
        this.routingIndicator = routingIndicator;
        this.signalingPointCode = signalingPointCode;
        this.ssn = ssn;
        this.globalTitle = globalTitle;
    }
    
    
    public void decode(byte[] buffer) throws IOException {
        DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(buffer));
        
        int i = in.readUnsignedByte();
        
        pointCodeIndicator = i & 0x01;
        ssnIndicator = (i & 0x02) >> 1;
        globalTitleIndicator = (i & 0x3c) >> 2;
        routingIndicator = (i & 0x40) >> 6;
        
        if (pointCodeIndicator == 1) {
            int b1 = in.readUnsignedByte();
            int b2 = in.readUnsignedByte();
            
            signalingPointCode = ((b2 & 0x3f) << 8) | b1;
        }
        
        if (ssnIndicator == 1) {
            ssn = in.readUnsignedByte();
        }
        
        switch (globalTitleIndicator) {
            case 4 :
                globalTitle = new GT0100();
                break;
        }
        
        globalTitle.decode(in);
    }
    
    public byte[] encode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        byte b = (byte)(pointCodeIndicator | ssnIndicator << 1 |
                globalTitleIndicator << 2 | routingIndicator << 6);
        out.write(b);
        
        if (pointCodeIndicator == 1) {
            byte b1 = (byte) signalingPointCode;
            byte b2 = (byte) ((signalingPointCode >> 8) & 0x3f);
            
            out.write(b1);
            out.write(b2);
        }
        
        if (ssnIndicator == 1) {
            out.write((byte) ssn);
        }
        
        globalTitle.encode(out);
        return out.toByteArray();
    }
    
    
    public String toString() {
        StringBuffer msg = new StringBuffer();
        
        if (pointCodeIndicator == 1) {
            msg.append("Address contains a signaling point code\n");
        } else {
            msg.append("Address contains no signaling point code\n");
        }
        
        if (ssnIndicator == 1) {
            msg.append("Address contains a subsystem number\n");
        } else {
            msg.append("Address contains no subsystem number\n");
        }
        
        msg.append(globalTitle.toString());
        return msg.toString();
    }
    

}
