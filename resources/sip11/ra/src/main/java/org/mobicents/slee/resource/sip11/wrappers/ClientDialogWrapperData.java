package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.RouteHeader;

import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ClientDialogWrapperData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient ClientDialogWrapper owner;
	
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
	 * Indicates sip activity handle for initial dialog activity created for
	 * this dialog - created as first. Child dialogs are represented by dialog
	 * activities with the same wrappedDialog object.
	 * wrappedDialog.getApplicationData() returns always initial dialog activity
	 * or one that won race to get 2xx response
	 */
	private DialogWithoutIdActivityHandle forkInitialActivityHandle;

	private DialogForkState forkState = DialogForkState.AWAIT_FIRST_TAG;

	/**
	 * Used when: {@link #forkInitialActivityHandle} !=null or Dialog is null.
	 */
	private transient ArrayList<RouteHeader> localRouteSet;

	/**
	 * Contains activity handles of fork children.
	 */
	private transient ConcurrentHashMap<String, DialogWithoutIdActivityHandle> toTag2DialogHandle;
	
	private Address fromAddress, toAddress;
	private CallIdHeader customCallId = null;
	private String localRemoteTag = null;

	private SipActivityHandle[] EMPTY_HANDLE_ARRAY = {};
	private RouteHeader[] EMPTY_RH_ARRAY = {};

	public ClientDialogWrapperData(ClientDialogWrapper owner) {
		this.owner = owner;
		localRouteSet = new ArrayList<RouteHeader>();
		toTag2DialogHandle = new ConcurrentHashMap<String, DialogWithoutIdActivityHandle>();
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {

		// write everything not static or transient
		stream.defaultWriteObject();

		if (isInForkedActions()) {
			final SipActivityHandle[] childDialogHandles = toTag2DialogHandle
					.values().toArray(EMPTY_HANDLE_ARRAY);
			stream.writeObject(childDialogHandles);
			final RouteHeader[] routeHeaders = localRouteSet
					.toArray(EMPTY_RH_ARRAY);
			stream.writeObject(routeHeaders);
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {

		stream.defaultReadObject();

		localRouteSet = new ArrayList<RouteHeader>();
		toTag2DialogHandle = new ConcurrentHashMap<String, DialogWithoutIdActivityHandle>();

		if (isInForkedActions()) {
			for (SipActivityHandle sipActivityHandle : (SipActivityHandle[]) stream
					.readObject()) {
				DialogWithoutIdActivityHandle dwiah = (DialogWithoutIdActivityHandle) sipActivityHandle;
				toTag2DialogHandle.put(dwiah.getRemoteTag(), dwiah);
			}
			for (RouteHeader routeHeader : (RouteHeader[]) stream.readObject()) {
				localRouteSet.add(routeHeader);
			}
		}
	}

	public ClientDialogWrapper getOwner() {
		return owner;
	}

	public void setOwner(ClientDialogWrapper owner) {
		this.owner = owner;
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

	public DialogWithoutIdActivityHandle getForkInitialActivityHandle() {
		return forkInitialActivityHandle;
	}

	public void setForkInitialActivityHandle(
			DialogWithoutIdActivityHandle forkInitialActivityHandle) {
		this.forkInitialActivityHandle = forkInitialActivityHandle;
	}

	public DialogForkState getForkState() {
		return forkState;
	}

	public void setForkState(DialogForkState forkState) {
		this.forkState = forkState;
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
	
	public boolean isInForkedActions() {
		return this.forkInitialActivityHandle != null
				|| this.forkState == DialogForkState.AWAIT_FINAL;
	}

	public List<RouteHeader> getRouteSet() {
		return new ArrayList<RouteHeader>(this.localRouteSet);
	}

	public Set<String> getTagsMappedToDialogs() {
		return toTag2DialogHandle.keySet();
	}

	public DialogWithoutIdActivityHandle getDialogMappedToTag(String tag) {
		return toTag2DialogHandle.get(tag);
	}

	public DialogWithoutIdActivityHandle unmapDialogMappedToTag(String tag) {
		return toTag2DialogHandle.remove(tag);
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

	public void mapTagToDialog(String tag,
			DialogWithoutIdActivityHandle handle) {
		toTag2DialogHandle.put(tag, handle);
	}

	public boolean tagIsMapped(String tag) {
		return toTag2DialogHandle.containsKey(tag);
	}
}
