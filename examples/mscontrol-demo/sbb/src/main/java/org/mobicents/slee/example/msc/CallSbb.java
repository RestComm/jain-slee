/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.example.msc;

import java.net.URI;
import java.text.ParseException;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.Joinable.Direction;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.media.mscontrol.networkconnection.SdpPortManagerException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.Tracer;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.resource.mediacontrol.MscActivityContextInterfaceFactory;

/**
 * 
 * @author kulikov
 */
public class CallSbb implements Sbb {

	public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
	public final static String WELCOME = "http://" + JBOSS_BIND_ADDRESS + ":8080/mscontrol/audio/RQNT-ULAW.wav";

	public final static String ENDPOINT_NAME = "/mobicents/media/IVR/$";

	protected static int CALL_ID_GEN = 1;
	protected static int GEN = 1000;
	protected SbbContext sbbContext;
	protected Tracer tracer;
	protected SleeSipProvider sipProvider;
	protected AddressFactory addressFactory;
	protected HeaderFactory headerFactory;
	protected MessageFactory messageFactory;
	protected SipActivityContextInterfaceFactory acif;
	private MsControlFactory mscFactory;
	private MscActivityContextInterfaceFactory mscAcifFactory;

	public void onInvite(RequestEvent event, ActivityContextInterface aci) {
		tracer.info("Receive call ");

		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		
		Request request = event.getRequest();

		// sending provisional response to the UA which indiactes that initial
		// request
		// successfuly reach call controller and is going to be handled
		try {
			Response response = messageFactory.createResponse(Response.TRYING, request);
			event.getServerTransaction().sendResponse(response);
		} catch (Exception e) {
			// Can not send provisional response? Forget about this request.
			return;
		}

		// Provisional response sent so possible to obtain SIP Dialog activity
		// and attach this
		// SBB to the Dialog activity. Dialog activity can be used to maintain
		// current state too.
		ActivityContextInterface callActivity = null;
		try {
			Dialog dialog = sipProvider.getNewDialog(event.getServerTransaction());
			dialog.terminateOnBye(true);
			callActivity = acif.getActivityContextInterface((DialogActivity) dialog);
			callActivity.attach(sbbLocalObject);
		} catch (Exception e) {
			// oops, this is unexpected core problem. there is only one way -
			// terminate call
			tracer.severe("Unexpected error", e);
			reject();
			return;
		}
		MediaSession session = null;
		try {
			session = mscFactory.createMediaSession();
		} catch (MsControlException e) {
			tracer.severe("Unexpected error", e);
			reject();
			return;
		}

		tracer.info("Created media session: " + session);
		NetworkConnection connection = null;
		try {
			connection = session.createNetworkConnection(NetworkConnection.BASIC);
		} catch (MsControlException e) {
			tracer.severe("Unexpected error", e);
			reject();
			return;
		}

		tracer.info("Created network connection: " + connection);
		// creating media connection activity context interface
		ActivityContextInterface activityContextInterface = null;
		try {
			activityContextInterface = mscAcifFactory.getActivityContextInterface(connection);
			activityContextInterface.attach(sbbLocalObject);
		} catch (Exception e) {
			tracer.severe("Unexpected error", e);
			reject();
			return;
		}

		SdpPortManager sdpManager = null;
		try {
			sdpManager = connection.getSdpPortManager();
		} catch (MsControlException e) {
		}

		tracer.info("SDP Manager: " + sdpManager);
		try {
			sdpManager.processSdpOffer((byte[]) request.getContent());
			tracer.info("SDP Manager: sent process offer request");
		} catch (SdpPortManagerException e) {
		}
	}

	public void onStreamFailure(SdpPortManagerEvent evt, ActivityContextInterface aci) {
		tracer.info("Failed to process SDP:");
		reject();
	}

	public void onAnswerGenerated(SdpPortManagerEvent evt, ActivityContextInterface aci) {
		tracer.info("Received SDP answer:");
		byte[] sdp = evt.getMediaServerSdp();

		ContentTypeHeader contentType = null;
		try {
			contentType = headerFactory.createContentTypeHeader("application", "sdp");
		} catch (ParseException ex) {
		}

		String localAddress = sipProvider.getListeningPoints()[0].getIPAddress();
		int localPort = sipProvider.getListeningPoints()[0].getPort();

		Address contactAddress = null;
		try {
			contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
		} catch (ParseException ex) {
		}
		ContactHeader contact = headerFactory.createContactHeader(contactAddress);

		try {
			ActivityContextInterface serverTxACI = getServerTransactionACI();
			serverTxACI.detach(sbbContext.getSbbLocalObject());
			ServerTransaction st = (ServerTransaction) serverTxACI.getActivity();
			Response ok = messageFactory.createResponse(Response.OK, st.getRequest(), contentType, sdp);
			ok.setHeader(contact);
			st.sendResponse(ok);
			// provider.sendResponse(ok);
		} catch (Exception e) {
			tracer.info("Can not send SIP response: ", e);
		}

		joinInitiate();
	}

