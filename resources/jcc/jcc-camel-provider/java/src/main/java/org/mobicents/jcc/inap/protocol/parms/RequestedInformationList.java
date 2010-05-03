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

package org.mobicents.jcc.inap.protocol.parms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import org.mobicents.jcc.inap.protocol.tcap.Util;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestedInformationList extends Vector {
    
    
    /** Creates a new instance of RequestedInformationList */
    public RequestedInformationList() {
        super();
    }
    
    public RequestedInformationList(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        while (in.available() > 0) {
            int tag = in.read(); //requested information
            int len = Util.readLen(in); //length
            
            byte[] buff = new byte[len];
            in.read(buff);
            
            add(new RequestedInformation(buff));
        }
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Enumeration elements = this.elements();
        while (elements.hasMoreElements()) {
            RequestedInformationValue value = (RequestedInformationValue) elements.nextElement();
            try {
                bout.write(value.toByteArray());
            } catch (IOException e) {
            }
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(0xA0);
        bout.write(buff.length);
        try {
            bout.write(buff);
        } catch (IOException e) {
        }
        
        return bout.toByteArray();
    }
    
    public String toString() {
        if (this.size()== 0) {
            return "[]";
        } else {
            StringBuffer s = new StringBuffer();
            s.append("[");
            s.append(get(0));
            
            for (int i = 1; i < size(); i++) {
                s.append("," + get(i));
            }
            
            s.append("]");
            return s.toString();
        }
    }
}
