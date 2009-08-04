package org.mobicents.slee.services.sip.location;

import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
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

public abstract class LocationSbb implements Sbb, LocationService {

	private static final Logger logger = Logger.getLogger(LocationSbb.class);

	private static LocationService locationService = null;

	private static final TimerOptions defaultTimerOptions = createDefaultTimerOptions();
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

	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	/**
	 * 
	 * @param aci
	 * @return
	 */
	public abstract RegistrationBindingActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);
	
	/*
	 * 
	 */
	private String getACIName(String contactAddress,String sipAddress) {
		return "LocationSbb:aci:ca="+contactAddress+",sa="+sipAddress;
	}

	// **** SBB LOCAL METHODS
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.LocationService#addBinding(java.lang.String, java.lang.String, java.lang.String, long, long, float, java.lang.String, long)
	 */
	public RegistrationBinding addBinding(String sipAddress,
			String contactAddress, String comment, long expires, long registrationDate,
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

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.LocationService#getRegisteredUsers()
	 */
	public Set<String> getRegisteredUsers() throws LocationServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getRegisteredUsers");
		}
		
		return locationService.getRegisteredUsers();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.LocationService#getBindings(java.lang.String)
	 */
	public Map<String, RegistrationBinding> getBindings(String sipAddress)
			throws LocationServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getBindings: sipAddress="+sipAddress);
		}
		
		return locationService.getBindings(sipAddress);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.LocationService#updateBinding(org.mobicents.slee.services.sip.location.RegistrationBinding)
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.LocationService#removeBinding(java.lang.String, java.lang.String)
	 */
	public void removeBinding(String sipAddress, String contactAddress)
			throws LocationServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("removeBinding: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
		
		try {
			// lookup null aci from aci naming facility, get timerid and cancel
			// timer (when present).
			ActivityContextInterface aci = activityContextNamingFacility
					.lookup(getACIName(contactAddress, sipAddress));
			if (aci != null) {
				timerFacility.cancelTimer(asSbbActivityContextInterface(aci)
						.getTimerID());
				activityContextNamingFacility.unbind(getACIName(contactAddress, sipAddress));
				// end null activity, detach is no good because this is a different
				// sbb entity then the one that create the binding
				((NullActivity)aci.getActivity()).endActivity();
			}
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		// remove from location service		
		locationService.removeBinding(sipAddress, contactAddress);
		
		if(logger.isInfoEnabled()) {
			logger.info("removed binding: sipAddress="+sipAddress+",contactAddress="+contactAddress);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.jmx.LocationServiceManagementMBean#getContacts(java.lang.String)
	 */
	public Set<String> getContacts(String sipAddress)
			throws LocationServiceException {
		return locationService.getContacts(sipAddress);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.jmx.LocationServiceManagementMBean#getExpirationTime(java.lang.String, java.lang.String)
	 */
	public long getExpirationTime(String sipAddress, String contactAddress)
			throws LocationServiceException {
		return locationService.getExpirationTime(sipAddress, contactAddress);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.services.sip.location.jmx.LocationServiceManagementMBean#getRegisteredUserCount()
	 */
	public int getRegisteredUserCount() throws LocationServiceException {
		return locationService.getRegisteredUserCount();
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
		String contactAddress = rgAci.getContactAddress();
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
