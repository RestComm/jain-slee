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
import java.io.OutputStream;

/**
 *
 * @author Oleg Kulikov
 */
public class TCContinue extends TCMessage {
    
    /** Creates a new instance of TCContinue */
    public TCContinue(long txID) {
        super(txID);
    }
    
    public TCContinue(byte[] bin) throws IOException {
        this.type = TCMessage.CONTINUE;
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
        
        //reading destination txID
        tag = data.read();
        length = data.read();
        
        if (tag != 0x49) throw new IOException("Destination TID should be present");
        data.skip(length);
        
        tag = data.read() & 0xff;
        length = Util.readLen(data);
        
        byte[] buff = new byte[length];
        data.read(buff);
        
        //decode dialogue portion
        boolean isDialoguePresent = tag == 0x6B;
        
        if (isDialoguePresent) {
            dialogue = new DialoguePortion();
        }
        
        if (isDialoguePresent && data.available() > 0) {
            tag = data.read() & 0xff;
            length = Util.readLen(data);
            buff = new byte[length];
            data.read(buff);
        }
        
        if (tag != 0x6C) {
            throw new IOException("Component portion expected");
        }        
        components = new Components(buff);
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        try {
            Util.encodeTransactioID(0x48, txID, bout);
            Util.encodeTransactioID(0x49, txID, bout);
        } catch (IOException e) {
        }
        
        if (dialogue != null) {
            try {
                bout.write(dialogue.toByteArray());
            } catch (IOException e) {
            }
        }
        
        if (components != null) {
            try {
                bout.write(components.toByteArray());
            } catch (IOException e) {
            }
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(0x65);
        try {
            Util.encodeLength(buff.length, bout);
            bout.write(buff);
        } catch (IOException e) {
        }
        
        return bout.toByteArray();
    }
    
    public String toString() {
        return "CONTINUE (tx=" + this.getTxID() + "," + components + ")";
    }
    
}
