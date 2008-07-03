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

import java.util.Properties;

import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;
import org.mobicents.jcc.inap.JccInapProviderImpl;

import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;

/**
 * JCC style address for Called Party Number.
 *
 * @author Oleg Kulikov
 */
public class JccCalledPartyNumber implements JccAddress {

    private JccInapProviderImpl provider = null;
    private int type = JccAddress.E164_MOBILE;
    
    private CalledPartyNumber localNumber;
    
    
    /** Creates a new instance of JccCallImpl */
    public JccCalledPartyNumber(JccInapProviderImpl provider, CalledPartyNumber cpn) {
        this.provider = provider;
        localNumber = provider.initialTranslation(cpn);
    }    

    public JccCalledPartyNumber(JccInapProviderImpl provider, String address) {
        this.provider = provider;
        localNumber = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1, address);
    }    
        
    public void setName(String digits) {
        localNumber.setAddress(digits);
    }
    
    public String getName() {
        return localNumber.getAddress();
    }

    public JccProvider getProvider() {
        return provider;
    }

    public int getType() {
        return type;
    }
    
    public CalledPartyNumber getRouteAddress() {
        return provider.finalTranslation(localNumber);
    }
        
    public String toString() {
        return "E164_MOBILE:" + getName();
    }
}
