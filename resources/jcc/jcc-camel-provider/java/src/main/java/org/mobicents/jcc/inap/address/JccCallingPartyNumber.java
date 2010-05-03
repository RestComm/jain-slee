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

package org.mobicents.jcc.inap.address;


import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;

import org.mobicents.jcc.inap.protocol.parms.APRI;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.GenericNumber;
import org.mobicents.jcc.inap.protocol.parms.NI;
import org.mobicents.jcc.inap.protocol.parms.NoA;
import org.mobicents.jcc.inap.protocol.parms.NumberingPlan;
import org.mobicents.jcc.inap.protocol.parms.Screening;

/**
 * JCC style address for calling party number.
 *
 * @author Oleg Kulikov
 */
public class JccCallingPartyNumber implements JccAddress {
    
    private JccProvider provider = null;
    private int type = JccAddress.E164_MOBILE;
    
    private CallingPartyNumber cpn;
    private String callerID;
    
    public JccCallingPartyNumber(JccProvider provider, CallingPartyNumber cpn) {
        this.provider = provider;
        this.cpn = cpn;
    }
    
    public String getName() {
        return cpn.getAddress() + ",NoA=" +NoA.toString(cpn.getNai());
    }
    
    public JccProvider getProvider() {
        return provider;
    }
    
    public int getType() {
        return type;
    }
    
    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }
    
    public CallingPartyNumber getNumber() {
        return cpn;
    }
    
    public GenericNumber getGenericNumber() {
        return callerID != null ? 
            new GenericNumber(
                GenericNumber.ADDITIONAL_CALLING_PARTY_NUMBER,
                NoA.NATIONAL, 
                NI.COMPLETE, 
                NumberingPlan.ISDN, 
                APRI.PRESENTATION_ALLOWED, 
                Screening.USER_PROVIDED_NOT_VERIFIED, 
                callerID)
        : null;
    }
    
    @Override
    public String toString() {
        return "E164_MOBILE:" + cpn.getAddress();
    }
}
