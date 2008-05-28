package org.mobicents.slee.services.sip.balancer;

import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * This class is a UAC template.
 * 
 * @author M. Ranganathan
 */

public class Shootist implements SipListener {

	static AddressFactory addressFactory;

	static MessageFactory messageFactory;

	static HeaderFactory headerFactory;

	static SipStack sipStack;

	static SipFactory sipFactory = null;

	SipProvider sipProvider;

	String myHost = "10.1.1.1";
	int myPort = 5040;
	static String transport = "UDP";

	Dialog d = null;
	AtomicInteger nbConcurrentInvite = new AtomicInteger(0);

	// Keeps track of successful dialog completion.
	private static Timer timer;

	static {
		timer = new Timer();
	}

	public void processRequest(RequestEvent requestReceivedEvent) {

		System.out
				.println("GOT REQEUST:\n" + requestReceivedEvent.getRequest());
		Request req = requestReceivedEvent.getRequest();

		if (req.getMethod().equals(Request.BYE)) {

			try {
				Response ok = this.messageFactory.createResponse(Response.OK,
						req);
				ToHeader toHeader = (ToHeader) ok.getHeader(ToHeader.NAME);
				toHeader.setTag("TCK_TMP_TAG_" + Math.random() * 10000);
				SipUri uri = new SipUri();
				uri.setPort(myPort);
				uri.setHost(myHost);
				uri.setTransportParam("UDP");
				Address contactAddress = this.addressFactory.createAddress(uri);
				ContactHeader contact = this.headerFactory
						.createContactHeader(contactAddress);
				ok.addHeader(contact);
				requestReceivedEvent.getServerTransaction().sendResponse(ok);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void processResponse(ResponseEvent responseReceivedEvent) {

		Response resp = responseReceivedEvent.getResponse();
		String method = ((CSeq) resp.getHeader(CSeq.NAME)).getMethod();
		System.out.println("GOT RESPONSE:\n" + resp);

		if (method.equals(Request.REGISTER)) {
			System.out.println("1");
			if (resp.getStatusCode() == 200) {
				System.out.println("2");
				sendInvite();
			} else {
				System.out.println("3");
				// BAD
			}

		}

		if (method.equals(Request.INVITE)) {
			if (resp.getStatusCode() == 200) {
				// try {
				d = responseReceivedEvent.getDialog();
				try {
					d.sendAck(d.createAck(1));
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// } catch (SipException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			} else {
				// BAD
			}

		}

	}

	public void processTimeout(javax.sip.TimeoutEvent timeoutEvent) {

	}

	public void sendRegister() {

		ViaHeader viaHeader = null;
		MaxForwardsHeader maxForwardsHeader = null;
		ContactHeader contactHeader = null;
		ToHeader toHeader = null;
		FromHeader fromHeader = null;
		CallIdHeader callIdHeader = null;
		CSeqHeader cseqHeader = null;
		ExpiresHeader expires = null;
		AllowHeader allow = null;
		Event event = null;

		RouteHeader routeHeader = null;

		// LETS CREATEOUR HEADERS

		String localAddress = sipProvider.getListeningPoints()[0]
				.getIPAddress();
		int localPort = sipProvider.getListeningPoints()[0].getPort();
		String localTransport = sipProvider.getListeningPoints()[0]
				.getTransport();

		try {
			cseqHeader = this.headerFactory.createCSeqHeader(1,
					Request.REGISTER);
			viaHeader = this.headerFactory.createViaHeader(localAddress,
					localPort, localTransport, null);
			Address fromAddres = this.addressFactory
					.createAddress("sip:tester@one.nist.gov");
			// Address
			// toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
			Address toAddress = this.addressFactory
					.createAddress("sip:tester@one.nist.gov");
			contactHeader = this.headerFactory
					.createContactHeader(this.addressFactory
							.createAddress("sip:" + localAddress + ":"
									+ localPort));
			// WE RELLY ON DEFUALT EXPIRES VALUE HERE
			// contactHeader.setExpires(3600);
			toHeader = this.headerFactory.createToHeader(toAddress, null);
			fromHeader = this.headerFactory.createFromHeader(fromAddres,
					"wakeupER");
			callIdHeader = sipProvider.getNewCallId();
			maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);

			// contentTypeHeader = this.headerFactory
			// .createContentTypeHeader("text", "plain");
			Address routeAddress = this.addressFactory
					.createAddress("sip:one.nist.gov:5010");
			routeHeader = this.headerFactory.createRouteHeader(routeAddress);

		} catch (ParseException e) {

			e.printStackTrace();

		} catch (InvalidArgumentException e) {

			e.printStackTrace();

		}
		// LETS CREATE OUR REQUEST AND
		ArrayList list = new ArrayList();
		list.add(viaHeader);
		URI requestURI = null;
		Request request = null;

		try {
			requestURI = this.addressFactory.createURI("sip:nist.gov");
			request = this.messageFactory.createRequest(requestURI,
					Request.REGISTER, callIdHeader, cseqHeader, fromHeader,
					toHeader, list, maxForwardsHeader);
			request.addHeader(routeHeader);
			request.addHeader(contactHeader);
		} catch (ParseException e) {

			e.printStackTrace();

		}

		ClientTransaction ctx;
		try {
			ctx = sipProvider.getNewClientTransaction(request);
			ctx.sendRequest();
		} catch (TransactionUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendInvite() {

		// SUBSCRIBE sip:demon@nist.gov SIP/2.0
		// Via: SIP/2.0/UDP
		// 127.0.0.1:4222;received=10.1.1.1;branch=z9hG4bK-d87543-406d2422373a6601-1--d87543-;rport=4222
		// Max-Forwards: 70
		// Contact: <sip:demon@10.1.1.1:4222>
		// To: "test1" <sip:demon@nist.gov>
		// From: "test1" <sip:demon@nist.gov>;tag=dc457b43
		// Call-ID: YWIwMGE4ZjQwMzIyMDFmNDEwYmQ1YzdlNDY1ZTBkMDU.
		// CSeq: 1 SUBSCRIBE
		// Expires: 300
		// Allow:
		// INVITE,ACK,CANCEL,OPTIONS,BYE,REFER,NOTIFY,MESSAGE,SUBSCRIBE,INFO
		// User-Agent: X-Lite release 1011s stamp 41150
		// Event: message-summary
		// Content-Length: 0
		ViaHeader viaHeader = null;
		MaxForwardsHeader maxForwardsHeader = null;
		ContactHeader contactHeader = null;
		ToHeader toHeader = null;
		FromHeader fromHeader = null;
		CallIdHeader callIdHeader = null;
		CSeqHeader cseqHeader = null;
		ExpiresHeader expires = null;
		AllowHeader allow = null;
		EventHeader event = null;

		RouteHeader routeHeader = null;

		System.out.println("4");

		// LETS CREATEOUR HEADERS

		String localAddress = sipProvider.getListeningPoints()[0]
				.getIPAddress();
		int localPort = sipProvider.getListeningPoints()[0].getPort();
		String localTransport = sipProvider.getListeningPoints()[0]
				.getTransport();

		try {
			System.out.println("5");
			cseqHeader = this.headerFactory.createCSeqHeader(1, Request.INVITE);
			viaHeader = this.headerFactory.createViaHeader(localAddress,
					localPort, localTransport, null);
			Address fromAddres = this.addressFactory
					.createAddress("sip:tester@one.nist.gov");
			// Address
			// toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
			Address toAddress = this.addressFactory
					.createAddress("sip:wakeup@two.nist.gov");
			contactHeader = this.headerFactory
					.createContactHeader(this.addressFactory
							.createAddress("sip:" + localAddress + ":"
									+ localPort));
			// WE RELLY ON DEFUALT EXPIRES VALUE HERE
			System.out.println("6");
			// contactHeader.setExpires(3600);
			toHeader = this.headerFactory.createToHeader(toAddress, null);
			fromHeader = this.headerFactory.createFromHeader(fromAddres,
					"wakeupER");
			callIdHeader = sipProvider.getNewCallId();
			maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);

			expires = this.headerFactory.createExpiresHeader(300);
			allow = this.headerFactory.createAllowHeader("INVITE,OK");
			event = this.headerFactory.createEventHeader("message-summary");
			System.out.println("7");

			// contentTypeHeader = this.headerFactory
			// .createContentTypeHeader("text", "plain");
			Address routeAddress = this.addressFactory
					.createAddress("sip:one.nist.gov:5010");
			routeHeader = this.headerFactory.createRouteHeader(routeAddress);

		} catch (ParseException e) {

			e.printStackTrace();

		} catch (InvalidArgumentException e) {

			e.printStackTrace();

		}
		// LETS CREATE OUR REQUEST AND
		System.out.println("8");
		ArrayList list = new ArrayList();
		list.add(viaHeader);
		URI requestURI = null;
		Request request = null;

		try {
			requestURI = this.addressFactory
					.createURI("sip:wakeup@two.nist.gov");
			request = this.messageFactory.createRequest(requestURI,
					Request.INVITE, callIdHeader, cseqHeader, fromHeader,
					toHeader, list, maxForwardsHeader);
			request.addHeader(routeHeader);
			request.addHeader(contactHeader);
			request.addHeader(expires);
			request.addHeader(allow);
			request.addHeader(event);
			System.out.println("9");
		} catch (ParseException e) {

			e.printStackTrace();

		}
		System.out.println("10");
		ClientTransaction ctx;
		try {
			System.out.println("SENDING INVITE:\n" + request);
			ctx = sipProvider.getNewClientTransaction(request);
			ctx.sendRequest();
		} catch (TransactionUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processIOException(IOExceptionEvent exceptionEvent) {
		System.out.println("IOException occured while retransmitting requests:"
				+ exceptionEvent);
	}

	public void processTransactionTerminated(
			TransactionTerminatedEvent transactionTerminatedEvent) {
		// System.out.println("Transaction Terminated event: " +
		// transactionTerminatedEvent);
	}

	public void processDialogTerminated(
			DialogTerminatedEvent dialogTerminatedEvent) {
		// System.out.println("Dialog Terminated event: " +
		// dialogTerminatedEvent);
	}

	public Shootist() {
		Properties properties = new Properties();
		properties.setProperty("javax.sip.IP_ADDRESS", myHost);
		properties.setProperty("javax.sip.RETRANSMISSION_FILTER", "true");
		properties.setProperty("javax.sip.STACK_NAME", "shootme");
		// You need 16 for logging traces. 32 for debug + traces.
		// Your code will limp at 32 but it is best for debugging.

		try {
			// Create SipStack object
			sipFactory = SipFactory.getInstance();
			sipFactory.setPathName("gov.nist");
			sipStack = sipFactory.createSipStack(properties);

		} catch (PeerUnavailableException pue) {
			// could not find
			// gov.nist.jain.protocol.ip.sip.SipStackImpl
			// in the classpath
			pue.printStackTrace();
			throw new IllegalStateException("Cant create stack due to["
					+ pue.getMessage() + "]");
		}

		try {
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();
			messageFactory = sipFactory.createMessageFactory();
			ListeningPoint lp = sipStack.createListeningPoint(myPort, "udp");

			sipProvider = sipStack.createSipProvider(lp);

			sipProvider.addSipListener(this);

			sipStack.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalStateException(
					"Cant create sip objects and lps due to[" + ex.getMessage()
							+ "]");
		}
	}

	public static void main(String args[]) throws Exception {
		
		Shootist s=null;
		try {
			
			s = new Shootist();
			s.sendRegister();
		

		} finally {
			System.in.read();
			s.sipStack.stop();
		}
	}
}
