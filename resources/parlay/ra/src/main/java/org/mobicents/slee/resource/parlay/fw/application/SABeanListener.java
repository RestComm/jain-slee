package org.mobicents.slee.resource.parlay.fw.application;
import java.util.EventListener;

import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;


/**
 * This is a listener interface which must be implemented by an application
 * which uses the 
 * {@link org.mobicents.slee.resource.parlay.fw.application.SABean SABean}.
 *
 * When the applciation object implementing this is registered as a listener it can
 * receive gateway originated events.
 */
public interface SABeanListener extends EventListener {

    /**
     * Called due to a gateway initiated service agreement termination.
     *
     *@param  event  The event emitted from the 
     * {@link org.mobicents.slee.resource.parlay.fw.application.SABeanImpl SABean}.
     */
    void terminateServiceAgreement(TerminateServiceAgreementEvent event);
}
