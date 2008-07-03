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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 *
 * @author Oleg Kulikov
 */
public class ProtocolClass extends MandatoryFixedParameter {
    
    private int pClass;
    private int msgHandling;
    
    /** Creates a new instance of UnitDataMandatotyFixedPart */
    public ProtocolClass() {
    }

    public ProtocolClass(int pClass, int msgHandling) {
        this.pClass = pClass;
        this.msgHandling = msgHandling;
    }
    
    public int getProtocolClass() {
        return pClass;
    }
    
    public void decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        
        pClass = b & 0x0f;
        msgHandling = b & 0xf0;
    }

    public void encode(OutputStream out) throws IOException {
        byte b = (byte)(pClass | (msgHandling << 4));
        out.write(b);
    }
    
}
