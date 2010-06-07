/*
 *
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.examples.ss7.isup;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;


/**
 * 
 * @author baranowb
 */
public abstract class ISUPSbb implements Sbb {
	
	private SbbContext sbbContext;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public ISUPSbb() {
	}

	// ////////////////////////
	// SLEE events handlers //
	// ////////////////////////
	/**
	 * Deploy process definition when the service is activated by
	 * SLEE
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
		ServiceActivity sa = (ServiceActivity) aci.getActivity();
		if (sa.getService().equals(this.sbbContext.getService())) {
			
		}
	}


	/**
	 * 
	 * @param event
	 * @param aci
	 */
	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {
		
	}

	
	public void onISUPEvent(Object event, ActivityContextInterface aci) {
		System.err.println("--------------------------");
		System.err.println("Event: "+event);
		System.err.println("--------------------------");
		
	}
	// success
	//public void onSuccessEvent(ResponseEvent event, ActivityContextInterface ac) {
		// nothing,
	//}

	

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer("ISUPSbb");

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			//provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			//acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}
}
