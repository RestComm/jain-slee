/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.enabler.xdmc;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.enabler.sip.ContentType;
import org.mobicents.slee.enabler.sip.EventPackageParameter;
import org.mobicents.slee.enabler.sip.Notify;
import org.mobicents.slee.enabler.sip.SubscriptionClientChild;
import org.mobicents.slee.enabler.sip.SubscriptionClientChildSbbLocalObject;
import org.mobicents.slee.enabler.sip.SubscriptionClientParent;
import org.mobicents.slee.enabler.sip.SubscriptionData;
import org.mobicents.slee.enabler.sip.SubscriptionException;
import org.mobicents.slee.enabler.sip.SubscriptionRequestContent;
import org.mobicents.slee.enabler.sip.SubscriptionStatus;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff;
import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientActivityContextInterfaceFactory;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;
import org.mobicents.xcap.client.header.Header;

/**
 * XDM Client SLEE Enabler.
 * 
 * @author martins
 * 
 */
public abstract class XDMClientChildSbb implements Sbb, XDMClientChild,
		SubscriptionClientParent {

	private static final JAXBContext xcapDiffJaxbContext = initXcapDiffJaxbContext();

	private static Tracer tracer;

	protected SbbContextExt sbbContext;

	protected XCAPClientResourceAdaptorSbbInterface xcapClientSbbInterface = null;
	protected XCAPClientActivityContextInterfaceFactory xcapClientACIF = null;

	// -- SBB LOCAL OBJECT METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#getCredentialsFactory()
	 */

	public CredentialsFactory getCredentialsFactory() {
		return xcapClientSbbInterface.getCredentialsFactory();
	}

	private static JAXBContext initXcapDiffJaxbContext() {
		try {
			return JAXBContext
					.newInstance("org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff");
		} catch (JAXBException e) {
			tracer.severe("failed to create jaxb context", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#delete(java.net.URI,
	 * org.mobicents.xcap.client.auth.Credentials)
	 */

	public void delete(URI uri, Credentials credentials) throws IOException {
		delete(uri, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#delete(java.net.URI,
	 * java.lang.String)
	 */

	public void delete(URI uri, String assertedUserId) throws IOException {
		delete(uri, assertedUserId, null);
	}

	private void delete(URI uri, String assertedUserId, Credentials credentials)
			throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Deleting " + uri);
		}
		getAsyncActivity().delete(uri,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	private Header[] getAssertedUserIdHeaders(String assertedUserId) {
		Header[] headers = null;
		if (assertedUserId != null) {
			headers = new Header[1];
			headers[0] = xcapClientSbbInterface.getHeaderFactory()
					.getBasicHeader(
							XcapConstant.HEADER_X_3GPP_Asserted_Identity,
							assertedUserId);
			;
		}
		return headers;
	}

	private AsyncActivity getAsyncActivity() throws IOException {
		final ActivityContextInterface[] activities = sbbContext
				.getActivities();
		if (activities.length != 0) {
			return (AsyncActivity) activities[0].getActivity();
		} else {
			try {
				final AsyncActivity activity = xcapClientSbbInterface
						.createActivity();
				final ActivityContextInterface aci = xcapClientACIF
						.getActivityContextInterface(activity);
				aci.attach(sbbContext.getSbbLocalObject());
				return activity;
			} catch (Exception e) {
				throw new IOException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#deleteIfMatch(java.net
	 * .URI, java.lang.String, org.mobicents.xcap.client.auth.Credentials)
	 */

	public void deleteIfMatch(URI uri, String eTag, Credentials credentials)
			throws IOException {
		deleteIfMatch(uri, eTag, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#deleteIfMatch(java.net
	 * .URI, java.lang.String, java.lang.String)
	 */

	public void deleteIfMatch(URI uri, String eTag, String assertedUserId)
			throws IOException {
		deleteIfMatch(uri, eTag, assertedUserId, null);
	}

	private void deleteIfMatch(URI uri, String eTag, String assertedUserId,
			Credentials credentials) throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Deleting " + uri + " if eTag matches " + eTag);
		}
		getAsyncActivity().deleteIfMatch(uri, eTag,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#deleteIfNoneMatch(java
	 * .net.URI, java.lang.String, org.mobicents.xcap.client.auth.Credentials)
	 */

	public void deleteIfNoneMatch(URI uri, String eTag, Credentials credentials)
			throws IOException {
		deleteIfNoneMatch(uri, eTag, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#deleteIfNoneMatch(java
	 * .net.URI, java.lang.String, java.lang.String)
	 */

	public void deleteIfNoneMatch(URI uri, String eTag, String assertedUserId)
			throws IOException {
		deleteIfNoneMatch(uri, eTag, assertedUserId, null);
	}

	private void deleteIfNoneMatch(URI uri, String eTag, String assertedUserId,
			Credentials credentials) throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Deleting " + uri + " if eTag does not matches " + eTag);
		}
		getAsyncActivity().deleteIfNoneMatch(uri, eTag,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientControl#get(java.net.URI,
	 * org.mobicents.xcap.client.auth.Credentials)
	 */

	public void get(URI uri, Credentials credentials) throws IOException {
		get(uri, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientControl#get(java.net.URI,
	 * java.lang.String)
	 */

	public void get(URI uri, String assertedUserId) throws IOException {
		get(uri, assertedUserId, null);

	}

	private void get(URI uri, String assertedUserId, Credentials credentials)
			throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Retrieving " + uri);
		}
		getAsyncActivity().get(uri, getAssertedUserIdHeaders(assertedUserId),
				credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientControl#put(java.net.URI,
	 * java.lang.String, byte[], org.mobicents.xcap.client.auth.Credentials)
	 */

	public void put(URI uri, String mimetype, byte[] content,
			Credentials credentials) throws IOException {
		put(uri, mimetype, content, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientControl#put(java.net.URI,
	 * java.lang.String, byte[], java.lang.String)
	 */

	public void put(URI uri, String mimetype, byte[] content,
			String assertedUserId) throws IOException {
		put(uri, mimetype, content, assertedUserId, null);
	}

	private void put(URI uri, String mimetype, byte[] content,
			String assertedUserId, Credentials credentials) throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Putting " + uri + ". Mimetype: " + mimetype
					+ ", Content: " + new String(content));
		}
		getAsyncActivity().put(uri, mimetype, content,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */

	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException {
		putIfMatch(uri, eTag, mimetype, content, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[], java.lang.String)
	 */

	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException {
		putIfMatch(uri, eTag, mimetype, content, assertedUserId, null);
	}

	private void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId, Credentials credentials)
			throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Putting " + uri + " if eTag matches " + eTag
					+ ". Mimetype: " + mimetype + ", Content: "
					+ new String(content));
		}
		getAsyncActivity().putIfMatch(uri, eTag, mimetype, content,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#putIfNoneMatch(java.
	 * net.URI, java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */

	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException {
		putIfNoneMatch(uri, eTag, mimetype, content, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#putIfNoneMatch(java.
	 * net.URI, java.lang.String, java.lang.String, byte[], java.lang.String)
	 */

	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException {
		putIfNoneMatch(uri, eTag, mimetype, content, assertedUserId, null);
	}

	private void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId, Credentials credentials)
			throws IOException {
		if (tracer.isFineEnabled()) {
			tracer.fine("Putting " + uri + " if eTag does not matches " + eTag
					+ ". Mimetype: " + mimetype + ", Content: "
					+ new String(content));
		}
		getAsyncActivity().putIfNoneMatch(uri, eTag, mimetype, content,
				getAssertedUserIdHeaders(assertedUserId), credentials);
	}

	// EVENT HANDLERS FOR XCAP REQUESTS

	protected XDMClientParentSbbLocalObject getParent() {
		return (XDMClientParentSbbLocalObject) sbbContext.getSbbLocalObject()
				.getParent();
	}

	/**
	 * Handles XCAP DELETE response events.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onDeleteResponseEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to delete " + event.getURI(),
						event.getException());
			}

			getParent().deleteResponse(event.getURI(), 500, null, null);
		} else {
			final XcapResponse response = event.getResponse();
			if (tracer.isInfoEnabled()) {
				if (response.getCode() == 200) {
					tracer.info("Deleted " + event.getURI() + ". ETag:"
							+ response.getETag());
				} else {
					tracer.info("Failed to delete " + event.getURI()
							+ ". Response: " + response);
				}
			}
			getParent().deleteResponse(event.getURI(), response.getCode(),
					response.getEntity().getContentAsString(),
					response.getETag());
		}
	}

	/**
	 * Handles XCAP GET response events.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onGetResponseEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to retrieve " + event.getURI(),
						event.getException());
			}
			getParent().getResponse(event.getURI(), 500, null, null, null);
		} else {
			final XcapResponse response = event.getResponse();
			if (tracer.isInfoEnabled()) {
				if (response.getCode() == 200) {
					tracer.info("Retrieved " + event.getURI());
				} else {
					tracer.info("Failed to retrieve " + event.getURI()
							+ ". Response: " + response);
				}
			}
			getParent().getResponse(event.getURI(), response.getCode(),
					response.getMimetype(),
					response.getEntity().getContentAsString(),
					response.getETag());
		}
	}

	/**
	 * Handles XCAP PUT response events.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onPutResponseEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to put " + event.getURI(),
						event.getException());
			}
			getParent().putResponse(event.getURI(), 500, null, null);
		} else {
			final XcapResponse response = event.getResponse();
			if (tracer.isInfoEnabled()) {
				if (response.getCode() == 200 || response.getCode() == 201) {
					tracer.info("Put " + event.getURI());
				} else {
					tracer.info("Failed to put " + event.getURI()
							+ ". Response: " + response);
				}
			}
			getParent().putResponse(event.getURI(), response.getCode(),
					response.getEntity().getContentAsString(),
					response.getETag());
		}
	}

	// -------------------- SUBSCRIBE part --------------------
	// static and helpers and "to override"
	public static final ContentType CONTENT_TYPE = new ContentType().setType("application").setSubType("resource-lists+xml");
	public static final ContentType[] ACCEPTED_CONTENT_TYPES = { new ContentType().setType("application").setSubType("xcap-diff+xml")};
	public static final String EVENT_PACKAGE = "xcap-diff";
	public static final EventPackageParameter[] EVENT_PARAMETERS = { new EventPackageParameter().setName("diff-processing").setValue("aggregate")};
	
	private static final String RESOURCE_LISTS_DOC_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resource-lists xmlns=\"urn:ietf:params:xml:ns:resource-lists\"><list>"; 
	private static final String RESOURCE_LISTS_DOC_SUFFIX = "</list></resource-lists>";
	private static final String RESOURCE_LISTS_ENTRY_PREFIX = "<entry uri=\""; 
	private static final String RESOURCE_LISTS_ENTRY_SUFFIX = "\"/>";
	
	public void subscribe(String subscriber, String notifier, int expires,
			String[] resourceURIs) throws SubscriptionException {

		if (resourceURIs == null || resourceURIs.length == 0) {
			throw new SubscriptionException("no resources to subscribe");
		}
		
		StringBuilder content = new StringBuilder(RESOURCE_LISTS_DOC_PREFIX);
		for (String uri : resourceURIs) {
			content.append(RESOURCE_LISTS_ENTRY_PREFIX).append(uri).append(RESOURCE_LISTS_ENTRY_SUFFIX);
		}
		content.append(RESOURCE_LISTS_DOC_SUFFIX);
		SubscriptionClientChild child = createSubscriptionChild(
				subscriber, notifier);
		SubscriptionRequestContent subscriptionContent = new SubscriptionRequestContent().setContent(content.toString()).setContentType(CONTENT_TYPE);
		SubscriptionData subscriptionData = new SubscriptionData().setSubscriberURI(subscriber).setNotifierURI(notifier).setEventPackage(EVENT_PACKAGE).setExpires(expires).setAcceptedContentTypes(ACCEPTED_CONTENT_TYPES).setEventParameters(EVENT_PARAMETERS);
		child.subscribe(subscriptionData, subscriptionContent);
	}

	public void unsubscribe(String subscriber, String notifier) {
		SubscriptionClientChild child = getSubscriptionChild(
				subscriber, notifier);
		if (child != null) {
			try {
				child.unsubscribe();
				// NOTE: no need to retthrow, pass up?
			} catch (SubscriptionException e) {
				tracer.severe("Failed to unsubscribe enabler!", e);
				try {
					((SubscriptionClientChildSbbLocalObject) child).remove();
				} catch (Exception ee) {
					tracer.severe("Failed on clean attempt!", ee);
				}
			}
		} else {
			if (tracer.isWarningEnabled()) {
				tracer.warning("No Subscribtion Enabler for notifier: "
						+ notifier);
			}
		}
	}

	// SUBSCRIBE callbacks
	public void onNotify(Notify ntfy,
			SubscriptionClientChildSbbLocalObject subscriptionChild) {
		// compile diff
		if (ntfy.getStatus().equals(SubscriptionStatus.terminated)) {
			try {
				final String notifier = ntfy.getNotifier();
				subscriptionChild.remove();
				getParent()
						.subscriptionTerminated(
								(XDMClientChildSbbLocalObject) this.sbbContext
										.getSbbLocalObject(),
								notifier, ntfy.getTerminationReason());
			} catch (Exception e) {
				tracer.severe("Unexpected exception on callback!", e);
			}
		} else {
			// cast is safe, cause we expect diff and check MIME
			XcapDiff diff = null;
			if (ntfy != null) {
				try {
					diff = (XcapDiff) xcapDiffJaxbContext.createUnmarshaller()
							.unmarshal(new StringReader(ntfy.getContent()));
				} catch (Exception e) {
					tracer.severe("Failed to parse diff!", e);
				}
			}
			getParent().subscriptionNotification(diff, ntfy.getStatus());
		}

	}

	public void resubscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject subscriptionChild) {

		try {
			final String notifier = subscriptionChild.getSubscriptionData().getNotifierURI();
			subscriptionChild.remove();
			getParent().resubscribeFailed(
					responseCode,
					(XDMClientChildSbbLocalObject) this.sbbContext
							.getSbbLocalObject(), notifier);
		} catch (Exception e) {
			tracer.severe("Unexpected exception on callback!", e);
		}
	}

	public void subscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject subscriptionChild) {

		try {
			final String notifier = subscriptionChild.getSubscriptionData().getNotifierURI();
			subscriptionChild.remove();

			getParent().subscribeFailed(
					responseCode,
					(XDMClientChildSbbLocalObject) this.sbbContext
							.getSbbLocalObject(), notifier);
		} catch (Exception e) {
			tracer.severe("Unexpected exception on callback!", e);
		}
	}

	// two easy cases.

	public void unsubscribeFailed(int arg0,
			SubscriptionClientChildSbbLocalObject subscriptionChild) {
		try {
			final String notifier = subscriptionChild.getSubscriptionData().getNotifierURI();
			subscriptionChild.remove();
			getParent().unsubscribeFailed(
					arg0,
					(XDMClientChildSbbLocalObject) this.sbbContext
							.getSbbLocalObject(), notifier);
		} catch (Exception e) {
			tracer.severe("Unexpected exception on callback!", e);
		}
	}

	private String getSIPSubscriptionClientChildName(String subscriber,
			String notifier) {
		return new StringBuilder(subscriber).append('@').append(notifier)
				.toString();
	}

	private SubscriptionClientChild createSubscriptionChild(String subscriber,
			String notifier) throws SubscriptionException {
		try {
			return (SubscriptionClientChild) getSubscriptionClientChildSbbChildRelation()
					.create(getSIPSubscriptionClientChildName(subscriber,
							notifier));
		} catch (Exception e) {
			throw new SubscriptionException(e);
		}
	}

	private SubscriptionClientChild getSubscriptionChild(String subscriber,
			String notifier) {
		return (SubscriptionClientChild) getSubscriptionClientChildSbbChildRelation()
				.get(getSIPSubscriptionClientChildName(subscriber, notifier));
	}

	// child relation methods

	public abstract ChildRelationExt getSubscriptionClientChildSbbChildRelation();

	// SBB OBJECT LIFECYCLE METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */

	public void sbbActivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */

	public void sbbCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 * java.lang.Object, javax.slee.ActivityContextInterface)
	 */

	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */

	public void sbbLoad() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */

	public void sbbPassivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */

	public void sbbPostCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	public void sbbRemove() {
		// terminate the xcap activity, if exists
		ActivityContextInterface[] activities = sbbContext.getActivities();
		if (activities.length != 0) {
			activities[0].detach(sbbContext.getSbbLocalObject());
			((AsyncActivity) activities[0].getActivity()).endActivity();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */

	public void sbbRolledBack(RolledBackContext arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */

	public void sbbStore() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = (SbbContextExt) sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(XDMClientChildSbb.class
					.getSimpleName());
		}
		try {
			Context context = (Context) new InitialContext()
					.lookup("java:comp/env");
			xcapClientSbbInterface = (XCAPClientResourceAdaptorSbbInterface) context
					.lookup("slee/resources/xcapclient/2.0/sbbrainterface");
			xcapClientACIF = (XCAPClientActivityContextInterfaceFactory) context
					.lookup("slee/resources/xcapclient/2.0/acif");
		} catch (NamingException e) {
			tracer.severe("Can't set sbb context.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

}
