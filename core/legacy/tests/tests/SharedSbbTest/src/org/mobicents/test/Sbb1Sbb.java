package org.mobicents.test;

import javax.slee.*;


import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class Sbb1Sbb implements javax.slee.Sbb {


	
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
	
    public abstract TestInterface asSbbActivityContextInterface(ActivityContextInterface activity);
	
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

	
	public abstract ChildRelation getSbb2Sbb();

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {
		
	}
	
	public void onTCKResourceEventX1(TCKResourceEventX event,
			ActivityContextInterface aci) {

		if(getSbb2Sbb().isEmpty())
		{
			
			TestInterface ti=asSbbActivityContextInterface(aci);
			ti.setBothDeployed(ti.getBothDeployed()+1);
			SbbLocalObject slo=null;
			try {
				slo = getSbb2Sbb().create();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				TCKSbbUtils.handleException(e);
			} 
			aci.attach(slo);
			
		}
	}
	
	
	public void onTCKResourceEventX2(TCKResourceEventX event,
			ActivityContextInterface aci) {

	}
	
	
}
