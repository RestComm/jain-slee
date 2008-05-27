package org.mobicents.slee.service.user.delivery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Basic;
import javax.persistence.EntityManager;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.header.CallIdHeader;
import javax.sip.header.Header;
import javax.sip.message.Request;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;

import org.apache.log4j.Logger;
import org.mobicents.examples.convergeddemo.seam.pojo.Order;
import org.mobicents.media.server.impl.common.events.EventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsNotifyEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSignalGenerator;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
import org.mobicents.slee.resource.persistence.ratype.PersistenceResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.tts.ratype.TTSSession;
import org.mobicents.slee.service.callcontrol.CallControlSbbLocalObject;
import org.mobicents.slee.service.common.CommonSbb;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;

public abstract class OrderDeliverDateSbb extends CommonSbb {

	private Logger logger = Logger.getLogger(OrderDeliverDateSbb.class);

	private PersistenceResourceAdaptorSbbInterface persistenceResourceAdaptorSbbInterface = null;

	private MsProvider msProvider;

	private MediaRaActivityContextInterfaceFactory mediaAcif;

	private TimerFacility timerFacility = null;
	
	private final String orderDeliveryDate = "audio/UserOrderDeliveryDate.wav";

	String audioFilePath = null;

	String callerSip = null;

	/** Creates a new instance of UserSbb */
	public OrderDeliverDateSbb() {
		super();
	}

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");

			audioFilePath = System.getProperty("jboss.server.data.dir");

			callerSip = (String) myEnv.lookup("callerSip");

			persistenceResourceAdaptorSbbInterface = (PersistenceResourceAdaptorSbbInterface) myEnv
					.lookup("slee/resources/pra/0.1/provider");

