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
import org.mobicents.jcc.inap.JccInapProviderImpl;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.INN;
import org.mobicents.jcc.inap.protocol.parms.NoA;

/**
 * JCC style address for CallingPartyBCDNumber.
 *
 * @author Oleg Kulikov
 */
public class JccCalledPartyBCDNumber implements JccAddress {
    
    private JccInapProviderImpl provider = null;
    private int addressType = JccAddress.E164_MOBILE;
    
    private CalledPartyBcdNumber number;
    private CalledPartyNumber localNumber;
    
    public JccCalledPartyBCDNumber(JccInapProviderImpl provider, CalledPartyBcdNumber number) {
        this.provider = provider;
        this.number = number;
        localNumber = new CalledPartyNumber(
                number.getNi(), 
                INN.ROUTING_ALLOWED,
                number.getNp(),
                number.getAddress());
    }
    
    public String getName() {
        return localNumber.getAddress() + ",NoA=" + NoA.toString(localNumber.getNai());
    }
    
    public void setName(String digits) {
        String[] tokens = digits.split(",");
        localNumber.setAddress(tokens[0]);
        
        for (int i = 1; i < tokens.length; i++) {
            String s = tokens[i].toLowerCase();
            if (s.startsWith("noa")) {
                int ind = NoA.parse(s);
                localNumber.setNai(ind);
            }
        }
    }
    
    public JccProvider getProvider() {
        return provider;
    }
    
    public int getType() {
        return addressType;
    }
    
    public CalledPartyNumber getRouteNumber() {
        return localNumber;
    }
    
    @Override
    public String toString() {
        return "E164_MOBILE:" + localNumber.getAddress();
    }
}
