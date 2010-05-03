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
public class ApplyCharging extends Operation {
    
	public static final int _TAG = 16;
	public static final int _TAG_CLASS = 0x00;
	public static final boolean _IS_PRIMITIVE = false;
	
	
    private boolean release = true;
    private int sideID;
    private int legID;
    private int duration;
    
    /** Creates a new instance of ApplyCharging */
    public ApplyCharging(int sideID, int legID) {
        this.sideID = sideID;
        this.legID = legID;
    }
        
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
//        //Local operation code
//        bout.write(0x02); //TAG: class=0 code=2 type=0
//        bout.write(0x01); //Lenth................1
//        bout.write(APPLY_CHARGING);
        
        //Apply charging
        //bout.write(0x30); //TAG: class=0 code=16 type=1
        //bout.write(0x12); //Length...................18
        
        //Ach billing charging characteristics
        bout.write(0x80);   //AG: class=2 code=0 type=0
        bout.write(0x0B);   //Length....................11
        
        //Time duration charging
        bout.write(0xA0);       //TAG: class=2 code=0 type=1
        bout.write(0x09);       //Length....................9
        
        //Max call period duration
        bout.write(0x80);       //TAG: class=2 code=0 type=0
        bout.write(0x02);       //Length..................2
        bout.write(0x46);
        bout.write(0x50);       //value = 600
        
        //Release if duration exceeded
        bout.write(0xA1);       //TAG: class=2 code=1 type=1
        bout.write(0x03);   //Length.......................3
        
        //Tone
        bout.write(0x01); //TAG: class=0 code=1 type=0
        bout.write(0x01);  //Length...................1
        if (release) {
            bout.write(0x01);
        } else {
            bout.write(0x00);
        }
        
        //PartyToCharge
        bout.write(0xA2);  //TAG: class=2 code=2 type=1
        bout.write(0x03);  //Length...................3;
        
        //Sending side ID
/*        bout.write(0x80); //TAG: class=2 code=0 type=0
        bout.write(0x01); //Length......................1
        bout.write(partyToCharge);
*/
                //Tag side ID,len
        bout.write(sideID);
        bout.write(0x01);
        bout.write(legID);

        return bout.toByteArray();
    }
    
}


