package org.mobicents.resoruces.sip.test;

import gov.nist.javax.sip.address.SipUri;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;

public abstract class SuperSipRaTestSbb implements Sbb {

	protected AddressFactory addressFactory = null;
	protected Logger logger = Logger.getLogger(this.getClass());
	protected HeaderFactory headerFactory = null;

	protected MessageFactory messageFactory = null;
	protected SipActivityContextInterfaceFactory acif = null;
	protected SleeSipProvider fp;

	protected SbbContext sbbContext;

	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);

	protected TimerFacility tf = null;

	public InitialEventSelector chooseThis(InitialEventSelector ies) {

		
		if (ies.getEvent() instanceof RequestEvent) {
			Request req = ((RequestEvent) ies.getEvent()).getRequest();
			FromHeader fromHeader = (FromHeader) req.getHeader(FromHeader.NAME);
			String tag = fromHeader.getTag();

			if (tag.toLowerCase().contains(generateFromTag().toLowerCase())) {
				ies.setCustomName(this.getClass().getName().toLowerCase());
				ies.setInitialEvent(true);
				return ies;
			}
		} else if (ies.getEvent() instanceof ResponseEvent) {
			Response resp = ((ResponseEvent) ies.getEvent()).getResponse();
			FromHeader fromHeader = (FromHeader) resp
					.getHeader(FromHeader.NAME);
			
			String tag = fromHeader.getTag();
			if (tag.toLowerCase().contains(generateFromTag().toLowerCase())) {
				ies.setCustomName(this.getClass().getName().toLowerCase());
				ies.setInitialEvent(true);
				return ies;
			}
		}
		ies.setInitialEvent(false);
		return ies;
	}

	public String generateFromTag() {
		String className = this.getClass().getName().replace("SbbImpl", "");
		logger.info(" -----------------> GENRATED TAG="
				+ (className + "SbbImpl_" + className).replace(
						"org.mobicents.resoruces.sip.test.", "") + "");
		// WHY IN JBOSS getName == getCanonicalName ??
		return (className + "SbbImpl_" + className).replace(
				"org.mobicents.resoruces.sip.test.", "");

	}

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

	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting JAIN SIP Resource Adaptor interfaces
			fp = (SleeSipProvider) myEnv
					.lookup("slee/resources/jainsip/1.2/provider");
			// To create Address objects from a particular implementation of
			// JAIN SIP
			addressFactory = fp.getAddressFactory();
			// To create Request and Response messages from a particular
			// implementation of JAIN SIP
			messageFactory = fp.getMessageFactory();
			headerFactory = fp.getHeaderFactory();
			// logger.info("^^^^^^^^^^^^^^^ "+addressFactory+" "+headerFactory+"
			// "+messageFactory+" ");
			acif = (SipActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainsip/1.2/acifactory");
			// l/ogger
			// .info("**************** ACIF: " + SIPACIF
			// + " **************");
		} catch (NamingException e) {

			e.printStackTrace();
		}

		tf = retrieveTimerFacility();
	}

	public void unsetSbbContext() {
		this.sbbContext = null;

	}

	public SbbContext getSbbContext() {
		return this.sbbContext;
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
			logger.error("Could not create nullActivityFactory: "
					+ ne.getMessage());
		} catch (UnrecognizedActivityException UAE) {
			logger
					.error("Could not create nullActivityContextInterfaceFactory: "
							+ UAE.getMessage());
		}
		return localACI;
	}

	// CMPs---------------------------
	public abstract Map getWatchedActivityHandles();

	public abstract void setWatchedActivityHandles(Map handles);

	public abstract long getMaxTime();

	public abstract void setMaxTime(long maxTime);

	public abstract Map getHeadersMap();

	public abstract void setHeadersMap(Map headers);

	// --- Protected help memthods
	protected void startActivityWatch() {
	}

	protected void sendActivitiesEndNotification(boolean succcess,
			String message) {

	}

	protected final static String _REMOTE_URI = "_REMOTE_URI";
	protected final static String _LOCAL_URI = "_LOCAL_URI";
	protected final static String _LOCAL_TO_FROM = "_LOCAL_TO_FROM";
	protected final static String _REMOTE_TO_FROM = "_REMOTE_TO_FROM";
	protected final static String _LOCAL_TAG = "_LOCAL_TAG";
	protected final static String _REMOTE_TAG = "_REMOTE_TAG";

	// Call for incoming requests
	protected void generateHeadersMap(Request request) {

		if (this.getHeadersMap() == null) {
			this.setHeadersMap(new HashMap<String, Object>());
		}

		Map<String, Object> headersMap = this.getHeadersMap();
		if (!headersMap.containsKey(_REMOTE_URI)) {
			ContactHeader remoteContact = (ContactHeader) request
					.getHeader(ContactHeader.NAME);
			headersMap.put(_REMOTE_URI, remoteContact.getAddress().getURI());
			logger.info("SETTIGN REMOTE="+remoteContact.getAddress().getURI());
		}

		if (!headersMap.containsKey(_LOCAL_URI)) {
			RouteHeader localContact = (RouteHeader) request
					.getHeader(RouteHeader.NAME);
			headersMap.put(_LOCAL_URI, localContact.getAddress().getURI());
		}

		ToHeader localToFrom = (ToHeader) request.getHeader(ToHeader.NAME);
		if (!headersMap.containsKey(_LOCAL_TO_FROM)) {

			headersMap.put(_LOCAL_TO_FROM, localToFrom.getAddress().getURI());

		}

		if (!headersMap.containsKey(_LOCAL_TAG) && localToFrom.getTag() != null) {

			headersMap.put(_LOCAL_TAG, localToFrom.getTag());

		}

		FromHeader remoteToFrom = (FromHeader) request
				.getHeader(FromHeader.NAME);
		if (!headersMap.containsKey(_REMOTE_TO_FROM)) {

			headersMap.put(_REMOTE_TO_FROM, remoteToFrom.getAddress().getURI());

		}

		if (!headersMap.containsKey(_REMOTE_TAG)
				&& remoteToFrom.getTag() != null) {

			headersMap.put(_REMOTE_TAG, remoteToFrom.getTag());

		}

	}

	// process Timer/ check for Activities end, remove handles, send
	// notification

	public void onTimeEvent(TimerEvent event, ActivityContextInterface aci) {

	}

	protected ViaHeader getLocalVia(SipProvider _provider)
			throws ParseException, InvalidArgumentException {
		return headerFactory.createViaHeader(_provider.getListeningPoint(
				"udp").getIPAddress(), _provider.getListeningPoint(
						"udp").getPort(), _provider.getListeningPoint(
								"udp").getTransport(), null);

	}

	protected RouteHeader generateRouteHeader(Address addr)
			throws ParseException {
		return headerFactory.createRouteHeader(addressFactory
				.createAddress("sip:" + ((SipURI) addr.getURI()).getHost()
						+ ":" + ((SipURI) addr.getURI()).getPort()));
	}

	public String doMessage(Throwable t) {
		StringBuffer sb = new StringBuffer();
		int tick = 0;
		Throwable e = t;
		do {
			StackTraceElement[] trace = e.getStackTrace();
			if (tick++ == 0)
				sb.append(e.getClass().getCanonicalName() + ":"
						+ e.getLocalizedMessage() + "\n");
			else
				sb.append("Caused by: " + e.getClass().getCanonicalName() + ":"
						+ e.getLocalizedMessage() + "\n");

			for (StackTraceElement ste : trace)
				sb.append("\t" + ste + "\n");

			e = e.getCause();
		} while (e != null);

		return sb.toString();

	}
	
	protected void sendErrorRequest(String method, String message) {
		try {
			Request messageRequest = messageFactory.createRequest(null);
			SipURI remoteURI = (SipURI) getHeadersMap().get(_REMOTE_URI);
			SipUri reqeustUri = new SipUri();
			reqeustUri.setMethod(Request.MESSAGE);
			reqeustUri.setPort(remoteURI.getPort());
			reqeustUri.setHost(fp.getListeningPoint("udp").getIPAddress());
			reqeustUri.setUser("SECOND_LEG");
			messageRequest.setRequestURI(reqeustUri);
			messageRequest.addHeader(fp.getNewCallId());
			messageRequest.addHeader(headerFactory.createCSeqHeader(
					(long) 1, Request.MESSAGE));
			SipUri fromURI = (SipUri) getHeadersMap().get(_LOCAL_TO_FROM);

			SipUri localFromURI = new SipUri();
			localFromURI.setHost(fromURI.getHost());
			localFromURI.setPort(fromURI.getPort());
			localFromURI.setScheme(fromURI.getScheme());
			localFromURI.setUser(fromURI.getUser());
			messageRequest.addHeader(headerFactory.createFromHeader(
					addressFactory.createAddress(localFromURI),
					generateFromTag() + "_SECOND_LEG"));

			messageRequest.addHeader(headerFactory.createToHeader(
					addressFactory.createAddress(reqeustUri), null));

			messageRequest.addHeader(headerFactory
					.createContactHeader(addressFactory
							.createAddress(localFromURI)));

			messageRequest.addHeader(getLocalVia(fp));

			// create and add the Route Header

			messageRequest
					.addHeader(headerFactory.createRouteHeader(addressFactory
							.createAddress(reqeustUri)));
			messageRequest.setMethod(Request.MESSAGE);
			messageRequest.addHeader(headerFactory.createMaxForwardsHeader(5));

			messageRequest.setContent(message, headerFactory
					.createContentTypeHeader("text", "plain"));
			fp.sendRequest(messageRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
