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
public class GenericNumber extends CallingPartyNumber {
    public final static int ADDITIONAL_CALLED_NUMBER = 1;
    public final static int ADDITIONAL_CONNECTED_NUMBER = 5;
    public final static int ADDITIONAL_CALLING_PARTY_NUMBER = 6;
    public final static int ADDITIONAL_ORIGINAL_CALLED_NUMBER = 7;
    public final static int ADDITIONAL_REDIRECTING_NUMBER = 8;
    public final static int ADDITIONAL_REDIRECTION_NUMBER = 9;
    
    private int nqi;
    
    /** Creates a new instance of GenericNumber */
    public GenericNumber(int nqi, int nai, int nii, int npi, int apri, int si, String address) {
        super(nai, nii, npi, apri, si, address);
        this.nqi = nqi;
    }
    
    public GenericNumber(int nqi, CallingPartyNumber cpn) {
        super(cpn.getNai(), cpn.getNii(), cpn.getNpi(), cpn.getApri(), 
                cpn.getSi(), cpn.getAddress());
        this.nqi = nqi;
    }
    
    public int getNqi() {
        return nqi;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(nqi);
        try {
            bout.write(super.toByteArray());
        } catch (IOException e) {
        }
        return bout.toByteArray();
    }
    
    public String toString() {
        return getAddress();
    }
}
