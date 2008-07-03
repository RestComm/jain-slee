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

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;

import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.GenericNumber;

/**
 * JCC style address for calling party number.
 *
 * @author Oleg Kulikov
 */
public class JccCallingPartyNumber implements JccAddress {
    
    private JccProvider provider = null;
    private int type = JccAddress.E164_MOBILE;
    
    private CallingPartyNumber cpn;
    private String cli;
    
    public JccCallingPartyNumber(JccProvider provider, CallingPartyNumber cpn) {
        this.provider = provider;
        this.cpn = cpn;
    }
    
    public String getName() {
        return cpn.getAddress();
    }
    
    public JccProvider getProvider() {
        return provider;
    }
    
    public int getType() {
        return type;
    }
    
    public void setCLI(String cli) {
        this.cli = cli;
    }
    
    public CallingPartyNumber getNumber() {
        return cpn;
    }
    
    public GenericNumber getGenericNumber() {
        return new GenericNumber(GenericNumber.ADDITIONAL_CALLING_PARTY_NUMBER,
                3, 0, 
                1, 0, 
                0, cli);
    }
    
    public String toString() {
        return getName();
    }
}
