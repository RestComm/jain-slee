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
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Oleg Kulikov
 */
public class Components extends Vector {
    
    /** Creates a new instance of Components */
    public Components() {
        super();
    }
    
    public Components(byte[] bin) throws IOException {
        super();        
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        
        while (in.available() > 0) {
            int code = in.read() & 0xff;
            int len = Util.readLen(in);
            if (code == 0) {
                return;
            }
            if (len <= 0) throw new IOException("Packet corrupted");
            
            byte[] buffer = new byte[len];
            in.read(buffer);

            switch (code) {
                case 0xA1 :
                    Invoke invoke = new Invoke(buffer);
                    add(invoke);
                    break;
                default :
                    System.out.println("Unknown tag: " + code);
                    
            }
        }
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Enumeration list = this.elements();
        while (list.hasMoreElements()) {
            Component component = (Component) list.nextElement();
            try {
                bout.write(component.toByteArray());
            } catch (IOException e) {
            }
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(0x6C);
        try {
            Util.encodeLength(buff.length, bout);
            bout.write(buff);
        } catch (IOException e) {
        }
        return bout.toByteArray();
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        
        int count = size();
        for (int i = 0; i < count; i++) {
            buffer.append(get(i));
        }
        
        buffer.append("}");
        return buffer.toString();
    }
}
