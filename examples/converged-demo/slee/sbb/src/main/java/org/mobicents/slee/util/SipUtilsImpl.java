/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU Lesser General Public License as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU Lesser General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.util;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipStack;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ProxyAuthenticateHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;

public class SipUtilsImpl implements SipUtils {

	private static Logger log = Logger.getLogger(SipUtilsImpl.class);

	private SleeSipProvider sipProvider;

	private HeaderFactory headerFactory;

	private MessageFactory messageFactory;

	private AddressFactory addressFactory;

	public SipUtilsImpl(SleeSipProvider sipProvider, HeaderFactory headerFactory, MessageFactory messageFactory,
			AddressFactory addressFactory) {
		this.sipProvider = sipProvider;
		this.headerFactory = headerFactory;
		this.messageFactory = messageFactory;
		this.addressFactory = addressFactory;
	}

	public Dialog getDialog(ResponseEvent event) throws SipException {
		Dialog retVal = null;

		if (event.getDialog() == null) {
			// This is non recoverable and can occur if AUTOMATIC_DIALOG_SUPPORT
			// is disabled in the sip stack.
			log.error("responseEvent.getDialog returned null, if AUTOMATIC_DIALOG_SUPPORT is disabled "
					+ "you must obtain a dialog before the first sip response arrives: "+event.getClientTransaction().getDialog());
			throw new SipException("responseEvent.getDialog returned null, if AUTOMATIC_DIALOG_SUPPORT is disabled "
					+ "you must obtain a dialog before the first sip response arrives");
		} else {
			retVal = event.getDialog();
			log.debug("Returning dialog in getDialog(ResponseEvent) obtained directly from ResponseEvent");
		}

		return retVal;
	}

	public Dialog getDialog(RequestEvent event) throws SipException {
		Dialog retVal = null;
		try {
			if (event.getDialog() == null) {
				retVal = sipProvider.getNewDialog(event.getServerTransaction());
			} else {
				retVal = event.getDialog();
			}
		} catch (SipException ex) {
			log.error("Exception in creating a new dialog in getDialog(RequestEvent)", ex);
			throw ex;
		}
		return retVal;
	}

	/**
	 * Hex characters
	 */
	private final char[] toHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public String generateTag() {
		String tag = new Integer((int) (Math.random() * 10000)).toString();
		return tag;
	}

	/**
	 * Convert an array of bytes to an hexadecimal string
	 * 
	 * @param b
	 *            The byte array to convert to a hexadecimal string
	 * @return The hexadecimal string representation of the byte array
	 * 
	 */
	private String toHexString(byte b[]) {
		int pos = 0;
		char[] c = new char[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			c[pos++] = toHex[(b[i] >> 4) & 0x0F];
			c[pos++] = toHex[b[i] & 0x0f];
		}
		return new String(c);
	}