	private void joinInitiate() {
		try {
			NetworkConnection connection = getNetworkConnection();
			MediaSession session = connection.getMediaSession();
			MediaGroup ivr = session.createMediaGroup(MediaGroup.PLAYER_RECORDER_SIGNALDETECTOR);
			connection.joinInitiate(Direction.DUPLEX, ivr, "context");
			ActivityContextInterface activityContextInterface = mscAcifFactory.getActivityContextInterface(ivr);
			activityContextInterface.attach(sbbContext.getSbbLocalObject());
			
		} catch (Exception e) {
			tracer.severe("Unable to initiate join: ", e);
		}
	}

	public void onJoined(JoinEvent event, ActivityContextInterface aci) {
		tracer.info("**** LOCAL CONNECION WAS CREATED");

		URI uri = null;
		try {
			uri = new URI(WELCOME);
			getMediaGroup().getPlayer().play(uri, null, null);
		} catch (Exception e) {
			tracer.severe("Unexpected error", e);
		}
	}

	public void onAnnouncementCompleted(PlayerEvent event, ActivityContextInterface aci) {
		tracer.info("********** ANNOUNCEMENT COMPLETED *************");
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		sendBye();
		releaseMedia();
	}

	public void onDisconnect(RequestEvent event, ActivityContextInterface aci) {
		tracer.info("Disconnecting call");
		releaseMedia();
	}

	private void reject() {
		try {
			ActivityContextInterface serverTxACI = getServerTransactionACI();
			serverTxACI.detach(sbbContext.getSbbLocalObject());
			ServerTransaction st = (ServerTransaction) serverTxACI.getActivity();
			Response response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, st.getRequest());
			st.sendResponse(response);
		} catch (Exception ex) {
		}
	}

	private NetworkConnection getNetworkConnection() {
		for (ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if (aci.getActivity() instanceof NetworkConnection) {
				NetworkConnection nc = (NetworkConnection) aci.getActivity();
				return nc;
			}
		}
		return null;
	}

	private MediaGroup getMediaGroup() {
		for (ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if (aci.getActivity() instanceof MediaGroup) {
				MediaGroup mg = (MediaGroup) aci.getActivity();
				return mg;
			}
		}
		return null;
	}

	private DialogActivity getDialogActivity() {
		for (ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if (aci.getActivity() instanceof DialogActivity) {

				DialogActivity da = (DialogActivity) aci.getActivity();
				return da;
			}
		}
		return null;
	}

	private ActivityContextInterface getServerTransactionACI() {
		for (ActivityContextInterface aci : this.sbbContext.getActivities()) {
			if (aci.getActivity() instanceof ServerTransaction) {
				return aci;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	private void releaseMedia() {
		MediaGroup mg = getMediaGroup();
		if (mg != null) {
			mg.release();
		}

		NetworkConnection nc = getNetworkConnection();
		if (nc != null) {
			nc.release();
		}
	}

	/**
	 * 
	 */
	private void sendBye() {

		DialogActivity da = getDialogActivity();
		if (da != null) {
			try {
				Request bye = da.createRequest(Request.BYE);
				da.sendRequest(bye);
			} catch (Exception e) {
				tracer.severe("Caught exception, something bad happened.", e);
			}
		}
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.tracer = sbbContext.getTracer("MS-Control-DEMO");
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			sipProvider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");
			messageFactory = sipProvider.getMessageFactory();
			headerFactory = sipProvider.getHeaderFactory();
			addressFactory = sipProvider.getAddressFactory();

			mscFactory = (MsControlFactory) ctx.lookup("slee/resources/media/1.0/provider");
			mscAcifFactory = (MscActivityContextInterfaceFactory) ctx.lookup("slee/resources/media/1.0/acifactory");
		} catch (Exception ne) {
			tracer.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
	}

	public void sbbRolledBack(RolledBackContext arg0) {
	}
}
