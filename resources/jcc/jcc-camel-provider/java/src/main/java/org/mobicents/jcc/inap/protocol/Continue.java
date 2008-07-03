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

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Oleg Kulikov
 */
public class Continue extends Operation {
    
    /** Creates a new instance of Continue */
    public Continue() {
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(0x02);
        bout.write(0x01);
        bout.write(0x1F);
        return bout.toByteArray();
    }
    
}
