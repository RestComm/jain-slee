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

/**
 *
 * @author Oleg Kulikov
 */
public class BCSMEvent implements Serializable {
    
    public final static int COLLECTED_INFO = 2;
    public final static int ROUTE_SELECT_FAILURE = 4;
    public final static int O_CALLED_PARTY_BUSY = 5;
    public final static int O_NO_ANSWER = 6;
    public final static int O_ANSWER = 7;
    public final static int O_DISCONNECT = 9;
    public final static int O_ABANDON = 10;
    public final static int TERM_ATTEMPT_AUTHORIZED = 12;
    public final static int T_BUSY = 13;
    public final static int T_NO_ANSWER = 14;
    public final static int T_ANSWER = 15;
    public final static int T_DISCONNECT = 17;
    public final static int T_ABANDON = 18;
    
    private int code;
    private LegID legID;
    
    /** Creates a new instance of BCSMEvent */
    public BCSMEvent(int code) {
        this.code = code;
    }

    public BCSMEvent(int code, LegID legID) {
        this.code = code;
        this.legID = legID;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        int len = legID == null ? 6 : 11;
        //BCSM event
        bout.write(0x30);
        //bout.write(0x06);
        bout.write(len);
        
        //Event type BCSM
        bout.write(0x80);
        bout.write(0x01);
        bout.write(code);
        
        //monitor mode
        bout.write(0x81);
        bout.write(0x01);
        bout.write(0x01);
        
        if (legID != null) {
            bout.write(0xA2);
            bout.write(0x03);
            try {
                bout.write(legID.toByteArray());
            } catch (IOException e) {
            }
        }
        return bout.toByteArray();
    }
    
    public String toString() {
        String s = null;
        switch (code) {
            case COLLECTED_INFO :
                s = "cllected_info";
                break;                
            case ROUTE_SELECT_FAILURE :
                s = "route_select_failure";
                break;                
            case O_CALLED_PARTY_BUSY :
                s = "o_called_party_busy";
                break;                
            case O_NO_ANSWER :
                s = "o_No_Answer";
                break;                
            case O_ANSWER :
                s = "o_Answer";
                break;                
            case O_DISCONNECT :
                s = "o_Disconnect";
                break;
            case O_ABANDON :
                s = "o_Abandon";
                break;
            case TERM_ATTEMPT_AUTHORIZED :
                s = "t_Term_Authorized";
                break;
            case T_BUSY :
                s = "t_Busy";
                break;
            case T_NO_ANSWER :
                s = "t_No_Answer";
                break;                
            case T_ANSWER :
                s = "t_Answer";
                break;
            case T_DISCONNECT :
                s = "t_Disconnect";
                break;
            case T_ABANDON :
                s = "t_Abandon";
                break;
            default :
                s = "unknown";
        }
        
        return s + "[monitor_mode=notify]";
    }
}
