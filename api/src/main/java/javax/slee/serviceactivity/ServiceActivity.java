package javax.slee.serviceactivity;

import javax.slee.ServiceID;

/**
 * This interface is implemented by Service activity objects.  Service activities can
 * be used by SBBs to monitor the managed lifecycle of the Service they are a part of.
 * When a Service is started, a Service activity is created and a {@link ServiceStartedEvent}
 * is fired on the activity.  When a Service is stopped, the Service activity is ended
 * and a {@link javax.slee.ActivityEndEvent} is fired on the activity.
 *
 * @see ServiceActivityFactory
 * @see ServiceActivityContextInterfaceFactory
 */
public interface ServiceActivity {
    /**
     * Get the Service component identifier that identifies the Service component
     * that this Service activity was created for.
     * @return the Service component identifier.
     * @since SLEE 1.1
     */
    public ServiceID getService();
}

