package org.mobicents.sleetests.container.management.jmx.startStopSequence;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.serviceactivity.ServiceStartedEventImpl;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class StartStopSequenceTestSbb extends BaseTCKSbb {

	
	private static ServiceID localSID=new ServiceIDImpl(new ComponentKey("StartStopSequenceTestSbb","mobicents","0.1")); 
	
	

	protected TimerOptions tOptions = new TimerOptions(false, 100,
			TimerPreserveMissed.LAST);

	protected static Logger logger = Logger
			.getLogger(StartStopSequenceTestSbb.class.toString());

	protected TimerFacility tf = null;

	private static SleeContainer container = null;

	private static EventLookup eventLookup = null;

	
	

	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		container = SleeContainer.lookupFromJndi();
		eventLookup = container.getEventLookupFacility();
		tf = retrieveTimerFacility();
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

		// TimerID tid = tf.setTimer(getNullAciForTimers(), null, System
		// .currentTimeMillis() + 500, tOptions); // 10

		// if (getCurrentTimerID() != null)
		// tf.cancelTimer(getCurrentTimerID());

		// setCurrentTimerID(tid);
	}

	protected void activateTimer(int miliseconds) {
		// TimerID tid = tf.setTimer(getNullAciForTimers(), null, System
		// / .currentTimeMillis()
		// + ((long) miliseconds) * 1000, tOptions); // 10

		// if (getCurrentTimerID() != null)
		// tf.cancelTimer(getCurrentTimerID());

		// setCurrentTimerID(tid);
	}

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		logger.info(" == SERVICE ACTIVATED ==");

	}

	public void onTCKResourceEventX1(TCKResourceEventX event,
			ActivityContextInterface aci) {
		

	}

	public void onTCKResourceEventX2(TCKResourceEventX event,
			ActivityContextInterface aci) {

		

	}

	public void onTCKResourceEventX3(TCKResourceEventX event,
			ActivityContextInterface aci) {

		
	}

	public void onTCKResourceEventY1(TCKResourceEventY event,
			ActivityContextInterface aci) {

		
	}

	public void onTCKResourceEventY2(TCKResourceEventY event,
			ActivityContextInterface aci) {

	}

	public void onTCKResourceEventY3(TCKResourceEventY event,
			ActivityContextInterface aci) {

	}

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {

	}

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {

		

	}

	public InitialEventSelector chooseService(InitialEventSelector ies) {

		Object event = ies.getEvent();
		try {
			if (event instanceof TCKResourceEventImpl) {


				ies.setCustomName("StartStopSequence");
				ies.setInitialEvent(true);
			} else if(event instanceof ServiceStartedEventImpl)
			{
				ServiceStartedEventImpl startEvent=(ServiceStartedEventImpl)event;
				ServiceID SID=startEvent.getServiceID();
				//if(SID.equals(getSbbContext().getService()))
				if(SID.equals(localSID))
				{
					ies.setCustomName("StartStopSequence");
					ies.setInitialEvent(true);
				}else
				{
					ies.setInitialEvent(false);
				}
				
			}else
			{
				ies.setInitialEvent(false);
			}
			return ies;
		} catch (Exception e) {
			e.printStackTrace();
			// THIS ISNT TAKEN INTO CONSIDERATION....
			ies.setInitialEvent(false);
			return ies;
		}

	}




}
