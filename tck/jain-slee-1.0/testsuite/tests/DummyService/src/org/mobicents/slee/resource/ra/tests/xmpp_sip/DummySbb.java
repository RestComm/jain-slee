package org.mobicents.slee.resource.ra.tests.xmpp_sip;

import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;


public abstract class DummySbb implements javax.slee.Sbb {

	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) { this.sbbContext = context; }
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
	

	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext
	
	
	

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {

	


	}
	
	
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		

	}

	

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		
	}

	public void onDummy_1(org.mobicents.slee.resource.dummy.ratype.DummyEvent event,
			ActivityContextInterface aci) {
		
	}

	public void onDummy_2(org.mobicents.slee.resource.dummy.ratype.DummyEvent event,
			ActivityContextInterface aci) {
		
	}
}
