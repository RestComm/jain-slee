package org.mobicents.slee.annotations.examples.sbb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;

/**
 * Simple example for a not annotated Sbb, on service start fires a 1 sec timer and
 * on timer event calculates real delay. Requires the usual XML descriptor. 
 * 
 * @author Eduardo Martins
 * 
 */
public abstract class SimpleExampleSbb implements Sbb {

	private static final long TIMER_DURATION = 1000;
	
	private Tracer tracer;
	private TimerFacility timerFacility;
	
	@Override
	public void setSbbContext(SbbContext context) {
		tracer = context.getTracer("default");
		try {
			final Context myEnv = (Context) new InitialContext();
			// slee facilities
			this.timerFacility = (TimerFacility) myEnv
					.lookup(TimerFacility.JNDI_NAME);			
		} catch (Exception e) {
			tracer.severe("Failure in setSbbContext()",e);
		}
		
	}
	
	public abstract Long getStartTime();
	public abstract void setStartTime(Long value);
	
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tracer.info("service started");
		timerFacility.setTimer(aci, null, TIMER_DURATION, new TimerOptions());
		setStartTime(Long.valueOf(System.currentTimeMillis()));
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		long delay = (System.currentTimeMillis()-TIMER_DURATION) - getStartTime();
		tracer.info("timer expired, delay = "+delay+" ms.");
	}
	
	@Override
	public void sbbActivate() {
		
	}
	
	@Override
	public void sbbCreate() throws CreateException {
		
	}
	
	@Override
	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface aci) {
		
	}
	
	@Override
	public void sbbLoad() {
		
	}
	
	@Override
	public void sbbPassivate() {
		
	}
	
	@Override
	public void sbbPostCreate() throws CreateException {
		
	}
	
	@Override
	public void sbbRemove() {
		
	}
	
	@Override
	public void sbbRolledBack(RolledBackContext context) {
		
	}
	
	@Override
	public void sbbStore() {
		
	}
	
	@Override
	public void unsetSbbContext() {
		
	}
	
}
