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

package org.mobicents.jcc.inap.protocol.tcap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class TCAbort extends TCMessage {
    
    /** Creates a new instance of TCAbort */
    public TCAbort(long txID) {
        super(txID);
    }
 
    public TCAbort(byte[] bin) throws IOException {
        type = TCMessage.ABORT;
        ByteArrayInputStream data = new ByteArrayInputStream(bin);
        
        
        //reading destination txID
        int tag = data.read();
        int length = data.read();
        
        if (tag != 0x49) throw new IOException("Originated TID should be present");
        //read tx value
        for (int i = 0; i < length; i++) {
            txID = (txID << 8) | data.read();
        }
        
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        try {
            Util.encodeTransactioID(0x49, txID, bout);
        } catch (IOException e) {
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(TCMessage.ABORT);
        try {
            Util.encodeLength(buff.length, bout);
            bout.write(buff);
        } catch (IOException e) {
        }
        
        return bout.toByteArray();
    }
    
    public String toString() {
        return "ABORT (tx=" + this.getTxID() + ")";
    }
    
}
