package org.mobicents.slee.services.sip.location.cache;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.services.sip.common.LocationInterface;
import org.mobicents.slee.services.sip.common.LocationServiceException;
import org.mobicents.slee.services.sip.common.RegistrationBinding;
import org.mobicents.slee.services.sip.location.cache.mbean.LocationServiceMBean;

public abstract class LocationSbb implements Sbb, LocationInterface {

	private static Logger logger = Logger.getLogger(LocationSbb.class
			.getCanonicalName());

	private SbbContext ctx = null;
	private TimerFacility timerFacility;

	private AlarmFacility alarmFacility;

	private ActivityContextNamingFacility namingFacility;

	private SbbID id;

	private NullActivityFactory nullActivityFactory;

	private NullActivityContextInterfaceFactory nullACIFactory;

	private Context myEnv;

	private static TimerOptions defaultTimerOptions = new TimerOptions(true,
			500, TimerPreserveMissed.LAST);

	// **************************************************** STATICS - JNDI NAMES
	private static final String JNDI_SERVICEACTIVITY_FACTORY = "java:comp/env/slee/serviceactivity/factory";

	private static final String JNDI_SERVICEACTIVITYACI_FACTORY = "java:comp/env/slee/serviceactivity/activitycontextinterfacefactory";

	private static final String JNDI_NULL_ACTIVITY_FACTORY = "java:comp/env/slee/nullactivity/factory";

	private static final String JNDI_NULL_ACI_FACTORY = "java:comp/env/slee/nullactivity/activitycontextinterfacefactory";

	private static final String JNDI_ACTIVITY_CONTEXT_NAMING_FACILITY = "java:comp/env/slee/facilities/activitycontextnaming";

	private static final String JNDI_TRACE_FACILITY = "java:comp/env/slee/facilities/trace";

	private static final String JNDI_TIMER_FACILITY_NAME = "java:comp/env/slee/facilities/timer";

	private static final String JNDI_ALARM_FACILITY_NAME = "java:comp/env/slee/facilities/alarm";

	private static final String JNDI_PROFILE_FACILITY_NAME = "java:comp/env/slee/facilities/profile";

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

	public abstract CacheLocationActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

	public void setSbbContext(SbbContext ctx) {
		this.ctx = ctx;

		try {
			id = ctx.getSbb();

			myEnv = new InitialContext();

			timerFacility = (TimerFacility) myEnv
					.lookup(JNDI_TIMER_FACILITY_NAME);
			alarmFacility = (AlarmFacility) myEnv
					.lookup(JNDI_ALARM_FACILITY_NAME);

			namingFacility = (ActivityContextNamingFacility) myEnv
					.lookup(JNDI_ACTIVITY_CONTEXT_NAMING_FACILITY);
			nullACIFactory = (NullActivityContextInterfaceFactory) myEnv
					.lookup(JNDI_NULL_ACI_FACTORY);
			nullActivityFactory = (NullActivityFactory) myEnv
					.lookup(JNDI_NULL_ACTIVITY_FACTORY);

		} catch (NamingException ne) {
			logger.log(java.util.logging.Level.WARNING,
					"Could not set SBB context: ", ne);
		}

	}

	public void unsetSbbContext() {
		this.ctx = null;

	}

	// **** SBB LOCAL METHODS

	public RegistrationBinding addUserLocation(String sipAddress,
			String contactAddress, String comment, long expiresDelta, float q,
			String id, long seq) throws LocationServiceException {

		RegistrationBindingImpl bind = new RegistrationBindingImpl(
				contactAddress, "", expiresDelta, q, id, seq);

		LocationService ls = new LocationService();

		Map bindings = ls.getBindings(sipAddress);
		ActivityContextInterface _aci = null;
		if (bindings != null && bindings.containsKey(contactAddress)) {
			// We already have null activity by now
			// We have to cancel timer
			ActivityContextInterface[] acis = ctx.getActivities();

			for (ActivityContextInterface aci : acis) {
				if (aci.getActivity() instanceof NullActivity) {
					_aci = aci;
					CacheLocationActivityContextInterface local = asSbbActivityContextInterface(aci);

					if (local.getTimerID() != null) {
						try {
							timerFacility.cancelTimer(local.getTimerID());
						} catch (Exception e) {
							e.printStackTrace();
						}

						local.setTimerID(null);
					}
					break;
				}
			}
		}

		ls.addBinding(sipAddress, bind);

		this.setRegistrationTimer(sipAddress, contactAddress, expiresDelta, id,
				seq, _aci);

		return bind;
	}

