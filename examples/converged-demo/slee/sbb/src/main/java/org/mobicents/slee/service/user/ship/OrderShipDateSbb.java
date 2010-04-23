package org.mobicents.slee.service.user.ship;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;

import java.sql.Timestamp;
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
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.apache.log4j.Logger;
import org.mobicents.examples.convergeddemo.seam.pojo.Order;
//XXX
//import org.mobicents.mscontrol.MsEndpoint;
//import org.mobicents.mscontrol.MsLink;
//import org.mobicents.mscontrol.MsLinkEvent;
//import org.mobicents.mscontrol.MsNotifyEvent;
//import org.mobicents.mscontrol.MsProvider;
//import org.mobicents.mscontrol.events.MsEventAction;
//import org.mobicents.mscontrol.events.MsEventFactory;
//import org.mobicents.mscontrol.events.MsRequestedEvent;
//import org.mobicents.mscontrol.events.MsRequestedSignal;
//import org.mobicents.mscontrol.events.ann.MsPlayRequestedSignal;
//import org.mobicents.mscontrol.events.pkg.MsAnnouncement;
//import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
//import org.mobicents.slee.resource.tts.ratype.TTSSession;
import org.mobicents.slee.service.callcontrol.CallControlSbbLocalObject;
import org.mobicents.slee.service.common.CommonSbb;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;
/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public abstract class OrderShipDateSbb extends CommonSbb {

	private Tracer logger = null;

	private EntityManagerFactory emf;


	String callerSip = null;

	/** Creates a new instance of UserSbb */
	public OrderShipDateSbb() {
		super();
	}

	public void setSbbContext(SbbContext context) {
		this.logger = context.getTracer("OrderShipDate");
		super.setSbbContext(context);
		try {

			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");


			callerSip = (String) myEnv.lookup("callerSip");
			
			InitialContext newIc = new InitialContext();
			emf = (EntityManagerFactory) newIc.lookup("java:/ShoppingDemoSleeEntityManagerFactory");

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public void onOrderShipped(CustomEvent event, ActivityContextInterface ac) {
		logger.info("======== OrderShipDateSbb ORDER_SHIPPED ========");
		ac.detach(this.getSbbContext().getSbbLocalObject());
		makeCall(event, ac);
	}

	private void makeCall(CustomEvent event, ActivityContextInterface ac) {

		EntityManager mgr = null;
		Order order = null;

		this.setCustomEvent(event);


		mgr = emf.createEntityManager();

		order = (Order) mgr.createQuery("select o from Order o where o.orderId = :orderId").setParameter("orderId",
				this.getCustomEvent().getOrderId()).getSingleResult();

		Timestamp orderDate = order.getDeliveryDate();

		mgr.close();

		

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Welcome ");
		stringBuffer.append(event.getCustomerName());
		stringBuffer.append(". This is a reminedr call for your order number ");
		stringBuffer.append(event.getOrderId());
		stringBuffer.append(". The shippment will be at your door step on .");
		stringBuffer.append(orderDate.getDate());
		stringBuffer.append(" of ");

		String month = null;

		switch (orderDate.getMonth()) {
		case 0:
			month = "January";
			break;
		case 1:
			month = "February";
			break;
		case 2:
			month = "March";
			break;
		case 3:
			month = "April";
			break;
		case 4:
			month = "May";
			break;
		case 5:
			month = "June";
			break;
		case 6:
			month = "July";
			break;
		case 7:
			month = "August";
			break;
		case 8:
			month = "September";
			break;
		case 9:
			month = "October";
			break;
		case 10:
			month = "November";
			break;
		case 11:
			month = "December";
			break;
		default:
			break;
		}

		stringBuffer.append(month);
		stringBuffer.append(" ");
		stringBuffer.append(1900 + orderDate.getYear());
		stringBuffer.append(" at ");
		stringBuffer.append(orderDate.getHours());
		stringBuffer.append(" hour and ");
		stringBuffer.append(orderDate.getMinutes());
		stringBuffer.append(" minute. Thank you. Bye.");

		setTtsString(stringBuffer.toString());

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

	
	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci)
	{
		if(event.getSecondEndpointIdentifier()!=null)
		{
			//we have media path estabilished.
			getChildSbbLocalObject().sendRQNT(getTtsString(), null, true);
			this.setTtsString(null);
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

		for (EventName observedEvent : observedEvents) {
			switch (observedEvent.getEventIdentifier().intValue()) {
			case MgcpEvent.REPORT_ON_COMPLETION:
				logger.info("Announcemnet Completed NTFY received");
				if(getChildSbbLocalObject().getSendBye())
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
			
			
			
			default:
				logger.info("Received undefined event.");

				break;
			}
			
		}
		
	}
	
	// child relation
	public abstract ChildRelation getCallControlSbbChild();

	public abstract void setCustomEvent(CustomEvent customEvent);

	public abstract CustomEvent getCustomEvent();

	public abstract void setChildSbbLocalObject(CallControlSbbLocalObject childSbbLocalObject);

	public abstract CallControlSbbLocalObject getChildSbbLocalObject();
	
	public abstract void setTtsString(String s);
	
	public abstract String getTtsString();
}