	private AddressFactory getAddressFactory() {
		AddressFactory addressFactory = null;
		try {
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
			SleeSipProvider factoryProvider = (SleeSipProvider) myEnv.lookup("slee/resources/jainsip/1.2/provider");
			addressFactory = factoryProvider.getAddressFactory();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return addressFactory;
	}

	public Address convertURIToAddress(String uri) {
		SipURI sipURI = convertURIToSipURI(uri);
		return getAddressFactory().createAddress(sipURI);
	}

	public SipURI convertURIToSipURI(String uri) {
		String[] sipUserAndHost = null;
		SipURI sipURI = null;
		try {
			sipUserAndHost = parseSipUri(uri);
			final String sipUser = sipUserAndHost[0];
			final String sipHost = sipUserAndHost[1];
			sipURI = getAddressFactory().createSipURI(sipUser, sipHost);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sipURI;
	}

	public String[] parseSipUri(String sipURI) throws URISyntaxException {
		String[] userAndHost = new String[2];
		final String sipPrexfix = "sip:";
		final String uriSeparator = "@";

		final int sipPrefixIndex = sipURI.indexOf(sipPrexfix) + 3;
		final int uriIndex = sipURI.indexOf(uriSeparator);

		final int nameIndex = sipPrefixIndex + 1;
		final int hostIndex = uriIndex + 1;

		if (sipURI.indexOf(sipPrexfix) == -1 || sipURI.indexOf(uriSeparator) == -1 || sipPrefixIndex > uriIndex) {
			throw new URISyntaxException(sipURI,
					"Malformed URI, the URI must use the format \"sip:user@host\". The incorrect URI was \"" + sipURI
							+ "\".");
		}

		userAndHost[0] = sipURI.substring(nameIndex, uriIndex);
		userAndHost[1] = sipURI.substring(hostIndex);

		return userAndHost;
	}

	public SipURI convertAddressToSipURI(Address address) throws ParseException {
		URI sipURI = address.getURI();
		SipURI retVal = null;
		if (sipURI.isSipURI())
			retVal = (SipURI) sipURI;
		else {
			throw new ParseException("URI was not of type SipURI!", -1);
		}
		return retVal;
	}

	public void sendCancel(ClientTransaction ct) throws SipException {
		Request request = ct.createCancel();
		// TODO Verify that this is the correct procedure for sending the CANCEL
		// It works but the api is not very clear about what happens when the
		// request
		// is created with ct.createCancel and sent in another new client
		// transaction
		ct = sipProvider.getNewClientTransaction(request);
		ct.sendRequest();
	}

	public Request buildInvite(Address fromAddress, Address toAddress, byte[] content, int cSeq) throws ParseException,
			InvalidArgumentException {
		return buildInvite(fromAddress, toAddress, content, cSeq, null);
	}

	public Request buildInvite(Address fromAddress, Address toAddress, byte[] content, int cSeq, String callId)
			throws ParseException, InvalidArgumentException {

		// From Header:
		FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, generateTag());

		// To header:
		ToHeader toHeader = headerFactory.createToHeader(toAddress, null);

		// Create request
		Request request = null;

		// Set the sequence number for the invite
		CSeqHeader cseqHeader = headerFactory.createCSeqHeader(cSeq, Request.INVITE);

		// Create the Via header and add to an array list
		ArrayList viaHeadersList = new ArrayList();
		viaHeadersList.add(createLocalViaHeader());

		/*
		 * The Max-Forwards header field serves to limit the number of hops a
		 * request can transit on the way to its destination. It consists of an
		 * integer that is decremented by one at each hop. If the Max-Forwards
		 * value reaches 0 before the request reaches its destination, it will
		 * be rejected with a 483(Too Many Hops) error response.
		 */
		MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);

		final SipURI requestURI = convertAddressToSipURI(toAddress);
		SipURI fromUri = convertAddressToSipURI(fromAddress);

		/*
		 * Create the request
		 */
		CallIdHeader callIdHeader = sipProvider.getNewCallId();

		if ((callId != null) && (callId.trim().length() > 0)) {
			callIdHeader.setCallId(callId);
		}

		// callId = callIdHeader.getCallId();
		request = messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader, cseqHeader, fromHeader,
				toHeader, viaHeadersList, maxForwardsHeader);

		// If a Content (such as an SDP) is to be attached with this
		// INVITE, attach it! Otherwise this part is just skipped
		if (content != null) {
			setContent(request, "application", "sdp", content);
		}

		/*
		 * Add a contact header, this is mandatory for INVITE's. The Contact
		 * header field value contains the URI at which the User Agent would
		 * like to receive requests, and this URI MUST be valid even if used in
		 * subsequent requests outside of any dialogs.
		 */
		ContactHeader contactHeader = createLocalContactHeader(fromUri.getUser());
		request.setHeader(contactHeader);

		if (log.isDebugEnabled()) {
			log.debug("Contact Header = " + contactHeader);
		}

