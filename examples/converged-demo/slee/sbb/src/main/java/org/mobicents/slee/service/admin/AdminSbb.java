/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.service.admin;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;

import java.text.ParseException;

//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import javax.jms.JMSException;
//import javax.jms.MessageProducer;
//import javax.jms.Queue;
//import javax.jms.TextMessage;
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

import org.apache.log4j.Logger;
import org.mobicents.examples.convergeddemo.seam.pojo.Order;

import org.mobicents.slee.service.callcontrol.CallControlSbbLocalObject;
import org.mobicents.slee.service.common.CommonSbb;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;

/**
 * @author baranowb
 * @author amit bhayani
 */
public abstract class AdminSbb extends CommonSbb {

	private Tracer logger = null;

	private TimerFacility timerFacility = null;

	private EntityManagerFactory emf;

	private final String orderApproved = "audio/AdminOrderApproved.wav";
	private final String orderCancelled = "audio/AdminOrderCancelled.wav";
	private final String orderReConfirm = "audio/AdminReConfirm.wav";

	String audioFilePath = null;

	String callerSip = null;

	String adminSip = null;

	long waitingTime = 0;


	/** Creates a new instance of SecondBounceSbb */
	public AdminSbb() {
		super();
	}

	public void setSbbContext(SbbContext sbbContext) {
		logger = sbbContext.getTracer("Admin");
		super.setSbbContext(sbbContext);
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			audioFilePath = System.getProperty("jboss.server.data.dir") + "/RecordedAdmin.wav";

			callerSip = (String) ctx.lookup("callerSip");

			adminSip = (String) ctx.lookup("adminSip");

			waitingTime = ((Long) ctx.lookup("waitingTiming")).longValue();

			// Getting Timer Facility interface
			timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
			
			InitialContext newIc = new InitialContext();
			emf = (EntityManagerFactory) newIc.lookup("java:/ShoppingDemoSleeEntityManagerFactory");

		} catch (NamingException ne) {
			logger.severe("Could not set SBB context: " + ne.toString(), ne);
		}
	}

	public void onOrderPlaced(CustomEvent event, ActivityContextInterface ac) {

		logger.info("AdminSbb: " + this + ": received an ORDER_PLACED event. OrderId = " + event.getOrderId()
				+ ". ammount = " + event.getAmmount() + ". Customer Name = " + event.getCustomerName());

		this.setCustomEvent(event);
	
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(event.getCustomerName());
		stringBuffer.append(" has placed an order of $");
		stringBuffer.append(event.getAmmount());
		stringBuffer.append(". Press 1 to approve and 2 to reject.");

		setTtsString(stringBuffer.toString());
		this.setSendBye(false);
		makeCall(ac);

	}

	public void onOrderCancelled(CustomEvent event, ActivityContextInterface ac) {
		logger.info("****** AdminSbb Recieved ORDER_CANCELLED ******");
		cancelTimer();
	}

	public void onOrderRejected(CustomEvent event, ActivityContextInterface ac) {

		logger.info("****** AdminSbb Recieved ORDER_REJECTED ******* ");
		cancelTimer();
	}

	public void onOrderApproved(CustomEvent event, ActivityContextInterface ac) {

		logger.info("****** AdminSbb Recieved ORDER_APPROVED ******* ");
		cancelTimer();
	}

	public void onBeforeOrderProcessed(CustomEvent event, ActivityContextInterface ac) {

		logger.info("AdminSbb: " + this + ": received an BEFORE_ORDER_PROCESSED event. OrderId = " + event.getOrderId()
				+ ". ammount = " + event.getAmmount() + ". Customer Name = " + event.getCustomerName());

		this.setCustomEvent(event);
		

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(event.getCustomerName());
		stringBuffer.append(" has placed an order of $");
		stringBuffer.append(event.getAmmount());
		stringBuffer.append(". Press 1 to approve and 2 to reject.");

		setTtsString(stringBuffer.toString());

		setTimer(ac);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		logger.info("****** AdminSbb Recieved TimerEvent ******* ");
		// Detach NullActivity
		//aci.detach(this.getSbbContext().getSbbLocalObject());

		makeCall(aci);
	}

	/**
	 * This method sets the Timer Object for passed ACI
	 * 
	 * @param ac
	 */
	private void setTimer(ActivityContextInterface ac) {
		TimerOptions options = new TimerOptions();
		options.setPersistent(true);

		// Set the timer on ACI
		TimerID timerID = this.timerFacility.setTimer(ac, null, System.currentTimeMillis() + waitingTime, options);

		this.setTimerID(timerID);
	}

	private void cancelTimer() {
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

	private void makeCall(ActivityContextInterface ac) {

		try {
			// Set the caller address to the address of our call controller
			Address callerAddress = getSipUtils().convertURIToAddress(callerSip);
			callerAddress.setDisplayName(callerSip);

			// Retrieve the callee addresses from the event
			Address calleeAddress = getSipUtils().convertURIToAddress(adminSip);

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
			if (dialog != null && logger.isInfoEnabled()) {
				logger.info("Obtained dialog from ClientTransaction : automatic dialog support on");
			}
			if (dialog == null) {
				// Automatic dialog support turned off
				try {
					dialog = getSipProvider().getNewDialog(ct);
					if (logger.isInfoEnabled()) {
						logger.info("Obtained dialog for INVITE request to callee with getNewDialog");
					}
				} catch (Exception e) {
					logger.severe("Error getting dialog", e);
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("Obtained dialog in makeCall : callId = " + dialog.getCallId().getCallId());
			}
			// Get activity context from factory
			ActivityContextInterface sipACI = ((SipActivityContextInterfaceFactory)getSipActivityContextInterfaceFactory()).getActivityContextInterface((DialogActivity)dialog);

			ActivityContextInterface clientSipACI = ((SipActivityContextInterfaceFactory)getSipActivityContextInterfaceFactory())
					.getActivityContextInterface(ct);

			calleeSession.setDialog(dialog);
			sa.setCalleeSession(calleeSession);

			Session callerSession = new Session();

			// Create a new caller address from caller URI specified in the
			// event (the real caller address)
			// since we need this in the next INVITE.
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
			//ct.sendRequest();
			
			ac.detach(this.getSbbContext().getSbbLocalObject());
			dialog.sendRequest(ct);

		} catch (ParseException parExc) {
			logger.severe("Parse Exception while parsing the callerAddess", parExc);
		} catch (InvalidArgumentException invalidArgExcep) {
			logger.severe("InvalidArgumentException while building Invite Request", invalidArgExcep);
		} catch (TransactionUnavailableException tranUnavExce) {
			logger.severe("TransactionUnavailableException when trying to getNewClientTransaction", tranUnavExce);
		} catch (UnrecognizedActivityException e) {
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
			logger.info("The Announcement should start.");
			break;
		default:
			ReturnCode rc = event.getReturnCode();
			logger.severe("RQNT failed. Value = " + rc.getValue() + " Comment = " + rc.getComment());

			cancelTimer();
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
				// TODO : Send DLCX and Send BYE to UA
				if(getChildSbbLocalObject().getSendBye())
				{
					getChildSbbLocalObject().sendBye();
				}
				break;
			
			case MgcpEvent.DTMF_1:
				logger.info("You have pressed 1");
				//sendRQNT(DTMF_1, false);
				success = true;
				onDtmf1();
				break;
			case MgcpEvent.DTMF_2:
				logger.info("You have pressed 2");
				//sendRQNT(DTMF_2, false);
				success = true;
				onDtmf2();
				break;
			
			default:
				logger.info("Received undefined DTMF digit(Order approved?).");
				onDefaultDtmf();
				break;
			}
			
		}
		if(success)
		{
			setSendBye(success);
		}
	}
	
	private void onDtmf1() {

		EntityManager mgr = null;
		Order order = null;

		String audioFile = null;


		audioFile = (getClass().getResource(orderApproved)).toString();


			mgr = emf.createEntityManager();

			order = (Order) mgr
					.createQuery(
							"select o from Order o where o.orderId = :orderId")
					.setParameter("orderId", this.getCustomEvent().getOrderId())
					.getSingleResult();

			order.setStatus(Order.Status.PROCESSING);

			mgr.flush();
			mgr.close();
		
		
		getChildSbbLocalObject().sendRQNT(null, audioFile, true);
	}
	private void onDtmf2()
	{
		EntityManager mgr = null;
		Order order = null;
		String audioFile = null;

		
		audioFile = (getClass().getResource(orderCancelled)).toString();

			mgr = emf.createEntityManager();

			order = (Order) mgr.createQuery("select o from Order o where o.orderId = :orderId").setParameter(
					"orderId", this.getCustomEvent().getOrderId()).getSingleResult();

			order.setStatus(Order.Status.CANCELLED);

			mgr.flush();
			mgr.close();
	
		getChildSbbLocalObject().sendRQNT(null, audioFile, true);
	}
	private void onDefaultDtmf()
	{
		String audioFile = (getClass().getResource(orderReConfirm)).toString();
		getChildSbbLocalObject().sendRQNT(null, audioFile, false);
	}

	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci)
	{
		if(event.getSecondEndpointIdentifier()!=null)
		{
			//we have media path estabilished.
			getChildSbbLocalObject().sendRQNT(getTtsString(), null, true);
			this.setTtsString(null);
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
		if (logger.isInfoEnabled()) {
			logger.info("Setting convergence name to: " + orderId);
		}
		ies.setCustomName(String.valueOf(orderId));
		return ies;
	}

	// child relation
	public abstract ChildRelation getCallControlSbbChild();

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	// 'timerID' CMP field getter
	public abstract TimerID getTimerID();

	public abstract void setCustomEvent(CustomEvent customEvent);

	public abstract CustomEvent getCustomEvent();

	/**
	 * Set to true after dtmf - that is, we can terminate after anouncement which is played after dtmf?
	 * @param isBye
	 */
	public abstract void setSendBye(boolean isBye);

	public abstract boolean getSendBye();

	public abstract void setTtsString(String s);
	
	public abstract String getTtsString();

	public abstract void setChildSbbLocalObject(CallControlSbbLocalObject childSbbLocalObject);

	public abstract CallControlSbbLocalObject getChildSbbLocalObject();

}
