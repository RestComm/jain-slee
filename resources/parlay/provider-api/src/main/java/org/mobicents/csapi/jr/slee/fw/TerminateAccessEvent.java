
package org.mobicents.csapi.jr.slee.fw;

import java.util.Arrays;


/**
 *
 **/
public class TerminateAccessEvent {

    private String terminationText = null;
    
    private final String signingAlgorithm;
    
    private final byte[] digitalSignature;
    
    /**
     * @param terminationText
     */
    /**
     * @param terminationText
     * @param signingAlgorithm
     * @param digitalSignature
     */
    public TerminateAccessEvent(String terminationText, String signingAlgorithm, byte[] digitalSignature) {
        this.terminationText = terminationText;
        this.signingAlgorithm = signingAlgorithm;
        this.digitalSignature = digitalSignature;
    }

    /**
     * @return Returns the terminationText.
     */
    public String getTerminationText() {
        return terminationText;
    }
    
    /**
     * @return Returns the digitalSignature.
     */
    public byte[] getDigitalSignature() {
        return digitalSignature;
    }
    /**
     * @return Returns the signingAlgorithm.
     */
    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }
    
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        
        if(!(o instanceof TerminateAccessEvent)) {
            return false;
        }
        
        TerminateAccessEvent event = (TerminateAccessEvent)o;
        
        if (!event.getTerminationText().equals(terminationText)) {
            return false;
        }
        
        if (!event.getSigningAlgorithm().equals(signingAlgorithm)) {
            return false;
        }
        
        if (!Arrays.equals(event.getDigitalSignature(), digitalSignature)) {
            return false;
        }
        
        return true;
    }

}
