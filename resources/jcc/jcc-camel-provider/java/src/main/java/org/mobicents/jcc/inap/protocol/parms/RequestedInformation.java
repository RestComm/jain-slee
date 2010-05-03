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

/**
 *
 * @author Oleg Kulikov
 */
public class RequestedInformation implements Serializable {
    
    private int type;
    private RequestedInformationValue value;
    
    /** Creates a new instance of RequestedInformation */
    public RequestedInformation(int type, RequestedInformationValue value) {
        this.type = type;
        this.value = value;
    }
    
    public RequestedInformation(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        in.read();
        in.read();
        
        type = in.read();
        
        in.read();
        int len = in.read();
        
        byte[] buff = new byte[len];
        in.read(buff);
        
        switch (type) {
            case RequestedInformationType.CALL_ATTEMPT_ELAPSED_TIME :
                break;
            case RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME :
                value = new CallConnectedElapsedTime(buff);
                break;
            case RequestedInformationType.CALL_STOP_TIME :
                value = new DateTime(buff);
                break;
            case RequestedInformationType.RELEASE_CAUSE :
                value = new Cause(buff);
        }
    }
    
    public int getType() {
        return type;
    }
    
    public RequestedInformationValue getValue() {
        return value;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        //encode type
        bout.write(0x80);
        bout.write(0x01);
        bout.write(type);
        
        //encode value
        try {
            bout.write(value.toByteArray());
        } catch (IOException e) {
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(0x30);
        bout.write(buff.length);
        
        try {
            bout.write(buff);
        } catch (IOException e) {
        }
        
        return bout.toByteArray();
    }
    
    public String toString() {
        String s = "";
        switch (type) {
            case RequestedInformationType.CALL_ATTEMPT_ELAPSED_TIME :
                break;
            case RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME :
                s = "duration";
                break;
            case RequestedInformationType.CALL_STOP_TIME :
                s = "call time";
                break;
            case RequestedInformationType.RELEASE_CAUSE :
                s = "release cause";
        }
        
        return s + ":" + value;
    }
}
