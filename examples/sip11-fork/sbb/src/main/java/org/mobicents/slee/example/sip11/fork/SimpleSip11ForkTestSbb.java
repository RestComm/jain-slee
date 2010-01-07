package org.mobicents.slee.example.sip11.fork;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.DialogForkedEvent;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

public abstract class SimpleSip11ForkTestSbb implements javax.slee.Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sipFactoryProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private TimerFacility timerFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;
	private ActivityContextNamingFacility acNamingFacility;
	private static Tracer logger;
	
	private static final long MSG_2_INVITE = 5000;
	private static final long ACK_2_BYE = 20000;
	private static final String TRANSPORT = "udp";
	private static final TimerOptions TIMER_OPTIONS = new TimerOptions();	
	
	public void onMessageRequest(RequestEvent event, ActivityContextInterface aci) {

		if (logger.isInfoEnabled()) {
			logger.info("onMessageRequest: activity = " + aci.getActivity());
		}
		
		final Request request = event.getRequest();
		final ServerTransaction st = event.getServerTransaction();
		
		if (getServiceState() == null) {
			
			setServiceState(ServiceState.RECEIVED_MSG);
			
			try {
				
				st.sendResponse(messageFactory.createResponse(200, request));

				if (request.getContentLength().getContentLength() != 0) {
					String content = new String(request.getRawContent());
					if (content.toLowerCase().contains("cancel"))
						setMakeCancel(Boolean.TRUE);
					else
						setMakeCancel(Boolean.FALSE);
				}
				setStoredRequest(request);
				
				NullActivity nullActivity = this.nullActivityFactory.createNullActivity();
				ActivityContextInterface nullAci = this.nullACIFactory.getActivityContextInterface(nullActivity);
				acNamingFacility.bind(nullAci, request.getHeader(CallIdHeader.NAME).toString());
				nullAci.attach(sbbContext.getSbbLocalObject());
				this.timerFacility.setTimer(nullAci, null, System.currentTimeMillis() + MSG_2_INVITE, TIMER_OPTIONS);
				
				setServiceState(ServiceState.INVITE_TIMER);
			} catch (Exception e) {
				logger.severe(e.getMessage(),e);
			}
		}
		else if (getServiceState() == ServiceState.INVITE_TIMER) {
			// This is retransmission, our OK didnt make it, lets send ok and leave it as
			// it is
			try {
				st.sendResponse(messageFactory.createResponse(200, request));
			} catch (Exception e) {
				logger.severe(e.getMessage(),e);
			}
		}
	}

	public void on2xxResponse(ResponseEvent event, ActivityContextInterface aci) {
		
		if (logger.isInfoEnabled()) {
			logger.info("on2xxResponse: activity = " + aci.getActivity() + " , event = " + event.getResponse().getStatusCode());
		}
		
		final Response response = event.getResponse();

		final CSeqHeader cSeqHeader = (CSeqHeader) event.getResponse().getHeader(CSeqHeader.NAME);
		final DialogActivity dialog = (DialogActivity) aci.getActivity();
		
		switch (getServiceState()) {
		case SENT_INVITE:

			if (cSeqHeader.getMethod().equals(Request.INVITE)) {
				try {
					dialog.sendAck(dialog.createAck(cSeqHeader.getSeqNumber()));
					setServiceState(ServiceState.SENT_ACK);
					timerFacility.setTimer(acNamingFacility.lookup(response.getHeader(CallIdHeader.NAME).toString()), null, System.currentTimeMillis() + ACK_2_BYE, TIMER_OPTIONS);
					setServiceState(ServiceState.BYE_TIMER);
				} catch (Throwable e) {
					logger.severe(e.getMessage(),e);
				}
			}
			break;

		case BYE_TIMER:
			// This is rtr?
			if (cSeqHeader.getMethod().equals(Request.INVITE)) {
				try {
					dialog.sendAck(dialog.createAck(cSeqHeader.getSeqNumber()));
				} catch (Throwable e) {
					logger.severe("Failed to send ack",e);
				}
			}
			break;
			
		}
	}

	public void onDialogForkEvent(DialogForkedEvent event, ActivityContextInterface aci) {
		
		if (logger.isInfoEnabled()) {
			logger.info("DialogForkedEvent: activity = " + aci.getActivity() + " , other = " + event.getForkedDialog() + " , event = " + event.getResponse().getStatusCode());
		}

		if (getServiceState() == ServiceState.SENT_INVITE) {
			ActivityContextInterface localAci;

			try {
				DialogActivity dialog = (DialogActivity) event.getForkedDialog();
				localAci = sipActivityContextInterfaceFactory.getActivityContextInterface(dialog);
				if (localAci.getActivity() != null) {
					localAci.attach(sbbContext.getSbbLocalObject());
					setChildDialog(localAci);
				}
				Boolean makeCancel = getMakeCancel();
				if (makeCancel != null && makeCancel.booleanValue()) {
					// We send cancel on this
					dialog.sendCancel();			
				}
			} catch (Throwable e) {
				logger.severe(e.getMessage(),e);
			}
		}
	}

	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Request request = ((RequestEvent) ies.getEvent()).getRequest();
		String callId = request.getHeader(CallIdHeader.NAME).toString();
		ies.setCustomName(callId);
		return ies;
	}

	public void onTimeEvent(TimerEvent event, ActivityContextInterface timerAci) {

		switch (getServiceState()) {
		case INVITE_TIMER:
			try {

				Request storedRequest = getStoredRequest();
				CallIdHeader callIdHeader = (CallIdHeader) storedRequest.getHeader(CallIdHeader.NAME);
				FromHeader fromHeader = (FromHeader) storedRequest.getHeader(FromHeader.NAME);
				ToHeader toHeader = (ToHeader) storedRequest.getHeader(ToHeader.NAME);
				// cheating... may not work outside mobicents
				DialogActivity dummyDialog = new DummyServerDialogActivity(callIdHeader,fromHeader.getAddress(),toHeader.getAddress()); 		
				DialogActivity dialog = sipFactoryProvider.getNewDialog(dummyDialog,true);
				
				Request request = dialog.createRequest(Request.INVITE);
				final String myUser = ((SipURI) toHeader.getAddress().getURI()).getUser();
				final ListeningPoint listeningPoint = sipFactoryProvider.getListeningPoint(TRANSPORT);
				SipURI contactURI = addressFactory.createSipURI(myUser,listeningPoint.getIPAddress());
				contactURI.setPort(listeningPoint.getPort());
				final Address contactAddress = addressFactory.createAddress(contactURI);
				contactAddress.setDisplayName(((SipURI) fromHeader.getAddress().getURI()).getUser());
				final ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
				request.addHeader(contactHeader);
				String sdpData = "v=0\r\n" + "o=4855 13760799956958020 13760799956958020" + " IN IP4  129.6.55.78\r\n" + "s=mysession session\r\n" + "p=+46 8 52018010\r\n"
						+ "c=IN IP4  129.6.55.78\r\n" + "t=0 0\r\n" + "m=audio 6022 RTP/AVP 0 4 18\r\n" + "a=rtpmap:0 PCMU/8000\r\n" + "a=rtpmap:4 G723/8000\r\n"
						+ "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n";
				byte[] contents = sdpData.getBytes();
				request.setContent(contents, headerFactory.createContentTypeHeader("application", "sdp"));
				
				ActivityContextInterface initialDialogAci = this.sipActivityContextInterfaceFactory.getActivityContextInterface(dialog);
				initialDialogAci.attach(sbbContext.getSbbLocalObject());
				setOriginalDialog(initialDialogAci);
				
				dialog.sendRequest(request);
			} catch (Exception e) {
				logger.severe("Exception when sending invite.",e);
			}
			setServiceState(ServiceState.SENT_INVITE);
			break;

		case BYE_TIMER:
			for (ActivityContextInterface aci : sbbContext.getActivities()) {
				Object activity = aci.getActivity();
				if (activity instanceof DialogActivity) {
					DialogActivity dialog = (DialogActivity) activity;
					logger.info("Sending BYE to attached dialog "+dialog);
					try {
						Request byeRequest = dialog.createRequest(Request.BYE);
						dialog.sendRequest(byeRequest);
					} catch (SipException e) {
						logger.severe(e.getMessage(),e);
					}
				}
				else if (activity instanceof NullActivity){
					((NullActivity)activity).endActivity();
				}
			}
			setServiceState(ServiceState.SENT_BYE);
		}

	}

	public void onActivityEndEvent(ActivityEndEvent end, ActivityContextInterface aci) {
		if (logger.isFineEnabled()) {
			logger.fine("  ---  ActivityEnd; Activity: " + aci.getActivity());

			logger.fine("Original ACI:" + getOriginalDialog());
			logger.fine("Original Activity:" + (getOriginalDialog() == null ? null : getOriginalDialog().getActivity()));
			logger.fine("Child ACI:" + getChildDialog());
			logger.fine("Child Activity:" + (getChildDialog() == null ? null : getChildDialog().getActivity()));
		}
	}

	// Other mid-dialog requests handled the same way as above
	// Helpers

	// other request handling methods
	
	// CMP field accessors for each Dialogs ACI
	public abstract void setOriginalDialog(ActivityContextInterface aci);

	public abstract ActivityContextInterface getOriginalDialog();

	public abstract void setChildDialog(ActivityContextInterface aci);

	public abstract ActivityContextInterface getChildDialog();

	public abstract Boolean getMakeCancel();

	public abstract void setMakeCancel(Boolean b);

	public abstract Request getStoredRequest();

	public abstract void setStoredRequest(Request b);

	public abstract ServiceState getServiceState();

	public abstract void setServiceState(ServiceState s);
	
	// lifecycle methods
	
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		if (logger == null) {
			logger = sbbContext.getTracer(SimpleSip11ForkTestSbb.class.getSimpleName());
		}
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");
			sipFactoryProvider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");
			addressFactory = sipFactoryProvider.getAddressFactory();
			headerFactory = sipFactoryProvider.getHeaderFactory();
			messageFactory = sipFactoryProvider.getMessageFactory();
			timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
			nullACIFactory = (NullActivityContextInterfaceFactory) ctx.lookup("slee/nullactivity/activitycontextinterfacefactory");
			nullActivityFactory = (NullActivityFactory) ctx.lookup("slee/nullactivity/factory");
			acNamingFacility = (ActivityContextNamingFacility) ctx.lookup("slee/facilities/activitycontextnaming");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	public void unsetSbbContext() { this.sbbContext = null; }
	public void sbbCreate() throws javax.slee.CreateException {}
	public void sbbPostCreate() throws javax.slee.CreateException {}
	public void sbbActivate() {}
	public void sbbPassivate() {}
	public void sbbRemove() {}
	public void sbbLoad() {}
	public void sbbStore() {}
	public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
	public void sbbRolledBack(RolledBackContext context) {}

	private SbbContext sbbContext; // This SBB's SbbContext

	public enum ServiceState {
		RECEIVED_MSG, INVITE_TIMER, SENT_INVITE, SENT_ACK, BYE_TIMER, SENT_BYE;
	}
}