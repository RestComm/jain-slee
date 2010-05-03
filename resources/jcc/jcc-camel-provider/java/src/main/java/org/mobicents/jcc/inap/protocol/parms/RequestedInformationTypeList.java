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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestedInformationTypeList implements Serializable {
    
    private Vector list = new Vector();
    
    /** Creates a new instance of RequestInformationTypeList */
    public RequestedInformationTypeList() {
    }
    
    public void add(int type) {
        list.add(new Integer(type));
    }
    
    public void remove(int type) {
        list.remove(new Integer(type));
    }
    
    public int size() {
        return list.size();
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Enumeration elements = list.elements();
        while (elements.hasMoreElements()) {
            Integer type = (Integer) elements.nextElement();
            bout.write(0x0A);
            bout.write(0x01);
            bout.write(type.intValue());
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
}
