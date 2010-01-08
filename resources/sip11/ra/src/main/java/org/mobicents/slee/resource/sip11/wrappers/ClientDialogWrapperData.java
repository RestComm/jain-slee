package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.RouteHeader;

public class ClientDialogWrapperData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Used to detect how route set has been created - in case of responses we
	 * want to override local route set, since in case of forks, route set on
	 * request will be propably bad :)
	 */
	private boolean routeSetOnRequest = false;

	private SipURI requestURI;

	private AtomicLong localSequenceNumber = new AtomicLong(0);
	
	// Comment: JSIP set ToTag and route list on first provisional response
	// with ToTag and 2xx, so we have to keep track of those for each activity,
	// in order to allow it to send responses to proper peer.
	// This is due to having one SIPDialog for all forks - this is standard for
	// no auto dialog support.

	/**
	 * Used when: {@link #forkInitialActivityHandle} !=null or Dialog is null.
	 */
	private transient ArrayList<RouteHeader> localRouteSet;

	private ClientDialogForkHandler forkHandler;
	
	private Address fromAddress, toAddress;
	private CallIdHeader customCallId = null;
	private String localRemoteTag = null;

	private static final RouteHeader[] EMPTY_RH_ARRAY = {};

	public ClientDialogWrapperData(ClientDialogForkHandler forkHandler) {
		this.forkHandler = forkHandler;
		localRouteSet = new ArrayList<RouteHeader>();		
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {

		// write everything not static or transient
		stream.defaultWriteObject();

		if (forkHandler.isForking()) {
			final RouteHeader[] routeHeaders = localRouteSet
					.toArray(EMPTY_RH_ARRAY);
			stream.writeObject(routeHeaders);
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {

		stream.defaultReadObject();

		localRouteSet = new ArrayList<RouteHeader>();
		
		if (forkHandler.isForking()) {
			for (RouteHeader routeHeader : (RouteHeader[]) stream.readObject()) {
				localRouteSet.add(routeHeader);
			}
		}
	}

	/**
	 * @return the forkData
	 */
	public ClientDialogForkHandler getForkHandler() {
		return forkHandler;
	}

	public boolean isRouteSetOnRequest() {
		return routeSetOnRequest;
	}

	public void setRouteSetOnRequest(boolean routeSetOnRequest) {
		this.routeSetOnRequest = routeSetOnRequest;
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

	public List<RouteHeader> getRouteSet() {
		return new ArrayList<RouteHeader>(this.localRouteSet);
	}

	public void addToRouteSet(RouteHeader routeHeader) {
		localRouteSet.add(routeHeader);
	}

	public void addAlltoRouteSet(List<RouteHeader> routeList) {
		localRouteSet.addAll(routeList);
	}

	public void clearRouteSet() {
		localRouteSet.clear();
	}

	
}
