/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/


package org.mobicents.slee.container.service;

import java.io.Serializable;

import javax.slee.ServiceID;
import javax.slee.serviceactivity.ServiceActivity;


/**
 *Service Activity Implementation.
 *The Administrator uses the SLEE’s management interface to activate and deactivate Services. Each Service
 *has an associated Activity. The specific Java type for this Activity is ServiceActivity.
 *The SLEE fires a Service Started Event on a ServiceActivity object after the Service is started. A
 *Service is started after one of the following occurs:
 *<ul>
 * <li>The SLEE is in the Running state and a ServiceManagementMBean object is invoked to activate
 * the Service, or
 * <li> The SLEE becomes active when the SLEE transitions from the Starting to the Running state and
 *   the SLEE starts the Service because the persistent state of the Service says it should be active.
 *   SBBs register interest in Service Started Events like any other event using an event element in 
 *   their deployment descriptor. An SBB can only receive a Service Started Event fired by the SLEE as 
 *  an initial event since this event is fired only once on a ServiceActivity object, at the time 
 * the corresponding Service Activity begins.
 * <li>Service Started Events enable an SBB to be notified when a Service becomes active. For example, the root
 *  SBB of a Service may listen to Service Started Events by specifying the event type of the Service Started
 *  Event as an initial event type of the SBB. The root SBB of a Wake-up Service may reinstate timers from
 *  persistent data stored in Profile Tables. Daemon-like Service entities may also use the Service Started
 *  Event and Service Activity to keep them alive instead of creating and attaching to NullActivity objects.
 * </ul>
 */
public class ServiceActivityImpl implements ServiceActivity, Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5887150772887499452L;

	/*
     * The Service for which this is a service Activity
     */
    private ServiceID serviceid;

	public ServiceActivityImpl(Service service) {
		if (service != null) {
			this.serviceid = service.getServiceID();
		} else {
			throw new IllegalArgumentException("service is null");
		}
	}

	public ServiceID getService() {
		return this.serviceid;
	}

	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			return this.serviceid.equals(((ServiceActivityImpl) obj).serviceid);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.getServiceID().toString().hashCode();
	}

	public ServiceID getServiceID() {
		return this.serviceid;
	}

}