			msProvider = (MsProvider) myEnv
					.lookup("slee/resources/media/1.0/provider");
			mediaAcif = (MediaRaActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/media/1.0/acifactory");

			timerFacility = (TimerFacility) myEnv
					.lookup("slee/facilities/timer");

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public void onOrderCancelled(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_CANCELLED ========");
		if (this.getTimerID() != null) {
			timerFacility.cancelTimer(this.getTimerID());
			ac.detach(getSbbContext().getSbbLocalObject());
		}
	}

	public void onOrderApproved(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_APPROVED ========");
		this.setCustomEvent(event);
		setTimer(ac);
	}

	public void onOrderProcessed(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_PROCESSED ========");
		this.setCustomEvent(event);
		setTimer(ac);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		makeCall();
	}

	private void setTimer(ActivityContextInterface ac) {
		TimerOptions options = new TimerOptions();
		options.setPersistent(true);

		// Set the timer on ACI
		TimerID timerID = this.timerFacility.setTimer(ac, null, System
				.currentTimeMillis() + 30000, options);

		this.setTimerID(timerID);
	}

	private void makeCall() {

		CustomEvent event = this.getCustomEvent();
		this.setDateAndTime("");

		try {
			// Set the caller address to the address of our call controller
			Address callerAddress = getSipUtils()
					.convertURIToAddress(callerSip);
			callerAddress.setDisplayName(callerSip);

			// Retrieve the callee addresses from the event
			Address calleeAddress = getSipUtils().convertURIToAddress(
					event.getCustomerPhone());

			// Build the INVITE request
			Request request = getSipUtils().buildInvite(callerAddress,
					calleeAddress, null, 1);

			// Create a new transaction based on the generated request
			ClientTransaction ct = getSipProvider().getNewClientTransaction(
					request);

			Header h = ct.getRequest().getHeader(CallIdHeader.NAME);
			String calleeCallId = ((CallIdHeader) h).getCallId();

			SessionAssociation sa = new SessionAssociation(
					"org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState");

			Session calleeSession = new Session(calleeCallId);
			calleeSession.setSipAddress(calleeAddress);
			calleeSession.setToBeCancelledClientTransaction(ct);

			// The dialog for the client transaction in which the INVITE is sent
			Dialog dialog = ct.getDialog();
			if (dialog != null && logger.isDebugEnabled()) {
				logger
						.debug("Obtained dialog from ClientTransaction : automatic dialog support on");
			}
			if (dialog == null) {
				// Automatic dialog support turned off
				try {
					dialog = getSipProvider().getNewDialog(ct);
					if (logger.isDebugEnabled()) {
						logger
								.debug("Obtained dialog for INVITE request to callee with getNewDialog");
					}
				} catch (Exception e) {
					logger.error("Error getting dialog", e);
				}
			}

			if (logger.isDebugEnabled()) {
				logger
						.debug("Obtained dialog in onThirdPCCTriggerEvent : callId = "
								+ dialog.getCallId().getCallId());
			}
			// Get activity context from factory
			ActivityContextInterface sipACI = getSipActivityContextInterfaceFactory()
					.getActivityContextInterface(dialog);

			ActivityContextInterface clientSipACI = getSipActivityContextInterfaceFactory()
					.getActivityContextInterface(ct);

			calleeSession.setDialog(dialog);
			sa.setCalleeSession(calleeSession);

			/**
			 * Actually callerSession is not required for this example and clean
			 * up is needed
			 */
			Session callerSession = new Session();

			// Create a new caller address from caller URI specified in the
			// event (the real caller address) since we need this in the next
			// INVITE.
			callerAddress = getSipUtils().convertURIToAddress(callerSip);
			callerSession.setSipAddress(callerAddress);
			// Since we don't have the client transaction for the caller yet,
			// just set the to be cancelled client transaction to null.
			callerSession.setToBeCancelledClientTransaction(null);
			sa.setCallerSession(callerSession);

			// put the callId for the callee dialog in the cache
			getCacheUtility().put(calleeCallId, sa);

			ChildRelation relation = getCallControlSbbChild();
			// Create child SBB
			CallControlSbbLocalObject child = (CallControlSbbLocalObject) relation
					.create();

			setChildSbbLocalObject(child);

			child.setParent(getSbbContext().getSbbLocalObject());

			// Attach child SBB to the activity context
			sipACI.attach(child);
			clientSipACI.attach(child);
			sipACI.attach(this.getSbbContext().getSbbLocalObject());
			// Send the INVITE request
			ct.sendRequest();

		} catch (ParseException parExc) {
			logger.error("Parse Exception while parsing the callerAddess",
					parExc);
		} catch (InvalidArgumentException invalidArgExcep) {
			logger.error(
					"InvalidArgumentException while building Invite Request",
					invalidArgExcep);
		} catch (TransactionUnavailableException tranUnavExce) {
			logger
					.error(
							"TransactionUnavailableException when trying to getNewClientTransaction",
							tranUnavExce);
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			logger
					.error(
							"UnrecognizedActivityException when trying to getActivityContextInterface",
							e);
		} catch (CreateException creaExce) {
			logger.error("CreateException while trying to create Child",
					creaExce);
		} catch (SipException sipExec) {
			logger.error("SipException while trying to send INVITE Request",
					sipExec);
		}

	}

	public void onLinkReleased(MsLinkEvent evt, ActivityContextInterface aci) {
		logger.info("-----onLinkReleased-----");

		if (this.getSendBye()) {
			getChildSbbLocalObject().sendBye();
		}
	}

	public void onAnnouncementComplete(MsNotifyEvent evt,
			ActivityContextInterface aci) {
		logger.info("Announcement complete: ");
		if (this.getSendBye()) {
			MsLink link = this.getLink();
			link.release();
		}
	}

	public void onLinkCreated(MsLinkEvent evt, ActivityContextInterface aci) {
		logger.info("--------onLinkCreated------------");
		MsLink link = evt.getSource();
		String announcementEndpoint = link.getEndpoints()[1];

		String endpointName = null;
		if (this.getEndpointName() == null) {
			this.setEndpointName(link.getEndpoints()[0]);
		}

		if (this.getAnnouncementEndpointName() == null) {
			this.setAnnouncementEndpointName(announcementEndpoint);
		}

		endpointName = this.getEndpointName();

		logger.info("endpoint name: " + endpointName);
		logger.info("Announcement endpoint: " + announcementEndpoint);

		MsSignalGenerator generator = msProvider
				.getSignalGenerator(announcementEndpoint);

		try {
			ActivityContextInterface generatorActivity = mediaAcif
					.getActivityContextInterface(generator);
			generatorActivity.attach(getSbbContext().getSbbLocalObject());

			String announcementFile = (getClass().getResource(orderDeliveryDate)).toString();
					
			generator.apply(EventID.PLAY,
					new String[] { announcementFile });

			//this.initDtmfDetector(getConnection(), endpointName);

		} catch (UnrecognizedActivityException e) {
			e.printStackTrace();
		}

	}

	public MsLink getLink() {
		ActivityContextInterface[] activities = getSbbContext().getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof MsLink) {
				return (MsLink) activities[i].getActivity();
			}
		}
		return null;
	}

	public void onInfoEvent(RequestEvent request, ActivityContextInterface aci) {
		System.out.println("onInfoEvent received");
		try {
			getSipUtils().sendStatefulOk(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Properties p = new Properties();
		try {
			p.load(new ByteArrayInputStream(request.getRequest()
					.getRawContent()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String dtmf = p.getProperty("Signal");
		System.out.println("The Dtmf is " + dtmf);

		int cause = Integer.parseInt(dtmf);

		boolean success = false;


		String dateAndTime = this.getDateAndTime();

		switch (cause) {
		case 0:
			dateAndTime = dateAndTime + "0";
			break;
		case 1:
			dateAndTime = dateAndTime + "1";
			break;
		case 2:
			dateAndTime = dateAndTime + "2";
			break;
		case 3:
			dateAndTime = dateAndTime + "3";
			break;
		case 4:
			dateAndTime = dateAndTime + "4";
			break;
		case 5:
			dateAndTime = dateAndTime + "5";
			break;
		case 6:
			dateAndTime = dateAndTime + "6";
			break;
		case 7:
			dateAndTime = dateAndTime + "7";
			break;
		case 8:
			dateAndTime = dateAndTime + "8";
			break;
		case 9:
			dateAndTime = dateAndTime + "9";
			break;
		default:
			break;
		}

		// TODO: Add logic to check if date and time is valid. We assume that
		// user is well educated and will always punch right date and time

		if (dateAndTime.length() == 10) {

			EntityManager mgr = null;
			Order order = null;
			
			StringBuffer audioPath = new StringBuffer(audioFilePath);
			audioPath.append("/");
			audioPath.append(this.getCustomEvent().getUserName());
			audioPath.append(".wav");			

			TTSSession ttsSession = getTTSProvider().getNewTTSSession(
					audioPath.toString(), "kevin");

			char[] c = dateAndTime.toCharArray();

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("You have selected delivery date to be ");

			String date = "" + c[0] + c[1];
			int iDate = (new Integer(date)).intValue();
			stringBuffer.append(iDate);

			String month = "" + c[2] + c[3];
			int iMonth = (new Integer(month)).intValue();

			String year = "" + c[4] + c[5];
			int iYear = (new Integer(year)).intValue();

			String hour = "" + c[6] + c[7];
			int iHour = (new Integer(hour)).intValue();

			String min = "" + c[8] + c[9];
			int iMin = (new Integer(min)).intValue();

			switch (iMonth) {
			case 1:
				month = "January";
				break;
			case 2:
				month = "February";
				break;
			case 3:
				month = "March";
				break;
			case 4:
				month = "April";
				break;
			case 5:
				month = "May";
				break;
			case 6:
				month = "June";
				break;
			case 7:
				month = "July";
				break;
			case 8:
				month = "August";
				break;
			case 9:
				month = "September";
				break;
			case 10:
				month = "October";
				break;
			case 11:
				month = "November";
				break;
			case 12:
				month = "December";
				break;
			default:
				break;
			}
			stringBuffer.append(" of ");
			stringBuffer.append(month);
			stringBuffer.append(" ");
			stringBuffer.append(2000 + iYear);
			stringBuffer.append(" at ");
			stringBuffer.append(iHour);
			stringBuffer.append(" hour and ");
			stringBuffer.append(iMin);
			stringBuffer.append(" minute. Thank you. Bye.");

			java.sql.Timestamp timeStamp = new java.sql.Timestamp(
					(iYear + 100), iMonth - 1, iDate, iHour, iMin, 0, 0);

			mgr = this.persistenceResourceAdaptorSbbInterface
					.createEntityManager(new HashMap(), "custom-pu");

			order = (Order) mgr
					.createQuery(
							"select o from Order o where o.orderId = :orderId")
					.setParameter("orderId", this.getCustomEvent().getOrderId())
					.getSingleResult();

			order.setDeliveryDate(timeStamp);

			mgr.flush();
			mgr.close();

			ttsSession.textToAudioFile(stringBuffer.toString());

			MsSignalGenerator generator = msProvider.getSignalGenerator(this
					.getAnnouncementEndpointName());

			try {
				ActivityContextInterface generatorActivity = mediaAcif
						.getActivityContextInterface(generator);
				generatorActivity.attach(getSbbContext().getSbbLocalObject());			
				

				
				generator.apply(EventID.PLAY,
						new String[] { "file:"+audioPath.toString() });

				//this.initDtmfDetector(getConnection(), this.getEndpointName());
			} catch (UnrecognizedActivityException e) {
				e.printStackTrace();
			}

			success = true;
			this.setSendBye(success);

		} else {
			this.setDateAndTime(dateAndTime);
			//this.initDtmfDetector(getConnection(), this.getEndpointName());
		}
	}

	private MsConnection getConnection() {
		ActivityContextInterface[] activities = getSbbContext().getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof MsConnection) {
				return (MsConnection) activities[i].getActivity();
			}
		}
		return null;
	}

	public InitialEventSelector orderIdSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		long orderId = 0;
		if (event instanceof CustomEvent) {
			orderId = ((CustomEvent) event).getOrderId();
		} else {
			// If something else, use activity context.
			ies.setActivityContextSelected(true);
			return ies;
		}
		// Set the convergence name
		if (logger.isDebugEnabled()) {
			logger.debug("Setting convergence name to: " + orderId);
		}
		ies.setCustomName(String.valueOf(orderId));
		return ies;
	}// child relation

	public abstract ChildRelation getCallControlSbbChild();

	public abstract void setEndpointName(String endPoint);

	public abstract String getEndpointName();

	public abstract void setAnnouncementEndpointName(String endPoint);

	public abstract String getAnnouncementEndpointName();

	public abstract void setCustomEvent(CustomEvent customEvent);

	public abstract CustomEvent getCustomEvent();

	public abstract void setSendBye(boolean isBye);

	public abstract boolean getSendBye();

	public abstract void setDateAndTime(String dateAndTime);

	public abstract String getDateAndTime();

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	// 'timerID' CMP field getter
	public abstract TimerID getTimerID();

	public abstract void setChildSbbLocalObject(
			CallControlSbbLocalObject childSbbLocalObject);

	public abstract CallControlSbbLocalObject getChildSbbLocalObject();
}
