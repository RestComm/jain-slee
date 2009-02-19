package org.mobicents.slee.container.component.test.du3;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceStartedEvent;

public abstract class Sbb2 implements Sbb {

	public abstract void setSbb1(Sbb1LocalObject sbbLocalObject);
	public abstract Sbb1LocalObject getSbb1();
	
	public void sbbActivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbLoad() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbPassivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbRemove() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbRolledBack(RolledBackContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbStore() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSbbContext(SbbContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void unsetSbbContext() {
		// TODO Auto-generated method stub
		
	}
	
	public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
		
	}
}
