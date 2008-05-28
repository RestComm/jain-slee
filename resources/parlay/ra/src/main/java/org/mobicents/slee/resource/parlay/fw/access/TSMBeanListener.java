package org.mobicents.slee.resource.parlay.fw.access;
import java.util.EventListener;

import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;


/**
 * This is a listener interface which must be implemented by an application
 * which uses the 
 * {@link org.mobicents.slee.resource.parlay.fw.access.TSMBean TSMBean}.
 *
 * When the applciation object implementing this is registered as a listener it can
 * receive gateway originated events.
 */
public interface TSMBeanListener extends EventListener {

    /**
     * Called due to a gateway initiated access termination.
     *
     *@param  event  The event emitted from the 
     * {@link org.mobicents.slee.resource.parlay.fw.access.TSMBean TSMBean}.
     */
    void terminateAccess(TerminateAccessEvent event);
}
