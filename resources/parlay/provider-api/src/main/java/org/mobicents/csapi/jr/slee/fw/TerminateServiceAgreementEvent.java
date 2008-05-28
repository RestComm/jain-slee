package org.mobicents.csapi.jr.slee.fw;



/**
 * This class represents a gateway initiated terminateServiceAgreement() message.
 */
public final class TerminateServiceAgreementEvent {

    private String terminationText;
    private String serviceToken;

    public TerminateServiceAgreementEvent(String serviceToken, String terminationText) {

        this.terminationText = terminationText;

        this.serviceToken = serviceToken;
    }
    
    
    /**
     * @return the reason for the termination of the service agreement.
     */
    public String getTerminationText() {
        return terminationText;
    }

    /**
     * @return the token identifying the terminated service.
     */
    public String getServiceToken() {
        return serviceToken;
    }

}
