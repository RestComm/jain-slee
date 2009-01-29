package org.mobicents.resources.sip.tests.junit;

import gov.nist.javax.sip.address.SipUri;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.cafesip.sipunit.SipTransaction;

import junit.framework.TestCase;

public class SuperJUnitSipRATC extends TestCase {

	protected boolean testFail = false;
	protected StringBuffer errorBuffer = new StringBuffer();

	protected void doFail(String message) {
		testFail = true;
		errorBuffer.append(message + "\n");
		//notifyAll();
	}

	public boolean isFailed() {
		return testFail;
	}

	protected void waitForTest(long miliseconds) {
		try {
			Thread.currentThread().sleep(miliseconds);
		} catch (InterruptedException e) {

			e.printStackTrace();
			fail(errorBuffer.toString());
		}
	}

	
	public String generateFromTag(String suffix) {
		// Again, this is some sort of bug - depending on place getName() ==
		// getCanonicalName() ....
		String className = this.getClass().getName().replace(
				"org.mobicents.resources.sip.tests.junit.", "");
		String[] split = className.split("_");
		className = split[split.length - 1];
		if (suffix == null) {
			return className + "SbbImpl_" + className;
		} else {
			return className.replace("Test", suffix + "Test") + "SbbImpl_"
					+ className.replace("Test", suffix + "Test");
		}
	}

	public String generateFromTag() {
		return this.generateFromTag(null);
	}

	protected SipStack sipStack;

	protected SipFactory sipFactory = null;

	protected SipProvider provider = null;

	protected SipProvider secondLegProvider = null;

	protected MessageFactory messageFactory = null;

	protected HeaderFactory headerFactory = null;

	protected AddressFactory addressFactory = null;

	protected int myPort;

	protected int remotePort = 5060;
	protected String testProtocol;

	protected Address remoteAddress;

	protected Address localAddress;

	protected SipURI requestUri;

	protected static final Properties defaultProperties = new Properties();
	static {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			host = "localhost";
		}

		defaultProperties.setProperty("javax.sip.IP_ADDRESS", host);
		defaultProperties.setProperty("javax.sip.STACK_NAME", "testAgent");
		defaultProperties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
		defaultProperties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
				"testAgent_debug.txt");
		defaultProperties.setProperty("gov.nist.javax.sip.SERVER_LOG",
				"testAgent_log.txt");
		defaultProperties
				.setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
		defaultProperties.setProperty(
				"gov.nist.javax.sip.CACHE_SERVER_CONNECTIONS", "false");

		defaultProperties.setProperty("sipunit.trace", "true");
		defaultProperties.setProperty("sipunit.test.port", "5600");
		defaultProperties.setProperty("sipunit.test.protocol", "udp");
	}

	protected Properties properties = new Properties(defaultProperties);

	// This is the first class/tests set that has to be executed, as on
	// ClientSide of tests we depend on this one to work well!!!
	public SuperJUnitSipRATC() {
		this(null);
	}

	public SuperJUnitSipRATC(Properties props) {
		super();
		properties.putAll(System.getProperties());

		
		properties.putAll(defaultProperties);
		if(props!=null)
		{
			properties.putAll(props);
		}
		try {
			myPort = Integer.parseInt(properties
					.getProperty("sipunit.test.port"));
		} catch (NumberFormatException e) {
			myPort = 5600;
		}

		System.out.println("HOST["
				+ (String) properties.get("javax.sip.IP_ADDRESS") + "]");
		testProtocol = properties.getProperty("sipunit.test.protocol");
	}
	
	protected void setUp() throws Exception {

		super.setUp();
		this.sipFactory = SipFactory.getInstance();
		this.sipFactory.setPathName("gov.nist"); // hmmm
		this.properties.remove("javax.sip.PORT");
		this.sipStack = this.sipFactory.createSipStack(this.properties);
		this.sipStack.start();
		ListeningPoint lp = this.sipStack.createListeningPoint(
				(String) properties.get("javax.sip.IP_ADDRESS"), this.myPort,
				this.testProtocol);
		this.provider = this.sipStack.createSipProvider(lp);
		this.headerFactory = this.sipFactory.createHeaderFactory();
		this.messageFactory = this.sipFactory.createMessageFactory();
		this.addressFactory = this.sipFactory.createAddressFactory();

		// Second leg

		lp = this.sipStack.createListeningPoint((String) properties
				.get("javax.sip.IP_ADDRESS"), this.myPort + 10,
				this.testProtocol);
		this.secondLegProvider = this.sipStack.createSipProvider(lp);

		localAddress = addressFactory
				.createAddress("sip:leg_1_tester@"
						+ this.provider.getListeningPoint(testProtocol)
								.getIPAddress()
						+ ":"
						+ this.provider.getListeningPoint(testProtocol)
								.getPort());

		remoteAddress = addressFactory.createAddress(addressFactory
				.createURI("sip:test_SBB@"
						+ this.provider.getListeningPoint(testProtocol)
								.getIPAddress() + ":" + remotePort));
	}

	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.sipStack.stop();
		this.sipFactory = null;

		this.sipStack = null;

		this.provider = null;
		this.headerFactory = null;
		this.messageFactory = null;
		this.addressFactory = null;
		this.secondLegProvider = null;
	}

	protected void triggerClientTest(String testSuffix, SipListener listener) throws Exception {
		Request messageRequest = null;

		messageRequest = messageFactory.createRequest(null);
		SipUri reqeustUri = new SipUri();
		reqeustUri.setMethod(Request.NOTIFY);
		reqeustUri.setPort(5060);
		reqeustUri.setHost(this.properties.getProperty("javax.sip.IP_ADDRESS"));
		reqeustUri.setUser("test_SBB");
		messageRequest.setRequestURI(reqeustUri);
		messageRequest.addHeader(this.provider.getNewCallId());
		messageRequest.addHeader(headerFactory.createCSeqHeader((long) 1,
				Request.NOTIFY));
		messageRequest.addHeader(headerFactory.createFromHeader(localAddress,
				generateFromTag(testSuffix)));

		messageRequest.addHeader(headerFactory.createToHeader(remoteAddress,
				null));

		messageRequest.addHeader(headerFactory
				.createContactHeader(localAddress));

		messageRequest.addHeader(headerFactory.createMaxForwardsHeader(5));
		ViaHeader via = getLocalVia(provider);
		messageRequest.addHeader(via);

		// create and add the Route Header

		messageRequest.addHeader(generateRouteHeader(remoteAddress));
		messageRequest.addHeader(headerFactory
				.createSubscriptionStateHeader("state=default"));
		messageRequest.addHeader(headerFactory.createEventHeader("Trigger"));
		messageRequest.setMethod(Request.NOTIFY);

		final ClientTransaction triggerTransaction = this.provider
				.getNewClientTransaction(messageRequest);

		

		this.provider.addSipListener(listener);
		triggerTransaction.sendRequest();

		
	}

	protected ViaHeader getLocalVia(SipProvider _provider)
			throws ParseException, InvalidArgumentException {
		return headerFactory.createViaHeader(_provider.getListeningPoint(
				testProtocol).getIPAddress(), _provider.getListeningPoint(
				testProtocol).getPort(), _provider.getListeningPoint(
				testProtocol).getTransport(), null);

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
	
}