		return request;
	}

	public ContactHeader createLocalContactHeader(String user) throws ParseException {



		// Get the transport
		final String transport = sipProvider.getListeningPoints()[0]
				.getTransport();

		// Create a SIP URI of the host name
		SipURI sipURI = sipProvider.getLocalSipURI(transport);
		sipURI.setUser(user);
		
		//There is a bug, let make sure
		sipURI.setTransportParam(transport);
		// Create the contact address using the address factory
		Address contactAddress = addressFactory.createAddress(sipURI);
		// Create the contact header from the contact address
		ContactHeader contactHeader = headerFactory
				.createContactHeader(contactAddress);
		return contactHeader;
	}

	public ViaHeader createLocalViaHeader() throws ParseException, InvalidArgumentException {
		final String transport = sipProvider.getListeningPoints()[0].getTransport();
		ViaHeader viaHeader = null;
		try {
			viaHeader = this.sipProvider.getLocalVia(transport, null);
		} catch (TransportNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return viaHeader;
	}

	/**
	 * Sets a content for a request
	 * 
	 * @param request
	 *            The request to set the content for
	 * @param contentType
	 *            the new string content type value.
	 * @param contentSubType
	 *            the new string content sub-type value.
	 * @param content
	 *            The content to set
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	private void setContent(Request request, String contentType, String contentSubType, byte[] content)
			throws ParseException, InvalidArgumentException {
		/*
		 * Create Content-Type The Content-Type header field indicates the media
		 * type of the message-body sent to the recipient. The Content-Type
		 * header field MUST be present if the body is not empty. If the body is
		 * empty, and a Content-Type header field is present, it indicates that
		 * the body of the specific type has zero length (for example, an empty
		 * audio file).
		 */
		ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(contentType, contentSubType);
		// Apply the header
		// request.setHeader(contentTypeHeader);

		request.setContent(content, contentTypeHeader);

		/*
		 * Content-Length header
		 */
		ContentLengthHeader contentLengthHeader = headerFactory.createContentLengthHeader(content.length);
		request.setContentLength(contentLengthHeader);
	}

	public Request buildRequestWithAuthorizationHeader(ResponseEvent event, String password)
			throws TransactionUnavailableException {
		// If user is not authenticated, initialize the authentication
		// process. First we start off by retrieving the request that
		// triggered this response.

		Request request = event.getClientTransaction().getRequest();
		Response response = event.getResponse();

		if (request == null) {
			if (log.isDebugEnabled()) {
				log.debug("The request that caused the 407 could not be retrieved.");
			}
			return null;
		} else {
			// Clone the previous request since we'd like to modify
			// it with an authentication header and send it back to the
			// server.
			// Dialog dialog = event.getDialog();
			// Request requestClone = (Request) request.clone();
			// Get the sequence number from the request clone
			CSeqHeader cseqHeader = (CSeqHeader) request.getHeader(CSeqHeader.NAME);
			// try {
			// // Increase the sequence number by one
			// cseqHeader
			// .setSequenceNumber(cseqHeader.getSequenceNumber() + 1);
			// } catch (InvalidArgumentException e) {
			// log.error("InvalidArgumentException while setting
			// cseqHeader.setSequenceNumber", e);
			// e.printStackTrace();
			// }

			FromHeader fromHeaderReq = (FromHeader) request.getHeader(FromHeader.NAME);
			Address fromAddressReq = fromHeaderReq.getAddress();

			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			Address toAddress = toHeader.getAddress();
			Request newRequest = null;
			String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
			try {
				newRequest = buildInvite(fromAddressReq, toAddress, null, cseqHeader.getSequenceNumber() + 1, callId);
			} catch (ParseException parseExc) {
				parseExc.printStackTrace();
			} catch (InvalidArgumentException invaliArgExc) {
				invaliArgExc.printStackTrace();
			}

			// Now can begin to build the authentication header using a MD5
			// digest scheme
			WWWAuthenticateHeader wwwAuthenticateHeader = (WWWAuthenticateHeader) response
					.getHeader(WWWAuthenticateHeader.NAME);
			ProxyAuthenticateHeader proxyAuthenticateHeader = (ProxyAuthenticateHeader) response
					.getHeader(ProxyAuthenticateHeader.NAME);

			String realm = null;
			String nonce = null;
			// Check whether we've received an wwwAuthenticationHeader
			// or a ProxyAuthenticationHeader from the proxy server
			if (wwwAuthenticateHeader != null) {
				if (log.isDebugEnabled()) {
					log.debug("wwwAuthenticateHeader found!");
				}
				// Retrieve the realm from the authentication header
				realm = wwwAuthenticateHeader.getRealm();
				// Retrieve the nonce from the wwwAuthenticateHeader
				nonce = wwwAuthenticateHeader.getNonce();

			} else if (proxyAuthenticateHeader != null) {
				if (log.isDebugEnabled()) {
					log.debug("ProxyAuthenticateHeader found!");
				}
				// Retrieve the realm from the authentication header
				realm = proxyAuthenticateHeader.getRealm();
				// Retrieve the nonce from the wwwAuthenticateHeader
				nonce = proxyAuthenticateHeader.getNonce();
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Neither a ProxyAuthenticateHeader or AuthorizationHeader found!");
				}
				return null;
			}

			// Retrieve the method from the CSeqHeader
			final String method = cseqHeader.getMethod();

			// Get the user name
			final FromHeader fromHeader = ((FromHeader) response.getHeader(FromHeader.NAME));
			Address address = fromHeader.getAddress();
			String fromHost = null;
			String fromUser = null;
			int fromPort = 0;

			String toHost = null;
			String toUser = null;
			int toPort = 0;

			SipURI fromSipURI = null;
			SipURI toSipURI = null;
			try {
				fromSipURI = convertAddressToSipURI(address);
				toSipURI = convertAddressToSipURI(toAddress);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			fromHost = fromSipURI.getHost();
			fromUser = fromSipURI.getUser();
			fromPort = fromSipURI.getPort();

			toHost = toSipURI.getHost();
			toUser = toSipURI.getUser();
			toPort = toSipURI.getPort();

			// Appened the port to the fromHost if available
			if (fromPort != -1) {
				fromHost += ":" + fromPort;
			}

			// Get the URI to set in the header
			SipURI uri = null;
			try {
				// uri = request.getRequestURI();
				uri = addressFactory.createSipURI(toUser, toHost);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// create the digest for the response
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			// Generate the response digest according to RFC 2069, i.e.
			// response-digest = <"> < KD ( H(A1), unquoted nonce-value ":"
			// H(A2) > <">
			// A1 = unquoted username-value ":" unquoted realm-value ":"
			// password
			// password = < user's password >
			// A2 = Method ":" digest-uri-value
			// H(A1) = The digested value of A1 converted to a hex string
			// H(A2) = The digested value of A2 converted to a hex string
			// KD = H(A1) ":" nonce ":" H(A2)

			// Create A1 and A2
			String A1 = fromUser + ":" + realm + ":" + password;
			String A2 = method.toUpperCase() + ":" + uri.toString();
			// MD5 digest A1
			byte mdbytes[] = md5.digest(A1.getBytes());
			// Create H(A1)
			String HA1 = toHexString(mdbytes);
			// MD5 digest A2
			mdbytes = md5.digest(A2.getBytes());
			// Create H(A2)
			String HA2 = toHexString(mdbytes);
			// Create KD
			String KD = HA1 + ":" + nonce + ":" + HA2;
			mdbytes = md5.digest(KD.getBytes());

			// Check if we're dealing with an wwwAuthenticateHeader or a
			// ProxyAuthenticationHeader one more time, we're needed to reply
			// with the same header type.
			// as the one we received from the proxy server.
			if (wwwAuthenticateHeader != null) {
				AuthorizationHeader ah = null;

				try {
					ah = headerFactory.createAuthorizationHeader("Digest");
					ah.setUsername(fromUser);
					ah.setRealm(realm);
					ah.setAlgorithm("MD5");
					ah.setURI(uri);
					ah.setNonce(nonce);

					ah.setResponse(toHexString(mdbytes));

					newRequest.setHeader(ah);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else { // if Proxy Authentication
				ProxyAuthorizationHeader pah = null;
				try {
					pah = headerFactory.createProxyAuthorizationHeader("Digest");
					pah.setUsername(fromUser);
					pah.setRealm(realm);
					pah.setAlgorithm("MD5");
					pah.setURI(uri);
					pah.setNonce(nonce);
					pah.setResponse(toHexString(mdbytes));

					newRequest.setHeader(pah);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			log.debug("********* New Request *******************");
			log.debug(newRequest);

			// Return the new request with the proper authorization header.
			return newRequest;

		}
	}

	public Request buildAck(Dialog dialog, Object content) throws SipException {

		// Request ackRequest = dialog.createRequest(Request.ACK);

		Request ackRequest = null;

		try {
			ackRequest = dialog.createAck(dialog.getLocalSeqNumber());
		} catch (InvalidArgumentException invalidArgExc) {
			invalidArgExc.printStackTrace();
		}

		if (content != null) {

			String contentString = null;
			if (content instanceof byte[]) {
				contentString = new String((byte[]) content);

			} else if (content instanceof String) {
				contentString = (String) content;
			}
			if (log.isDebugEnabled()) {
				log.debug("sendCalleeAck Content = " + contentString);
			}

			// TODO Change signature of setContent to accept object
			final byte[] sdpContent = contentString.getBytes();
			try {
				setContent(ackRequest, "application", "sdp", sdpContent);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ackRequest;
	}

	public void sendOk(Request request) throws ParseException, SipException {
		Response okResponse = messageFactory.createResponse(Response.OK, request);
		sipProvider.sendResponse(okResponse);
	}

	public void sendStatefulOk(RequestEvent event) throws ParseException, SipException, InvalidArgumentException {
		ServerTransaction tx = event.getServerTransaction();
		Request request = event.getRequest();

		Response response = messageFactory.createResponse(Response.OK, request);
		tx.sendResponse(response);

	}
}
