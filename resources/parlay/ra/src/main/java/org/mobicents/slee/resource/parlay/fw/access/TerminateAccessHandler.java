package org.mobicents.slee.resource.parlay.fw.access;

import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;


/**
 * This class represents a gateway initiated terminateAccess message.
 */
public final class TerminateAccessHandler implements Runnable {
    
    private final TSMBean tsmBean;
    
    private final String terminationText;
    
    private final String signingAlgorithm;
    
    private final byte[] digitalSignature;
    

    public TerminateAccessHandler(TSMBean tsmBean, String terminationText, String signingAlgorithm, byte[] digitalSignature) {
        this.tsmBean = tsmBean;
        this.terminationText = terminationText;
        this.signingAlgorithm =  signingAlgorithm;
        this.digitalSignature = digitalSignature;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        tsmBean.fireTerminateAccess(new TerminateAccessEvent(terminationText, signingAlgorithm, digitalSignature));
        
    }

}