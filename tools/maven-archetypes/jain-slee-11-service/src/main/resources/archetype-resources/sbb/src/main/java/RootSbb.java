package ${package};

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.slee.facilities.Tracer;

public abstract class RootSbb implements javax.slee.Sbb {

	private SbbContext sbbContext = null; // This SBB's context			
	//log facility instead of java.util or log4j
	private Tracer logger = null;
	// -- EVENT HANDLERS
	
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		
		if(logger.isFineEnabled()) {
			logger.fine("onServiceStartedEvent(event=" + event.toString() + ",aci="
				+ aci.toString() + ")");
		}
		
		try {
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				// it's this service that is starting
				logger.info("service activated...");
			}		
			// don't want to receive further events on this activity
			aci.detach(this.sbbContext.getSbbLocalObject());
			
		} catch (Exception e) {
			logger.severe("Can't handle service started event.", e);
		}
	}
	
	// -- SBB OBJECT LIFECYLE METHODS
	
	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
		this.logger = context.getTracer("TracerName-PutSomething-Here");
		if (logger.isFineEnabled()) {
			logger.fine("setSbbContext(...)");
		}
		this.sbbContext = context;
		
	}

	public void unsetSbbContext() {
		if (logger.isFineEnabled()) {
			logger.fine("unsetSbbContext()");
		}
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
		if (logger.isFineEnabled()) {
			logger.fine("sbbCreate()");
		}
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		if (logger.isFineEnabled()) {
			logger.fine("sbbPostCreate()");
		}
	}

	public void sbbActivate() {
		if (logger.isFineEnabled()) {
			logger.fine("sbbActivate()");
		}
	}

	public void sbbPassivate() {
		if (logger.isFineEnabled()) {
			logger.fine("sbbPassivate()");
		}
	}

	public void sbbRemove() {
		if (logger.isFineEnabled()) {
			logger.fine("sbbRemove()");
		}
	}

	public void sbbLoad() {
		if (logger.isFineEnabled()) {
			logger.fine("sbbLoad()");
		}
	}

	public void sbbStore() {
		if (logger.isFineEnabled()) {
			logger.fine("sbbStore()");
		}
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		if (logger.isFineEnabled()) {
			logger.fine("sbbExceptionThrown(exception=" + exception.toString()
					+ ",event=" + event.toString() + ",activity="
					+ activity.toString() + ")");
		}
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		if (logger.isFineEnabled()) {
			logger.fine("sbbRolledBack(sbbRolledBack=" + sbbRolledBack.toString()
				+ ")");
		}
	}


}