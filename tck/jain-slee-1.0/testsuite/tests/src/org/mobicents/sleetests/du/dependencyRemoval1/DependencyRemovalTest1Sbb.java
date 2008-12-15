package org.mobicents.sleetests.du.dependencyRemoval1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class DependencyRemovalTest1Sbb extends BaseTCKSbb {

	public static final int SERVICE_NAME_POSITION = 0;

	public static final int EVENT_IDS_POSITION = 1;

	public static final int RESOURCE_OPTIONS_POSITION = 2;

	public static final int RA_TYPE_COMPONENT_KEY = 3;

	public static final int RA_COMPONENT_KEY = 4;

	public static final int COMPONENTKEYS_TO_SERVICEIDS_POSITION = 5;

	public static final int COMPONENTKEYS_TO_OPTIONS_POSITION = 6;

	private ServiceDescriptorImpl serviceDesc = null;

	protected ActivityContextInterface nullACIForTimer = null;

	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);

	/*
	 * protected boolean cancelingEventReceived = false;
	 * 
	 * protected boolean dialogTerminatedEventReceived = false;
	 * 
	 * protected boolean dialogSetupFialedEventReceived = false;
	 */
	protected static Logger logger = Logger
			.getLogger("InstallServiceTestSbb.class");

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

	public abstract TestInterface asSbbActivityContextInterface(
			ActivityContextInterface activity);

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

	}

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {

	}

	public void onTCKResourceEventX2(TCKResourceEventX event,
			ActivityContextInterface aci) {
		Object[] message=(Object[]) event.getMessage();
		
		DeployableUnitIDImpl duID1,duID2;
		String serviceID1,serviceID2;
		
		duID1=(DeployableUnitIDImpl) message[0];
		serviceID1=(String) message[1];
		duID2=(DeployableUnitIDImpl) message[2];
		serviceID2=(String) message[3];
		
		TestInterface ti=asSbbActivityContextInterface(aci);
		
		int test=ti.getBothDeployed();
		try{
		if(test!=4)
		{
			
			// IT SHOULD BE EQUAL 4
			setResultFailed(" BOTH_DEPLOYED_COUNT SHOULD BE EQUAL 4, it is ["+test+"]");
			return;
		}
		
		
		SleeContainer con=SleeContainer.lookupFromJndi();
		
		DeployableUnitID[] dids=con.getDeployableUnitManagement().getDeployableUnits();
		
		boolean du1Present=false;
		boolean du2Present=false;
		for(int i=0;i<dids.length;i++)
		{
			logger.info("DID["+dids[i]+"]");
			if(dids[i].equals(duID1))
				du1Present=true;
			if(dids[i].equals(duID2))
				du2Present=true;
	
			
		}
	
		if(!du1Present || !du2Present)
		{
			// BOTH DU SHOULD BE PRESENT
			setResultFailed("BOTH DU SHOULD BE PRESENT DU1["+du1Present+"] DU2["+du2Present+"]");
			return;
		}
		
		
		
		ComponentKey key=new ComponentKey("Sbb1","mobicents","0.1");
		ComponentIDImpl compID=new SbbIDImpl(key);
		try{
			con.getComponentManagement().getComponentDescriptor(compID);
		}catch(IllegalArgumentException e)
		{
			setResultFailed(" === COULDNT LOCATE COMP["+compID+"]["+e+"]");
			return;
		}
		key=new ComponentKey("Sbb2","mobicents","0.1");
		compID=new SbbIDImpl(key);
		try{
			con.getComponentManagement().getComponentDescriptor(compID);
		}catch(IllegalArgumentException e)
		{
			setResultFailed(" === COULDNT LOCATE COMP["+compID+"]["+e+"]");
			return;
		}
		key=new ComponentKey("Sbb3","mobicents","0.1");
		compID=new SbbIDImpl(key);
		try{
			con.getComponentManagement().getComponentDescriptor(compID);
		}catch(IllegalArgumentException e)
		{
			setResultFailed(" === COULDNT LOCATE COMP["+compID+"]["+e+"]");
			return;
		}
				
		}catch(Exception ee)
		{
			TCKSbbUtils.handleException(ee);
		}
	}

	public void onTCKResourceEventY1(TCKResourceEventY event,
			ActivityContextInterface aci) {

		// Here DU2, should be undeployed - Sbb3 should not be present, however
		// Sbb2 should still be present.
		SleeContainer con = SleeContainer.lookupFromJndi();

		DeployableUnitID[] dids = con.getDeployableUnitManagement().getDeployableUnits();

		Object[] message = (Object[]) event.getMessage();

		DeployableUnitIDImpl duID1, duID2;
		String serviceID1, serviceID2;

		duID1 = (DeployableUnitIDImpl) message[0];
		serviceID1 = (String) message[1];
		duID2 = (DeployableUnitIDImpl) message[2];
		serviceID2 = (String) message[3];

		boolean du1Present = false;
		boolean du2Present = false;
		for (int i = 0; i < dids.length; i++) {
			logger.info("DID[" + dids[i] + "]");
			if (dids[i].equals(duID1))
				du1Present = true;
			if (dids[i].equals(duID2))
				du2Present = true;

		}

		if (!du1Present || du2Present) {
			// BOTH DU SHOULD BE PRESENT
			try {
				setResultFailed("DU1, should be present, DU2 not, DU1["	+ du1Present + "] DU2[" + du2Present + "]");
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
			return;
		}
		
		
		ComponentKey key=new ComponentKey("Sbb1","mobicents","0.1");
		ComponentIDImpl compID=new SbbIDImpl(key);
		try{
			con.getComponentManagement().getComponentDescriptor(compID);
		}catch(IllegalArgumentException e)
		{
			try {
				setResultFailed(" === COULDNT LOCATE COMP["+key+"]");
			} catch (Exception e1) {
				TCKSbbUtils.handleException(e1);
				e1.printStackTrace();
			}
			return;
		}
		key=new ComponentKey("Sbb2","mobicents","0.1");
		compID=new SbbIDImpl(key);
		try{
			con.getComponentManagement().getComponentDescriptor(compID);
		}catch(IllegalArgumentException e)
		{
			try {
				setResultFailed(" === COULDNT LOCATE COMP["+key+"]");
			} catch (Exception e1) {
				TCKSbbUtils.handleException(e1);
				e1.printStackTrace();
			}
			return;
		}
		
		try {
			setResultPassed("TEST PASSED");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		
	}

	public InitialEventSelector chooseService(InitialEventSelector ies) {

		Object event = ies.getEvent();

		try {
			if (event instanceof TCKResourceEventImpl) {
				TCKResourceEventImpl tckEvent = (TCKResourceEventImpl) event;
				String eventName = tckEvent.getEventTypeName();

				String name = "CHOOSE_ME";
				ies.setCustomName(name);
				ies.setInitialEvent(true);
				logger
						.log(
								Level.FINEST,
								"\n================= NAMEX ================================\n"
										+ name
										+ "\n=======================================================");

			} else
				ies.setInitialEvent(false);

			logger
					.log(
							Level.FINEST,
							"\n----------------------------------------------------\nISE:\n"
									+ ies
									+ "\n-------------------------------------------------");
			return ies;
		} catch (Exception e) {
			e.printStackTrace();
			// THIS ISNT TAKEN INTO CONSIDERATION....
			ies.setInitialEvent(false);
			return ies;
		}

	}

}
