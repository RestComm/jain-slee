package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.header.RouteList;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;

public class ClientDialogWrapperData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient SipURI requestURI;

	private transient AtomicLong localSequenceNumber = new AtomicLong(0);
	
	// Comment: JSIP set ToTag and route list on first provisional response
	// with ToTag and 2xx, so we have to keep track of those for each activity,
	// in order to allow it to send responses to proper peer.
	// This is due to having one SIPDialog for all forks - this is standard for
	// no auto dialog support.

	/**
	 * Used when: {@link #forkInitialActivityHandle} !=null or Dialog is null.
	 */
	private transient RouteList localRouteSet;

	private ClientDialogForkHandler forkHandler;
	
	private transient Address fromAddress, toAddress;
	private transient CallIdHeader customCallId;
	private transient String localRemoteTag;

	public ClientDialogWrapperData(ClientDialogForkHandler forkHandler) {
		this.forkHandler = forkHandler;
	}

	/**
	 * @return the forkData
	 */
	public ClientDialogForkHandler getForkHandler() {
		return forkHandler;
	}

	public SipURI getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(SipURI requestURI) {
		this.requestURI = requestURI;
	}

	public AtomicLong getLocalSequenceNumber() {
		return localSequenceNumber;
	}

	public void setLocalSequenceNumber(AtomicLong localSequenceNumber) {
		this.localSequenceNumber = localSequenceNumber;
	}

	public Address getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Address getToAddress() {
		return toAddress;
	}

	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}

	public CallIdHeader getCustomCallId() {
		return customCallId;
	}

	public void setCustomCallId(CallIdHeader callIdToReUse) {
		this.customCallId = callIdToReUse;
	}

	public String getLocalRemoteTag() {
		return localRemoteTag;
	}

	public void setLocalRemoteTag(String localRemoteTag) {
		this.localRemoteTag = localRemoteTag;
	}

	public RouteList getLocalRouteList() {
		return localRouteSet;
	}
	
	public void setLocalRouteList(RouteList routeList) {
		this.localRouteSet = routeList != null ? (RouteList) routeList.clone() : null;
	}
}
