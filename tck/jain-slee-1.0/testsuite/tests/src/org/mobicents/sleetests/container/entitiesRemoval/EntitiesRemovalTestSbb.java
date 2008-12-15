package org.mobicents.sleetests.container.entitiesRemoval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.management.ServiceState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.EventLookup;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class EntitiesRemovalTestSbb extends BaseTCKSbb {

	private static int Y1Count=0;
	private String serviceID="EntitiesRemovalTestService#mobicents#0.1";
	private ComponentKey sid = new ComponentKey(this.serviceID);
	private ServiceIDImpl service = new ServiceIDImpl(sid);
	
	protected ActivityContextInterface nullACIForTimer = null;

	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);

	
	protected static Logger logger = Logger
			.getLogger(EntitiesRemovalTestSbb.class.toString());

	protected TimerFacility tf = null;

	private static SleeContainer container = null;

	private static EventLookup eventLookup = null;

	
	
	
	
	
	
	
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		container = SleeContainer.lookupFromJndi();
		eventLookup = container.getEventLookupFacility();
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove
	 * this method, the sbbContext variable and the variable assignment in
	 * setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {

		return sbbContext;
	}

	/**
	 * Does JNDI lookup to create new reference to TimerFacility. If its
	 * successful it stores it in CMP field "timerFacility" and returns this
	 * reference.
	 * 
	 * @return TimerFacility object reference
	 */
	protected TimerFacility retrieveTimerFacility() {
		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			TimerFacility tf = (TimerFacility) myEnv
					.lookup("slee/facilities/timer");

			return tf;
		} catch (NamingException NE) {
			logger.info("COULDNT GET TIMERFACILITY: " + NE.getMessage());
		}
		return null;
	}

	/**
	 * Encapsulates JNDI lookups for creation of nullActivityContextInterface.
	 * 
	 * @return New NullActivityContextInterface.
	 */
	protected ActivityContextInterface retrieveNullActivityContext() {
		ActivityContextInterface localACI = null;
		NullActivityFactory nullACFactory = null;
		NullActivityContextInterfaceFactory nullActivityContextFactory = null;
		try {
			logger.info("Creating nullActivity!!!");
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			nullACFactory = (NullActivityFactory) myEnv
					.lookup("slee/nullactivity/factory");
			NullActivity na = nullACFactory.createNullActivity();
			nullActivityContextFactory = (NullActivityContextInterfaceFactory) myEnv
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			localACI = nullActivityContextFactory
					.getActivityContextInterface(na);

		} catch (NamingException ne) {
			logger.info("Could not create nullActivityFactory: "
					+ ne.getMessage());
		} catch (UnrecognizedActivityException UAE) {
			logger
					.info("Could not create nullActivityContextInterfaceFactory: "
							+ UAE.getMessage());
		}
		return localACI;
	}

	protected void setResultPassed(String msg) throws Exception {
		logger.info("Success: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.TRUE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected void setResultFailed(String msg) throws Exception {
		logger.info("Failed: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.FALSE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected SbbContext sbbContext; // This SBB's SbbContext

	protected void activateTimer() {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis() + 20000,
				tOptions); // 10
	}

	protected void activateTimer(int miliseconds) {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis()
				+ ((long) miliseconds) * 1000, tOptions); // 10
	}

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {
		//??
	}

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		logger.info(" == SERVICE ACTIVATED ==");
		
	}

	public void onTCKResourceEventX1(TCKResourceEventX event,
			ActivityContextInterface aci) {

		logger.info("\n------------------------- X1 -----------------------");
		ServiceComponent svcComponent=null;
		Set activeServiceIDs= new HashSet();
		try {
			for(ServiceID serviceID: SleeContainer.lookupFromJndi().getServiceManagement().getServices(ServiceState.ACTIVE)) {
				activeServiceIDs.add(serviceID);
			}
		} catch (Exception e1) {
			TCKSbbUtils.handleException(e1);
			e1.printStackTrace();
		}
		
		if(!getGotX1())
		{
			setGotX1(true);
			logger.info(" == RECEIVED FIRST X1, SERVICE STATE - Active["+activeServiceIDs.contains(service)+"], ACTIVITY["+aci.getActivity()+"] ==");
			
		}else
		{
			logger.info(" == RECEIVED SECOND X1, SERVICE STATE - Active["+activeServiceIDs.contains(service)+"], ACTIVITY["+aci.getActivity()+"] ==");
			try {
				setResultPassed("== RECEIVED SECOND X1, SERVICE STATE - Active["+activeServiceIDs.contains(service)+"] ==");
				//Y1Count = 0;
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
		}
	}
	
	public void onTCKResourceEventY1(TCKResourceEventY event,
			ActivityContextInterface aci) {

		//This one should be received only once - initialy created sbb will receive it since it is attached to aci, however not 
		//addtional sbb should be created since service is deactivated, thus only originaly creted sbb will receive Y1
		//FIXME ??
		Y1Count++;
		Set activeServiceIDs= new HashSet();
		try {
			for(ServiceID serviceID: SleeContainer.lookupFromJndi().getServiceManagement().getServices(ServiceState.ACTIVE)) {
				activeServiceIDs.add(serviceID);
			}
		} catch (Exception e1) {
			TCKSbbUtils.handleException(e1);
			e1.printStackTrace();
		}
		
		logger.info("\n------------------------- Y1["+Y1Count+"] -----------------------");
		if(Y1Count>=2)
		{
			try {
				setResultFailed(" == RECEIVED Y1["+Y1Count+"], THIS SHOULD NOT HAPPEN, SERVICE STATE - Active["+activeServiceIDs.contains(service)+"], ACTIVITY["+aci.getActivity()+"] ==");
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
		}else
		{
			logger.info("\n--------------------- INITIAL SBB RECEIVED Y1 --------------");
		}
	}

	public InitialEventSelector chooseService(InitialEventSelector ies) {

		Object event = ies.getEvent();
		logger.info("IES CALLED");
		try {
			if (event instanceof TCKResourceEventImpl) {
				TCKResourceEventImpl tckEvent = (TCKResourceEventImpl) event;
				String eventName = tckEvent.getEventTypeName();
				if (eventName.indexOf("X.X1") != -1) {

					String name = "X_NAME";

					ies.setCustomName(name);
					ies.setInitialEvent(true);

				} else if (eventName.indexOf("Y.Y1") != -1) {

					String name = "Y_NAME";

					ies.setCustomName(name);
					ies.setInitialEvent(true);
				}
			} else
				ies.setInitialEvent(false);

			return ies;
		} catch (Exception e) {
			e.printStackTrace();
			// THIS ISNT TAKEN INTO CONSIDERATION....
			ies.setInitialEvent(false);
			return ies;
		}

	}
	public abstract void setGotX1(boolean val);
	public abstract boolean getGotX1();
}
