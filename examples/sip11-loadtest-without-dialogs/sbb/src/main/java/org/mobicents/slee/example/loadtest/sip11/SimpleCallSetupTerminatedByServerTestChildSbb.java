
package org.mobicents.slee.example.loadtest.sip11;

import javax.sip.message.Request;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;

public abstract class SimpleCallSetupTerminatedByServerTestChildSbb implements javax.slee.Sbb, SimpleCallSetupTerminatedByServerTestChildSbbLocalObject {

	public abstract Request getBye();
	public abstract void setBye(Request value);

	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context; 	
	}
	
    public void unsetSbbContext() { this.sbbContext = null; }
    
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

}
