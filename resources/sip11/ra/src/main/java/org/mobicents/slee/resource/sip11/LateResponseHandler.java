package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPRequest;

import java.util.ArrayList;
import java.util.List;

import javax.sip.ResponseEvent;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;

public class LateResponseHandler {

	private static Tracer tracer;
	
	/**
	 * 
	 * @param resp
	 * @param infoTrace
	 */
	public static void processLateInDialogResponse(ResponseEvent resp, SipResourceAdaptor ra) {
	
		if (tracer == null) {
			tracer = ra.getTracer(LateResponseHandler.class.getSimpleName());
		}
				
		final String method = ((CSeq) resp.getResponse().getHeader(CSeq.NAME)).getMethod();

		if (tracer.isInfoEnabled()) {
			tracer.info("ClientTransaction is null, possibly a late 2xx. ToTag[" + ((ToHeader) resp.getResponse().getHeader(ToHeader.NAME)).getTag() + "] Dialog[" + resp.getDialog() + "] CALLID[" + ((CallID) resp.getResponse().getHeader(CallID.NAME)).getCallId() + "] BRANCH[" + ((Via) resp.getResponse().getHeaders(Via.NAME).next()).getBranch()
					+ "] METHOD[" + method + "] CODE[" + resp.getResponse().getStatusCode() + "]");
		}

		if ((Utils.getDialogCreatingMethods().contains(method))) {

			if (tracer.isFineEnabled()) {
				tracer.fine("No Handle for dialog with such from and callId, using default. CALLID[" + ((CallID) resp.getResponse().getHeader(CallID.NAME)).getCallId() + "] BRANCH[" + ((Via) resp.getResponse().getHeaders(Via.NAME).next()).getBranch() + "] METHOD[" + method
						+ "] CODE[" + resp.getResponse().getStatusCode() + "]");
			}
			doTerminateOnLate2xx(resp,ra);
			
		}
	}
	
	/**
	 * Generare 200 and sends BYE if method == 200, possibly this shoudl also terminate subscriptions?
	 * @param respEvent
	 */
	public static void doTerminateOnLate2xx(ResponseEvent respEvent, SipResourceAdaptor ra) {

		if (tracer == null) {
			tracer = ra.getTracer(LateResponseHandler.class.getSimpleName());
		}
		
		final SleeSipProviderImpl provider = ra.getProviderWrapper();

		try {
			final Response response = respEvent.getResponse();
			final CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
			List<RouteHeader> routeSet = org.mobicents.slee.resource.sip11.Utils.getRouteList(response,provider.getHeaderFactory());
			SipURI requestURI = org.mobicents.slee.resource.sip11.Utils.getRequestUri(response, provider.getAddressFactory());
			String branch = ((ViaHeader)response.getHeaders(ViaHeader.NAME).next()).getBranch();
			
			long cseqNumber = cseq.getSeqNumber();
			long statusCode = response.getStatusCode();

			// logger.info("DOING FORGE FOR: \n"+response);
			if (cseq.getMethod().equals(Request.INVITE) && (statusCode < 300 && statusCode >= 200)) {
				if (requestURI == null) {
					tracer.severe("Cannot ack on request that has empty contact!!!!");
					return;
				}

				MaxForwardsHeader mf = provider.getHeaderFactory().createMaxForwardsHeader(70);
				List<ViaHeader> lst = new ArrayList<ViaHeader>(1);
				final ViaHeader localViaHeader = provider.getLocalVia();
				localViaHeader.setBranch(branch);
				lst.add(localViaHeader);
				Request forgedRequest = provider.getMessageFactory().createRequest(requestURI,Request.ACK,(CallIdHeader)response.getHeader(CallIdHeader.NAME),provider.getHeaderFactory().createCSeqHeader(cseqNumber, Request.ACK),
						(FromHeader)response.getHeader(FromHeader.NAME)	,(ToHeader)response.getHeader(ToHeader.NAME),lst,mf	);
				for (Header h : routeSet) {
					forgedRequest.addLast(h);
				}

				//forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), branch));
				// ITS BUG....
				//((SIPRequest) forgedRequest).setMethod(Request.ACK);
				if (tracer.isInfoEnabled()) {
					tracer.info("Sending request:\n"+forgedRequest);
				}
				provider.sendRequest(forgedRequest);

				//forgedRequest = this.provider.getMessageFactory().createRequest(null);
				lst = new ArrayList<ViaHeader>();
				lst.add(provider.getLocalVia());
				requestURI = org.mobicents.slee.resource.sip11.Utils.getRequestUri(response,provider.getAddressFactory());
				forgedRequest = provider.getMessageFactory().createRequest(requestURI,Request.BYE,(CallIdHeader)response.getHeader(CallIdHeader.NAME),provider.getHeaderFactory().createCSeqHeader(cseqNumber+1, Request.BYE),
						(FromHeader)response.getHeader(FromHeader.NAME)	,(ToHeader)response.getHeader(ToHeader.NAME),lst,mf	);
				
				for (Header h : routeSet) {
					forgedRequest.addLast(h);
				}

				//forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), null));
				// ITS BUG....
				((SIPRequest) forgedRequest).setMethod(Request.BYE);
				if (tracer.isInfoEnabled()) {
					tracer.info("Sending request:\n"+forgedRequest);
				}
				provider.sendRequest(forgedRequest);
			} else {
				// FIXME: add sometihng here?
			}
			// response.get
		} catch (Exception e) {
			tracer.severe(e.getMessage(),e);
		}

	}
}
