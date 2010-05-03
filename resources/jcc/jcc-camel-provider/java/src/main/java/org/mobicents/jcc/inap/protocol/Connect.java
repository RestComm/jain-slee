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
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.GenericNumber;

/**
 *
 * @author Oleg Kulikov
 */
public class Connect extends Operation {
    
	public static final int _TAG = 16;
	public static final int _TAG_CLASS = 0x00;
	public static final boolean _IS_PRIMITIVE = false;
	
    private CalledPartyNumber calledPartyNumber;
    private GenericNumber gn;
    private CallingPartyNumber callingPartyNumber;
    
    private Connect() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Connect(int code) {
		super(code);
		// TODO Auto-generated constructor stub
	}

	/** Creates a new instance of Connect */
    public Connect(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }
    
    public void setGenericNumber(GenericNumber gn) {
        this.gn = gn;
    }
    
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        try {
            byte[] cpn = calledPartyNumber.toByteArray();
            bout.write(0x04);
            bout.write(cpn.length);
            bout.write(cpn);
            
            byte[] buffer = bout.toByteArray();
            bout = new ByteArrayOutputStream();
            
            bout.write((byte)0xa0);
            bout.write(buffer.length);
            bout.write(buffer);
            
            if (gn != null) {
                byte[] gnb = this.encGn();
                bout.write(0xAe);
                bout.write(gnb.length);
                bout.write(gnb);
            }
            
            if (callingPartyNumber != null) {
                byte[] gnb = callingPartyNumber.toByteArray();
                bout.write(0x9b);
                bout.write(gnb.length);
                bout.write(gnb);
            }
            
           // buffer = bout.toByteArray();
            //bout = new ByteArrayOutputStream();
            
            //AAA, why all of them have 0x30.... cmon
            //bout.write(0x30);
            //bout.write(buffer.length);
            //bout.write(buffer);
            
            //buffer = bout.toByteArray();
           // bout = new ByteArrayOutputStream();
            
            //local operation
            //bout.write(0x02);
            //bout.write(0x01);
            //bout.write(0x14);
            //bout.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();
    }
    
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("CONNECT[");
        buff.append(calledPartyNumber.toString());
        if (gn != null) {
            buff.append("," + gn.toString());
        }
        buff.append("]");
        return buff.toString();
    }
    
    private byte[] encGn() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] hx = gn.toByteArray();
        bout.write(0x04);
        bout.write(hx.length);
        bout.write(hx);
        return bout.toByteArray();
    }
}
