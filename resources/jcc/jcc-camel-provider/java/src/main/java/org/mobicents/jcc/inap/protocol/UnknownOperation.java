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

package org.mobicents.jcc.inap.protocol;

import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class UnknownOperation extends Operation {
    
    private byte[] buff;
    
    /** Creates a new instance of UnknownOperation */
    public UnknownOperation() {
    }

    public UnknownOperation(int code, byte[] bin) throws IOException {
        super(code);
        this.buff = bin;
    }
    
    public byte[] toByteArray() {
        return buff;
    }
    
    public String toString() {
        return "Unknown operation:" + getCode();
    }
}
