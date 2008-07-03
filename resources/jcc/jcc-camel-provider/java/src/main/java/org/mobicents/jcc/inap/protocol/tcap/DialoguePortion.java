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

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class DialoguePortion implements Serializable {
    private static byte[] dialoguePortion = new byte[] {
        (byte) 0x6B, (byte) 0x2A, 0x28, 0x28, 0x06,
        0x07, 0x00, 0x11, (byte)0x86, 0x05, 0x01, 0x01, 0x01, (byte)0xA0,
        0x1D, 0x61, 0x1B, (byte)0x80, 0x02, 0x07, (byte)0x80, (byte)0xA1,
        0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x32, 0x01, (byte)0xA2,
        0x03, 0x02, 0x01, 0x00, (byte)0xA3, 0x05, (byte)0xA1, 0x03, 0x02, 0x01, 0x00
    };
    
    /** Creates a new instance of DialoguePortion */
    public DialoguePortion() {
    }
    
    public DialoguePortion(byte[] bin) throws IOException {
    }
    
    public byte[] toByteArray() {
        return dialoguePortion;
    }
}