	public Set<String> getRegisteredUsers() {

		return new LocationService().getRegistered();
	}

	public Map<String, RegistrationBinding> getUserBindings(String sipAddress)
			throws LocationServiceException {

		return new LocationService().getBindings(sipAddress);
	}


	public void removeBinding(String sipAddress, String contactAddress)
			throws LocationServiceException {
		LocationService ls = new LocationService();

		Map bindings = ls.getBindings(sipAddress);
		if (bindings.isEmpty() || !bindings.containsKey(contactAddress))
			throw new LocationServiceException("No such contanct!!!!");

		ls.removeBinding(sipAddress, contactAddress);

	}

	/**
	 * Set a timer on a registration entry. If a timer is already set for this
	 * registration, reset it to the new timeout value. <br>
	 * <b>THIS IS SLEE DEPENDANT METHOD</b>
	 * 
	 * @param sipAddress
	 *            the public SIP address-of-record for the user
	 * @param sipContactAddress
	 *            the physical contact address for this registration
	 * @param timeout
	 *            expiry time (in seconds) of the registration
	 * @param callId
	 *            the SIP callid of the REGISTER request
	 * @param cseq
	 *            the SIP sequence number of the REGISTER request
	 */
	void setRegistrationTimer(String sipAddress, String sipContactAddress,
			long timeout, String callId, long cseq, ActivityContextInterface aci) {
		// first find out if we already have a timer set for this registration,
		// and if so, cancel it.
		logger.finest("setRegistrationTimer(" + sipAddress + ", "
				+ sipContactAddress + ", " + timeout + ", " + callId + ", "
				+ cseq + ")");

		try {
			// set a one-shot timer. when it fires we expire the registration
			long expireTime = System.currentTimeMillis() + (timeout * 1000);

			// Create new ACI for this timer
			ActivityContextInterface nullACI = null;
			if (aci == null) {
				NullActivity nullAC = nullActivityFactory.createNullActivity();
				nullACI = nullACIFactory.getActivityContextInterface(nullAC);
			} else
				nullACI = aci;
			CacheLocationActivityContextInterface regACI = asSbbActivityContextInterface(nullACI);
			regACI.setSipAddress(sipAddress);
			regACI.setSipContactAddress(sipContactAddress);
			// callId and cseq used to identify a particular registration
			regACI.setCallId(callId);
			regACI.setCSeq(cseq);

			// attach so we will receive the timer event...
			regACI.attach(ctx.getSbbLocalObject());

			TimerOptions timerOpts = new TimerOptions();
			timerOpts.setPersistent(true);

			TimerID newTimer = timerFacility.setTimer(regACI, null, expireTime,
					timerOpts);
			regACI.setTimerID(newTimer);
			logger.fine("set new timer for registration: " + sipAddress
					+ " -> " + sipContactAddress + ", expires in " + timeout
					+ "s");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Expire registration entry, remove it from location service. This would be
	 * a callback from whatever timer is set in setRegistrationExpiry() above.
	 * Only remove a registration if the callId and cseq values match those of
	 * the original registration. If the values don't match, this means the
	 * registration has been updated by a more recent REGISTER request, so we
	 * should not change anything. The timer for the most recent REGISTER
	 * request will expire the entry.
	 * 
	 * @param sipAddress
	 *            the public SIP address-of-record for the user
	 * @param sipContactAddress
	 *            the physical contact address for this registration
	 * @param callId
	 *            the SIP callid of the REGISTER request
	 * @param cseq
	 *            the SIP sequence number of the REGISTER request
	 */
	void expireRegistration(String sipAddress, String sipContactAddress,
			String callId, long cseq) {
		// find user in location service
		if (logger.isLoggable(Level.FINEST))
			logger.finest("Expire on[" + sipAddress + "][" + sipContactAddress
					+ "][" + callId + "] [" + cseq + "]");
		try {
			LocationService ls = new LocationService();
			Map bindings = ls.getBindings(sipAddress);
			if (bindings == null) {
				logger.warning("expireRegistration: user " + sipAddress
						+ " not found.");
				return;
			}

			// remove binding for sipContactAddress, if callId and cseq match.
			RegistrationBinding binding = (RegistrationBinding) bindings
					.get(sipContactAddress);
			if (binding == null) {
				if (logger.isLoggable(Level.FINEST))
					logger.warning("expireRegistration: registration for "
							+ sipAddress + " -> " + sipContactAddress
							+ " not found.");
				return;
			}

			if (callId.equals(binding.getCallId())
					&& (cseq == binding.getCSeq())) {
				// this is my registration, so I am allowed to remove it
				ls.removeBinding(sipAddress, sipContactAddress);

				// set bindings
				if (logger.isLoggable(Level.FINEST))
					logger.finest("expireRegistration: removed binding: "
							+ sipAddress + " -> " + sipContactAddress);
			} else { // not my registration, do nothing
				if (logger.isLoggable(Level.FINEST))
					logger
							.finest("expireRegistration: callId/cseq for binding ("
									+ sipAddress
									+ " -> "
									+ sipContactAddress
									+ ") has been updated, not removing");
			}

		} catch (LocationServiceException lse) {
			lse.printStackTrace();
		}

	}

	public void onBindingTimeout(TimerEvent timer, ActivityContextInterface aci) {

		CacheLocationActivityContextInterface local = asSbbActivityContextInterface(aci);

		expireRegistration(local.getSipAddress(), local.getSipContactAddress(),
				local.getCallId(), local.getCSeq());
		aci.detach(ctx.getSbbLocalObject());

	}

	// ********** DEBUG

	static class DebugTimerTask extends TimerTask {

		@Override
		public void run() {
			LocationService ls = new LocationService();

			System.out.println("=-=-=Registered users:");
			try {
				SleeContainer.getTransactionManager().begin();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int i = 0;
			for (Object user : ls.getRegistered()) {
				System.out.println("[" + i++ + "]" + user);
				try {
					Map bindings = ls.getBindings((String) user);
					if (bindings.isEmpty()) {
						System.out.println("THIS SHOULD NOT HAPPEN");
						continue;
					}

					int j = 0;
					for (Object binding : bindings.values()) {
						System.out.println("---[" + j++ + "]" + binding);
					}
				} catch (LocationServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			try {
				SleeContainer.getTransactionManager().commit();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void onServiceStarted(
			javax.slee.serviceactivity.ServiceStartedEvent serviceEvent,
			ActivityContextInterface aci) {
		
		aci.detach(this.ctx.getSbbLocalObject());
		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("java:comp/env/slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				startMBeanConfigurator();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
	
	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		try {
			Object activity = aci.getActivity();
			if (activity instanceof ServiceActivity) {
				Context myEnv = (Context) new InitialContext()
						.lookup("java:comp/env");
				// check if it's my service aci that is ending
				ServiceActivity sa = ((ServiceActivityFactory) myEnv
						.lookup("slee/serviceactivity/factory")).getActivity();
				if (sa.equals(activity)) {
					logger.finest("Service aci ending, removing mbean");
					// lets remove our mbean
					clearENV();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void startMBeanConfigurator() {

		org.mobicents.slee.services.sip.location.cache.mbean.LocationService lsmb = new org.mobicents.slee.services.sip.location.cache.mbean.LocationService();


		String confValue = null;
		Context myEnv = null;
		try {
			logger.info("Building Configuration from ENV Entries");
			myEnv = (Context) new InitialContext().lookup("java:comp/env");
			String configurationName = (String) myEnv.lookup("configuration-MBEAN");
			if(configurationName!=null)
				lsmb.setName(configurationName);
		} catch (NamingException ne) {

			logger.warning("Could not set SBB context:" + ne.getMessage());
			return;
		}
		
		// GO ;] start service
		lsmb.startService();

	}

	protected void clearENV() {

		try {
			// if (getConfiguration() != null)
			// try {
			logger.info("Clearing environment, removing mbean");
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");

			// env-entries
			String configurationName = (String) myEnv.lookup("configuration-MBEAN");

			MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
			ObjectName on = new ObjectName(LocationServiceMBean.MBEAN_NAME_PREFIX
					+ configurationName);
			mbs.unregisterMBean(on);
			// } catch (NamingException ne) {
			// logger.warning("Could not set SBB context:"
			// + ne.getMessage());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			// This will happen if event is ServiceStart ????
		}
	}
	
	private static Timer tmpTimer = new Timer();

	static {

		// tmpTimer.scheduleAtFixedRate(new DebugTimerTask(), 5000, 5000);

	}

}
