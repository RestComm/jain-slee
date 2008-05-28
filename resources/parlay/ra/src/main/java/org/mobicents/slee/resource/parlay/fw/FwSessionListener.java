package org.mobicents.slee.resource.parlay.fw;

import java.util.EventListener;

import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;
import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;

/**
 * This is a listener interface which must be implemented by an application
 * which uses the
 * {@link org.mobicents.slee.resource.parlay.fw.FwSession FwSession}.
 * 
 * When the application object implementing this is registered as a listener it
 * can receive gateway originated events.
 *  
 */
public interface FwSessionListener extends EventListener {

    /**
     * Called due to a gateway initiated access termination.
     * 
     * @param event
     */
    void terminateAccess(TerminateAccessEvent event);

    /**
     * Called due to a gateway initiated service agreement termination.
     * 
     * @param event
     */
    void terminateServiceAgreement(TerminateServiceAgreementEvent event);
}