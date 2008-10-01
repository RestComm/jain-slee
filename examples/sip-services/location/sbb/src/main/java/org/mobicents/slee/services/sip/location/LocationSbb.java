package org.mobicents.slee.services.sip.location;

import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.address.Address;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.apache.log4j.Logger;

public abstract class LocationSbb implements Sbb, LocationSbbLocalObject {

	private static Logger logger = Logger.getLogger(LocationSbb.class);

	private static LocationService locationService = null;

	private static TimerOptions defaultTimerOptions = createDefaultTimerOptions();
	private static TimerOptions createDefaultTimerOptions() {
		TimerOptions timerOptions = new TimerOptions();
        timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
		return timerOptions;
	}
	
	private Context myEnv;
	private SbbContext sbbContext;
	private TimerFacility timerFacility;
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private ActivityContextNamingFacility activityContextNamingFacility;
	
	public void sbbActivate() {}
	public void sbbCreate() throws CreateException {}
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {}
	public void sbbLoad() {}
	public void sbbPassivate() {}
	public void sbbPostCreate() throws CreateException {}
	public void sbbRemove() {}
	public void sbbRolledBack(RolledBackContext arg0) {}
	public void sbbStore() {}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		try {
			myEnv = (Context) new InitialContext().lookup("java:comp/env");		
			timerFacility = (TimerFacility) myEnv
				.lookup("slee/facilities/timer");
			nullACIFactory = (NullActivityContextInterfaceFactory) myEnv
				.lookup("slee/nullactivity/activitycontextinterfacefactory");
			nullActivityFactory = (NullActivityFactory) myEnv
				.lookup("slee/nullactivity/factory");
			activityContextNamingFacility = (ActivityContextNamingFacility) myEnv
				.lookup("slee/facilities/activitycontextnaming");
			if (locationService == null) {
				locationService = LocationServiceFactory.getLocationService((String) myEnv.lookup("LOCATION_SERVICE_CLASS_NAME"));
			}
		} catch (Exception ne) {
			logger.error("Could not set SBB context: ", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public abstract RegistrationBindingActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);
	
	private String getACIName(Address contactAddress,String sipAddress) {
		return "LocationSbb:aci:ca="+contactAddress.getURI().toString()+",sa="+sipAddress;
	}
	
	// **** SBB LOCAL METHODS
	
	public RegistrationBinding addBinding(String sipAddress,
			Address contactAddress, String comment, long expires, long registrationDate,
			float qValue, String callId, long cSeq)
			throws LocationServiceException {

		// add binding
		RegistrationBinding registrationBinding = locationService.addBinding(sipAddress, contactAddress, comment, expires, registrationDate, qValue, callId, cSeq);
		if (logger.isDebugEnabled()) {
			logger.debug("addBinding: "+registrationBinding);
		}
		// create null aci
		NullActivity nullActivity = nullActivityFactory.createNullActivity();
		ActivityContextInterface aci = null;
		try {
			aci = nullACIFactory.getActivityContextInterface(nullActivity);
			// set name
			activityContextNamingFacility.bind(aci, getACIName(contactAddress, sipAddress));
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		// atach to this activity
		aci.attach(sbbContext.getSbbLocalObject());
		// set timer
		TimerID timerID = timerFacility.setTimer(aci, null,registrationDate + ((expires+1)*1000), defaultTimerOptions);
		// save data in aci 
		RegistrationBindingActivityContextInterface rgAci = asSbbActivityContextInterface(aci);
		rgAci.setTimerID(timerID);
		rgAci.setContactAddress(contactAddress);
		rgAci.setSipAddress(sipAddress);
		
		if(logger.isInfoEnabled()) {
			logger.info("added binding: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
		
		return registrationBinding;
	}

	public Set<String> getRegisteredUsers() throws LocationServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getRegisteredUsers");
		}
		
		return locationService.getRegisteredUsers();
	}

	public Map<Address, RegistrationBinding> getBindings(String sipAddress)
			throws LocationServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getBindings: sipAddress="+sipAddress);
		}
		
		return locationService.getBindings(sipAddress);
	}

	public void updateBinding(RegistrationBinding registrationBinding)
			throws LocationServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("updateBinding: registrationBinding="+registrationBinding);
		}
		
		// get named aci
		ActivityContextInterface aci = activityContextNamingFacility.lookup(getACIName(registrationBinding.getContactAddress(), registrationBinding.getSipAddress()));
		// get the timer id from the aci and reset the timer
		RegistrationBindingActivityContextInterface rgAci = asSbbActivityContextInterface(aci);
		timerFacility.cancelTimer(rgAci.getTimerID());
		rgAci.setTimerID(timerFacility.setTimer(aci, null, registrationBinding.getRegistrationDate() + ((registrationBinding.getExpires()+1) * 1000), defaultTimerOptions));
		// update in location service
		locationService.updateBinding(registrationBinding);
		
		if(logger.isInfoEnabled()) {
			logger.info("binding updated: sipAddress="+registrationBinding.getSipAddress()+",contactAddress="+registrationBinding.getContactAddress());
		}
	}
	
	public void removeBinding(String sipAddress, Address contactAddress)
			throws LocationServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("removeBinding: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
		
		try {
			// lookup null aci from aci naming facility, get timerid and cancel
			// timer
			ActivityContextInterface aci = activityContextNamingFacility
					.lookup(getACIName(contactAddress, sipAddress));
			timerFacility.cancelTimer(asSbbActivityContextInterface(aci)
					.getTimerID());
			// end null activity, detach is no good because this is a different
			// sbb entity then the one that create the binding
			((NullActivity)aci.getActivity()).endActivity();
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		// remove from location service		
		locationService.removeBinding(sipAddress, contactAddress);
		
		if(logger.isInfoEnabled()) {
			logger.info("removed binding: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
	}

	/**
	 * a registration expires
	 * @param timer
	 * @param aci
	 */
	public void onTimerEvent(TimerEvent timer, ActivityContextInterface aci) {

		if (logger.isDebugEnabled()) {
			logger.debug("onTimerEvent()");
		}
		
		aci.detach(sbbContext.getSbbLocalObject());
			
		// cast to rg aci
		RegistrationBindingActivityContextInterface rgAci = asSbbActivityContextInterface(aci);
		// get data from aci
		Address contactAddress = rgAci.getContactAddress();
		String sipAddress = rgAci.getSipAddress();
		
		// unbind from aci so it ends
		try {
			activityContextNamingFacility.unbind(getACIName(contactAddress, sipAddress));
		} catch (Exception e) {
			logger.error(e);
		}
		// remove rg from location service	
		try {
			locationService.removeBinding(sipAddress, contactAddress);
		} catch (Exception e) {
			logger.error(e);
		}

		if(logger.isInfoEnabled()) {
			logger.info("binding expired: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
		
	}

	// PROCESSING SERVICE (DE)ACTIVATION

	public void init() {
		try {
			locationService.init();
		} catch (Exception e) {
			logger.error(e);
		}	
	}
	
	public void shutdown() {
		try {		
			// lets close the jpa location service
			locationService.shutdown();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
}
