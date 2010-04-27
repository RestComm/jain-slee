package org.mobicents.slee.service.user.delivery;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
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
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.mobicents.examples.convergeddemo.seam.pojo.Order;

import org.mobicents.slee.service.callcontrol.CallControlSbbLocalObject;
import org.mobicents.slee.service.common.CommonSbb;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;

/**
 * 
 * @author amit.bhayani
 * @author baranowb
 */
public abstract class OrderDeliverDateSbb extends CommonSbb {

	private Tracer logger = null;

	private EntityManagerFactory emf;

	private TimerFacility timerFacility = null;

	private final String orderDeliveryDate = "audio/UserOrderDeliveryDate.wav";

	String audioFilePath = null;

	String callerSip = null;

	/** Creates a new instance of UserSbb */
	public OrderDeliverDateSbb() {
		super();
	}

	public void setSbbContext(SbbContext context) {
		this.logger = context.getTracer("OrderDeliverDate");
		super.setSbbContext(context);
		try {

			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");

			audioFilePath = System.getProperty("jboss.server.data.dir");

			callerSip = (String) myEnv.lookup("callerSip");

			timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");

			InitialContext newIc = new InitialContext();
			emf = (EntityManagerFactory) newIc.lookup("java:/ShoppingDemoSleeEntityManagerFactory");

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public void onOrderCancelled(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_CANCELLED ========");
		if (this.getTimerID() != null) {
			timerFacility.cancelTimer(this.getTimerID());
		}
		ActivityContextInterface[] activities = getSbbContext().getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof NullActivity) {
				activities[i].detach(this.getSbbContext().getSbbLocalObject());
			}
		}
	}

	public void onOrderApproved(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_APPROVED ========");
		this.setCustomEvent(event);
		setTimer(ac, 30000);
	}

	public void onOrderProcessed(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderDeliverDateSbb ORDER_PROCESSED ========");
		this.setCustomEvent(event);
		setTimer(ac, 30000);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		logger.info("Timer fired calling makeCall");

		// Detach NullActivity
		aci.detach(this.getSbbContext().getSbbLocalObject());
		makeCall();

	}

	private void setTimer(ActivityContextInterface ac, int duration) {
		TimerOptions options = new TimerOptions();
		options.setPersistent(true);

		// Set the timer on ACI
		TimerID timerID = this.timerFacility.setTimer(ac, null, System.currentTimeMillis() + duration, options);

		this.setTimerID(timerID);
	}

	private void makeCall() {

		CustomEvent event = this.getCustomEvent();
		this.setDateAndTime("");

		try {
			// Set the caller address to the address of our call controller
			Address callerAddress = getSipUtils().convertURIToAddress(callerSip);
			callerAddress.setDisplayName(callerSip);

			// Retrieve the callee addresses from the event
			Address calleeAddress = getSipUtils().convertURIToAddress(event.getCustomerPhone());

			// Build the INVITE request
			Request request = getSipUtils().buildInvite(callerAddress, calleeAddress, null, 1);

			// Create a new transaction based on the generated request
			ClientTransaction ct = getSipProvider().getNewClientTransaction(request);

			Header h = ct.getRequest().getHeader(CallIdHeader.NAME);
			String calleeCallId = ((CallIdHeader) h).getCallId();

			SessionAssociation sa = new SessionAssociation(
					"org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState");

			Session calleeSession = new Session(calleeCallId);
			calleeSession.setSipAddress(calleeAddress);
			calleeSession.setToBeCancelledClientTransaction(ct);

			// The dialog for the client transaction in which the INVITE is sent
			Dialog dialog = ct.getDialog();
			if (dialog != null && logger.isFineEnabled()) {
				logger.fine("Obtained dialog from ClientTransaction : automatic dialog support on");
			}
			if (dialog == null) {
				// Automatic dialog support turned off
				try {
					dialog = getSipProvider().getNewDialog(ct);
					if (logger.isFineEnabled()) {
						logger.fine("Obtained dialog for INVITE request to callee with getNewDialog");
					}
				} catch (Exception e) {
					logger.severe("Error getting dialog", e);
				}
			}

			if (logger.isFineEnabled()) {
				logger.fine("Obtained dialog in onThirdPCCTriggerEvent : callId = " + dialog.getCallId().getCallId());
			}
			// Get activity context from factory
			ActivityContextInterface sipACI = ((SipActivityContextInterfaceFactory)getSipActivityContextInterfaceFactory()).getActivityContextInterface((DialogActivity)dialog);

			ActivityContextInterface clientSipACI = ((SipActivityContextInterfaceFactory)getSipActivityContextInterfaceFactory())
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
			CallControlSbbLocalObject child = (CallControlSbbLocalObject) relation.create();

			setChildSbbLocalObject(child);

			child.setParent(getSbbContext().getSbbLocalObject());

			// Attach child SBB to the activity context
			sipACI.attach(child);
			clientSipACI.attach(child);
			sipACI.attach(this.getSbbContext().getSbbLocalObject());
			// Send the INVITE request
			ct.sendRequest();

		} catch (ParseException parExc) {
			logger.severe("Parse Exception while parsing the callerAddess", parExc);
		} catch (InvalidArgumentException invalidArgExcep) {
			logger.severe("InvalidArgumentException while building Invite Request", invalidArgExcep);
		} catch (TransactionUnavailableException tranUnavExce) {
			logger.severe("TransactionUnavailableException when trying to getNewClientTransaction", tranUnavExce);
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			logger.severe("UnrecognizedActivityException when trying to getActivityContextInterface", e);
		} catch (CreateException creaExce) {
			logger.severe("CreateException while trying to create Child", creaExce);
		} catch (SipException sipExec) {
			logger.severe("SipException while trying to send INVITE Request", sipExec);
		}

	}
	
