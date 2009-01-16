package javax.slee.serviceactivity;

import javax.slee.ServiceID;

/**
 * This interface is implemented by Service started events genererated by
 * the SLEE when a Service is started.  A Service starts when either:
 * <ul>
 *   <li>the SLEE is in the Running state and the Service is activated via
 *       the <code>ServiceManagementMBean</code>
 *   <li>the persistent state of the Service says that the Service should be
 *       active, and a previously stopped SLEE transitions to the Running state.
 * </ul>
 * <p>
 * The event type name of Service started events is
 * "<code>javax.slee.serviceactivity.ServiceStartedEvent</code>".  The 1.0
 * version of this event may be delivered by the SLEE to SBBs in any Service.
 * The 1.1 version of this event, when fired by the SLEE, is delivered only to
 * SBBs in the Service that is starting. 
 */
public interface ServiceStartedEvent {
    /**
     * Get the Service component identifier that identifies the Service component
     * that has started.
     * @return the Service component identifier.
     * @since SLEE 1.1
     */
    public ServiceID getService();
}

