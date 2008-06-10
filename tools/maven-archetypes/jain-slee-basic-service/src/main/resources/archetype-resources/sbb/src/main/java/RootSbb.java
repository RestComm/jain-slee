package ${package};

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import org.apache.log4j.Logger;

public abstract class RootSbb implements javax.slee.Sbb {

	private SbbContext sbbContext = null; // This SBB's context			
	
	// -- EVENT HANDLERS
	
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("onServiceStartedEvent(event=" + event.toString() + ",aci="
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
			logger.error("Can't handle service started event.", e);
		}
	}
	
	// -- SBB OBJECT LIFECYLE METHODS
	
	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSbbContext(...)");
		}
		this.sbbContext = context;
	}

	public void unsetSbbContext() {
		if (logger.isDebugEnabled()) {
			logger.debug("unsetSbbContext()");
		}
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbCreate()");
		}
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbPostCreate()");
		}
	}

	public void sbbActivate() {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbActivate()");
		}
	}

	public void sbbPassivate() {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbPassivate()");
		}
	}

	public void sbbRemove() {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbRemove()");
		}
	}

	public void sbbLoad() {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbLoad()");
		}
	}

	public void sbbStore() {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbStore()");
		}
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbExceptionThrown(exception=" + exception.toString()
					+ ",event=" + event.toString() + ",activity="
					+ activity.toString() + ")");
		}
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		if (logger.isDebugEnabled()) {
			logger.debug("sbbRolledBack(sbbRolledBack=" + sbbRolledBack.toString()
				+ ")");
		}
	}

	private static Logger logger = Logger.getLogger(RootSbb.class);

}