	public void onNotificationRequestResponse(NotificationRequestResponse event, ActivityContextInterface aci) {
		logger.info("onNotificationRequestResponse");

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
			logger.info("The Announcement should have been started");
			break;
		default:
			ReturnCode rc = event.getReturnCode();
			logger.severe("RQNT failed. Value = " + rc.getValue() + " Comment = " + rc.getComment());

			if(getChildSbbLocalObject().getSendBye())
			{
				getChildSbbLocalObject().sendBye();
			}
			
			break;
		}

	}
	
	
	public void onNotifyRequest(Notify event, ActivityContextInterface aci) {
		logger.info("onNotifyRequest");

		NotificationRequestResponse response = new NotificationRequestResponse(event.getSource(),
				ReturnCode.Transaction_Executed_Normally);
		response.setTransactionHandle(event.getTransactionHandle());

		((JainMgcpProvider)getMgcpProvider()).sendMgcpEvents(new JainMgcpEvent[] { response });

		EventName[] observedEvents = event.getObservedEvents();

		boolean success = false;
		for (EventName observedEvent : observedEvents) {
			switch (observedEvent.getEventIdentifier().intValue()) {
			case MgcpEvent.REPORT_ON_COMPLETION:
				logger.info("Announcemnet Completed NTFY received");
				if(this.getSendBye() && getChildSbbLocalObject().getSendBye())
				{
					getChildSbbLocalObject().sendBye();
				}
				break;
			case MgcpEvent.REPORT_FAILURE:
				logger.info("Announcemnet Failed received");

				if(getChildSbbLocalObject().getSendBye())
				{
					getChildSbbLocalObject().sendBye();
				}
				break;
			
			case MgcpEvent.DTMF_0:
				logger.info("You have pressed 0");
			case MgcpEvent.DTMF_1:
				logger.info("You have pressed 1");
			case MgcpEvent.DTMF_2:
				logger.info("You have pressed 2");
			case MgcpEvent.DTMF_3:
				logger.info("You have pressed 3");
			case MgcpEvent.DTMF_4:
				logger.info("You have pressed 4");
			case MgcpEvent.DTMF_5:
				logger.info("You have pressed 5");
			case MgcpEvent.DTMF_6:
				logger.info("You have pressed 6");
			case MgcpEvent.DTMF_7:
				logger.info("You have pressed 7");
			case MgcpEvent.DTMF_8:
				logger.info("You have pressed 8");
			case MgcpEvent.DTMF_9:
				logger.info("You have pressed 9");
			
				processDtmf(observedEvent);
				
			default:
				logger.info("Received undefined event.");

				break;
			}
			
		}
		if(success)
		{
			setSendBye(success);
		}
	}
	private void processDtmf(EventName event) {
		// TODO Auto-generated method stub
		String dateAndTime = this.getDateAndTime();
		boolean success = false;
		
		switch (event.getEventIdentifier().intValue()) {
		case MgcpEvent.DTMF_0:
			dateAndTime = dateAndTime + "0";
			break;
		case MgcpEvent.DTMF_1:
			dateAndTime = dateAndTime + "1";
			break;
		case MgcpEvent.DTMF_2:
			dateAndTime = dateAndTime + "2";
			break;
		case MgcpEvent.DTMF_3:
			dateAndTime = dateAndTime + "3";
			break;
		case MgcpEvent.DTMF_4:
			dateAndTime = dateAndTime + "4";
			break;
		case MgcpEvent.DTMF_5:
			dateAndTime = dateAndTime + "5";
			break;
		case MgcpEvent.DTMF_6:
			dateAndTime = dateAndTime + "6";
			break;
		case MgcpEvent.DTMF_7:
			dateAndTime = dateAndTime + "7";
			break;
		case MgcpEvent.DTMF_8:
			dateAndTime = dateAndTime + "8";
			break;
		case MgcpEvent.DTMF_9:
			dateAndTime = dateAndTime + "10";
			break;

		default:
			logger.info("Received undefined DTMF digit().");

			break;
		}
		if (dateAndTime.length() == 10) {

			EntityManager mgr = null;
			Order order = null;
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

			mgr = emf.createEntityManager();

			order = (Order) mgr
					.createQuery(
							"select o from Order o where o.orderId = :orderId")
					.setParameter("orderId", this.getCustomEvent().getOrderId())
					.getSingleResult();

			order.setDeliveryDate(timeStamp);

			mgr.flush();
			mgr.close();
			
			success = true;
			getChildSbbLocalObject().sendRQNT(stringBuffer.toString(), null, false);
		}else
		{
			setDateAndTime(dateAndTime);
		}
		
		if(success)
		{
			this.setSendBye(success);
		}
	}

	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci)
	{
		if(event.getSecondEndpointIdentifier()!=null)
		{
			String announcementFile = (getClass().getResource(orderDeliveryDate)).toString();
			//we have media path estabilished.
			getChildSbbLocalObject().sendRQNT(null, announcementFile, true);
		}
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
		if (logger.isFineEnabled()) {
			logger.severe("Setting convergence name to: " + orderId);
		}
		ies.setCustomName(String.valueOf(orderId));
		return ies;
	}// child relation

	public abstract ChildRelation getCallControlSbbChild();

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

	public abstract void setChildSbbLocalObject(CallControlSbbLocalObject childSbbLocalObject);

	public abstract CallControlSbbLocalObject getChildSbbLocalObject();

}
