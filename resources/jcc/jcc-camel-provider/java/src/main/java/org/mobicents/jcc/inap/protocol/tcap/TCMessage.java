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
import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class TCMessage implements Serializable {
    public final static int BEGIN = 0x62;
    public final static int CONTINUE = 0x65;
    public final static int ABORT = 0x67;
    public final static int END = 0x64;
    
    protected long txID;
    protected int type;
    
    protected Components components;
    protected DialoguePortion dialogue;
    
    public TCMessage() {
    }
    
    /** Creates a new instance of Message */
    public TCMessage(long txID) {
        this.txID = txID;
    }
        
    public int getType() {
        return type;
    }
    
    public long getTxID() {
        return txID;
    }
    
    public DialoguePortion getDialogue() {
        return dialogue;
    }
    
    public void setDialogue(DialoguePortion dialogue) {
        this.dialogue = dialogue;
    }
    
    public Components getComponents() {
        return components;
    }
    
    public void setComponents(Components components) {
        this.components = components;
    }
    
    private int readLen(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        if ((b & 0x80) != 0x80) {
            return b;
        } else {
            int count = b & 0x7f;
            int length = 0;
            
            for (int i = 0; i < count; i++) {
                length <<= 8;
                length |= (in.read() & 0xff);
            }
            return length;
        }
    }
    
    public byte[] toByteArray() {
        return null;
    }
}
