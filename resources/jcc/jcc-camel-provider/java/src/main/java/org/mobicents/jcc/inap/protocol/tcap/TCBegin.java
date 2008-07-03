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
import java.io.IOException;
import org.mobicents.util.Utils;

/**
 *
 * @author Oleg Kulikov
 */
public class TCBegin extends TCMessage {
    
    /** Creates a new instance of TCBegin */
    public TCBegin(long txID) {
        super(txID);
        this.type = TCMessage.BEGIN;
    }
    
    public TCBegin(byte[] bin) throws IOException {
        this.type = TCMessage.BEGIN;
        ByteArrayInputStream data = new ByteArrayInputStream(bin);
        
        //reading type of message
        //type = data.read() & 0xff;
        //int length = Util.readLen(data);
        
        //reading orination txID
        int tag = data.read();
        int length = data.read();

        if (tag != 0x48) throw new IOException("Originated TID should be present");
        //read tx value
        for (int i = 0; i < length; i++) {
            txID = (txID << 8) | data.read();
        }
        
        tag = data.read() & 0xff;
        length = Util.readLen(data);
        
        //decode dialogue portion
        if (tag != 0x6B) {
            throw new IOException("Dialogue portion expected");
        }
        
        byte[] buff = new byte[length];
        data.read(buff);
        dialogue = new DialoguePortion(buff);
        
        //decode component portion
        tag = data.read() & 0xff;
        length = Util.readLen(data);
        
        if (tag != 0x6C) {
            throw new IOException("Component portion expected");
        }
        
        if (length == 0) {
            length = 126;
        }
        
        buff = new byte[length];
        data.read(buff);
        components = new Components(buff);
    }
    
    public String toString() {
        return "BEGIN (tx=" + this.getTxID() + "," + components + ")";
    }
}
