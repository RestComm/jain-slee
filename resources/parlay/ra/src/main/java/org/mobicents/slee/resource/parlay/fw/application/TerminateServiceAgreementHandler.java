package org.mobicents.slee.resource.parlay.fw.application;

import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;



/**
 * This class represents a gateway initiated terminateServiceAgreement() message.
 */
public final class TerminateServiceAgreementHandler implements Runnable {

    private final SABean saBean;
    private final String terminationText;
    private final String serviceToken;

    public TerminateServiceAgreementHandler(SABean saBean, String serviceToken, String terminationText) {
        this.saBean = saBean;
        
        this.terminationText = terminationText;

        this.serviceToken = serviceToken;
    }
    

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        
        saBean.fireTerminateServiceAgreement(new TerminateServiceAgreementEvent(serviceToken, terminationText));
        
    }
}
