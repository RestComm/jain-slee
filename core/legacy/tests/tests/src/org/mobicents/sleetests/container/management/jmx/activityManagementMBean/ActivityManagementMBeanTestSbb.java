package org.mobicents.sleetests.container.management.jmx.activityManagementMBean;

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
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.EventLookup;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class ActivityManagementMBeanTestSbb extends BaseTCKSbb {

	// 1. Service activity, and 1 TCKRa activity?
	//protected int numberOfActivitieAfterDeployingService = 2;

	//protected int numberOfActivitieAfterCreatingNulls = 6; //+4

	//protected int numberOfActivitieAfterRemovingNulls = 4; //+2

	private final int _ADD_AC = 0;

	private final int _REMOVE_AC = 1;

	private final int _END_NULL = 2;

	private final int _LIST = 3;

	protected TimerOptions tOptions = new TimerOptions(false, 100,
			TimerPreserveMissed.LAST);

	protected static Logger logger = Logger
			.getLogger(ActivityManagementMBeanTestSbb.class.toString());

	protected TimerFacility tf = null;

	private static SleeContainer container = null;

	private static EventLookup eventLookup = null;

	public final static String _NULL_1_Name = "_NULL_1_Name";

	public final static String _NULL_2_Name = "_NULL_2_Name";

	public final static String _NULL_TOMbean_END_Name = "_NULL_TOMbean_END_Name";

	public final static String _NULL_3_NAME_FOR_LINGERER = "_NULL_3_NAME_FOR_LINGERER";

	public final static String _MBEAN_O_NAME = "slee:name=ActivityManagementMBean";

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

		logger.info("\n------------------------- X1 -----------------------");

		setReport(new StringBuffer("\n"));

		int count = container.getActivityContextCount();
		
		logger.info("===[***] Activity Count[" + count + "] on start");
		this.setAciNumberOnStart(count);

		ActivityContextNamingFacility naming = container
				.getActivityContextNamingFacility();
		ActivityContextInterface naci = retrieveNullActivityContext();
		naci.attach(getSbbContext().getSbbLocalObject());
		try {
			naming.bind(naci, _NULL_1_Name);
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		logger.info("[TEST] Adding ["
				+ container.getActivityContextFactory()
						.getActivityContextId(naci.getActivity()) + "]");

		naci = retrieveNullActivityContext();
		naci.attach(getSbbContext().getSbbLocalObject());
		try {
			naming.bind(naci, _NULL_2_Name);
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		logger.info("[TEST] Adding ["
				+ container.getActivityContextFactory()
						.getActivityContextId(naci.getActivity()) + "]");
		
		
		naci = retrieveNullActivityContext();
		naci.attach(getSbbContext().getSbbLocalObject());
		try {
			naming.bind(naci, _NULL_TOMbean_END_Name);
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		logger.info("[TEST] Adding NACI TO END BY MBEAN["
				+ container.getActivityContextFactory()
						.getActivityContextId(naci.getActivity()) + "]");

		naci = retrieveNullActivityContext();
		naci.attach(getSbbContext().getSbbLocalObject());
		try {
			naming.bind(naci, _NULL_3_NAME_FOR_LINGERER);
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		logger.info("[TEST] Adding ["
				+ SleeContainer.lookupFromJndi().getActivityContextFactory()
						.getActivityContextId(naci.getActivity()) + "]");
		
		setTestStage(this._ADD_AC);

		// activateTimer();

	}

	public void onTCKResourceEventX2(TCKResourceEventX event,
			ActivityContextInterface aci) {

		// Now lets check how many ac we have
		int acCount = container.getActivityContextCount();

		logger.info("===[$$$]onX2[ADD_AC] Activity Count[" + acCount
				+ "] Should be [" + (getAciNumberOnStart()+ 4) + "]");

		if (acCount != (getAciNumberOnStart()+ 4))
			setAciNumbersAfterCreation(false);
		else
			setAciNumbersAfterCreation(true);

		getReport().append(
				"1. Numbber of aci after creating additional acis is OK["
						+ getAciNumbersAfterCreation() + "]\n");

		setTestStage(_REMOVE_AC);

		ActivityContextNamingFacility naming = container
				.getActivityContextNamingFacility();

		ActivityContextInterface naci = naming.lookup(_NULL_1_Name);
		if (naci != null) {
			((NullActivity)naci.getActivity()).endActivity();
		} else {
			// We shouldnt be here, since TckSbbUtils.handleException() should
			// terminated test if something goes wrong in onX1
			logger.info("Cant find Null Aci in naming facility!!!");
			getReport().append(
					"   * Retrieval of named aci[" + _NULL_1_Name
							+ "] has FAILED!!!\n");
		}

		naci = naming.lookup(_NULL_2_Name);
		if (naci != null) {
			((NullActivity)naci.getActivity()).endActivity();
		} else {
			// We shouldnt be here, since TckSbbUtils.handleException() should
			// terminated test if something goes wrong
			logger.info("Cant find Null Aci in naming facility!!!");
			getReport().append(
					"   * Retrieval of named aci[" + _NULL_2_Name
							+ "] has FAILED!!!\n");
		}

	}

	public void onTCKResourceEventX3(TCKResourceEventX event,
			ActivityContextInterface aci) {

		// here we are after removing two null acis, we shold have 4 of them
		int acCount = container.getActivityContextCount();

		logger.info("===[$$$]onX3[REMOVE_AC] Activity Count[" + acCount
				+ "] Should be [" + (getAciNumberOnStart()+2) + "]");

		if (acCount != (getAciNumberOnStart()+2)) {
			setAciNumbersAfterRemoval(false);
		} else {
			setAciNumbersAfterRemoval(true);
		}

		getReport().append(
				"2. Number of acis after removing 2 is OK ["
						+ getAciNumbersAfterRemoval() + "]\n");

		setTestStage(_END_NULL);

		try {
			MBeanServer mbs = container.getMBeanServer();
			ObjectName mbeanName = new ObjectName(_MBEAN_O_NAME);
			// Iterator it = getNullAcisForTest().iterator();
			// ActivityContextInterface naci = (ActivityContextInterface) it
			// .next();
			// it.remove();
			ActivityContextNamingFacility naming = container
					.getActivityContextNamingFacility();

			ActivityContextInterface naci = naming
					.lookup(_NULL_TOMbean_END_Name);

			if (naci == null) {

				logger.info("Cant find Null Aci in naming facility!!!");
				getReport().append("4. MBean end null ac is OK [false]!!!\n");
				getReport().append(
						"   * Retrieval of named aci[" + _NULL_TOMbean_END_Name
								+ "] has FAILED!!!\n");
				setMBeanRemove(false);

			}

			String acID = (SleeContainer.lookupFromJndi())
					.getActivityContextFactory().getActivityContextId(
							naci.getActivity());

			mbs.invoke(mbeanName, "endActivity", new Object[] { acID },
					(String[]) new String[] { "java.lang.String" });
		} catch (Exception e) {
			try {
				setResultFailed("Failed to invoke operation on MBean due to:"
						+ e.getMessage());
			} catch (Exception e1) {
				TCKSbbUtils.handleException(e1);
			}
			e.printStackTrace();
		}
	}

	public void onTCKResourceEventY1(TCKResourceEventY event,
			ActivityContextInterface aci) {

		boolean pass = getMBeanRemove() && getAciNumbersAfterRemoval()
				&& getAciNumbersAfterCreation();

		if (pass) {
			logger.info(getReport().toString());
			try {
				setResultPassed(getReport().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				setResultFailed(getReport().toString());
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
		}
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

		if (getTestStage() == _END_NULL) {

			// Yupi we have ended activities, we should be still attached to one
			// NULL AC
			// Lets iterate
			logger.info("===[$$$]onActivityEndEvent[END_NULL]");
			ActivityContextInterface[] acis = getSbbContext().getActivities();

			int nullAcCount = 0;
			for (int i = 0; i < acis.length; i++) {
				if (acis[i].getActivity() instanceof NullActivity) {
					nullAcCount++;
					if (!acis[i].getActivity().equals(aci.getActivity())) {
						((NullActivity)acis[i].getActivity()).endActivity();
					}
				}
			}

			if (nullAcCount != 2) {
				setMBeanRemove(false);
				logger
						.info("===[$$$] Unexpected number of NULL AC we are attached to ["
								+ nullAcCount + "] should be [2]"); // +1 -
																	// because
																	// we are
																	// still
																	// attached
																	// to ending
																	// null ac
			} else {
				setMBeanRemove(true);
				logger
						.info("===[$$$] Number of NULL AC we are attached to is correct["
								+ nullAcCount + "]");
			}

			getReport().append(
					"3. MBean end null ac is OK  [" + getMBeanRemove() + "]");
			// At this stage ActivityManagementMBeanTest.run will call list
			// memthods.
			this.setTestStage(_LIST);
			// activateTimer();

		}

		

	}

	public InitialEventSelector chooseService(InitialEventSelector ies) {

		Object event = ies.getEvent();
		logger.info("IES CALLED");
		try {
			if (event instanceof TCKResourceEventImpl) {
				TCKResourceEventImpl tckEvent = (TCKResourceEventImpl) event;
				String eventName = tckEvent.getEventTypeName();

				ies.setCustomName("ActivityManagementMBeanTestSbb");
				ies.setInitialEvent(true);
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

	// public abstract void setNullAciForTimers(ActivityContextInterface aci);

	// public abstract ActivityContextInterface getNullAciForTimers();

	// public abstract void setCurrentTimerID(TimerID tid);

	// public abstract TimerID getCurrentTimerID();

	// public abstract void setNullAcisForTest(Set s);

	// public abstract Set getNullAcisForTest();

	public abstract void setTestStage(int stage);

	public abstract int getTestStage();

	public abstract void setAciNumberOnStart(int value);

	public abstract int getAciNumberOnStart();

	public abstract void setAciNumbersAfterCreation(boolean passed);

	public abstract boolean getAciNumbersAfterCreation();

	public abstract void setAciNumbersAfterRemoval(boolean passed);

	public abstract boolean getAciNumbersAfterRemoval();

	public abstract void setMBeanRemove(boolean passed);

	public abstract boolean getMBeanRemove();

	public abstract void setReport(StringBuffer sb);

	public abstract StringBuffer getReport();

}
