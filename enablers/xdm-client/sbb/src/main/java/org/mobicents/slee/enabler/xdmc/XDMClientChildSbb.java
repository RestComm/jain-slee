package org.mobicents.slee.enabler.xdmc;

import java.io.IOException;
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
public abstract class XDMClientChildSbb implements Sbb, XDMClientChild {

	private static Tracer tracer;

	protected SbbContext sbbContext;

	protected XCAPClientResourceAdaptorSbbInterface xcapClientSbbInterface = null;
	protected XCAPClientActivityContextInterfaceFactory xcapClientACIF = null;

	// -- SBB LOCAL OBJECT METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#getCredentialsFactory()
	 */
	@Override
	public CredentialsFactory getCredentialsFactory() {
		return xcapClientSbbInterface.getCredentialsFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#delete(java.net.URI,
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	@Override
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
	@Override
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
		try {
			AsyncActivity activity = xcapClientSbbInterface.createActivity();
			ActivityContextInterface aci = xcapClientACIF
					.getActivityContextInterface(activity);
			aci.attach(sbbContext.getSbbLocalObject());
			return activity;
		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#deleteIfMatch(java.net
	 * .URI, java.lang.String, org.mobicents.xcap.client.auth.Credentials)
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void deleteIfNoneMatch(URI uri, String eTag, String assertedUserId)
			throws IOException {
		deleteIfNoneMatch(uri, eTag, assertedUserId, null);
	}

	private void deleteIfNoneMatch(URI uri, String eTag, String assertedUserId,
			Credentials credentials) throws IOException {
		if (tracer.isFineEnabled()) {
			tracer
					.fine("Deleting " + uri + " if eTag does not matches "
							+ eTag);
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
	@Override
	public void get(URI uri, Credentials credentials) throws IOException {
		get(uri, null, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientControl#get(java.net.URI,
	 * java.lang.String)
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientControl#setParentSbb(org.mobicents
	 * .slee.enabler.xdmc.XDMClientControlParentSbbLocalObject)
	 */
	@Override
	public void setParentSbb(XDMClientParentSbbLocalObject parentSbb) {
		setParentSbbCMP(parentSbb);
	}

	// EVENT HANDLERS FOR XCAP REQUESTS

	/**
	 * Handles XCAP DELETE response events.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onDeleteResponseEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		aci.detach(sbbContext.getSbbLocalObject());
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to delete " + event.getURI(),event.getException());
			}
			getParentSbbCMP().deleteResponse(event.getURI(), 500, null, null);
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
			getParentSbbCMP().deleteResponse(event.getURI(),
					response.getCode(),
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
		aci.detach(sbbContext.getSbbLocalObject());
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to retrieve " + event.getURI(),event.getException());
			}
			getParentSbbCMP()
					.getResponse(event.getURI(), 500, null, null, null);
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
			getParentSbbCMP().getResponse(event.getURI(), response.getCode(),
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
		aci.detach(sbbContext.getSbbLocalObject());
		if (event.getException() != null) {
			if (tracer.isInfoEnabled()) {
				tracer.info("Failed to put " + event.getURI(),event.getException());
			}
			getParentSbbCMP().putResponse(event.getURI(), 500, null, null);
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
			getParentSbbCMP().putResponse(event.getURI(), response.getCode(),
					response.getEntity().getContentAsString(),
					response.getETag());
		}
	}

	// CMP FIELDs

	/**
	 * Setter for parentSbbCMP cmp field.
	 * 
	 * @param parentSbb
	 */
	public abstract void setParentSbbCMP(
			XDMClientParentSbbLocalObject parentSbb);

	/**
	 * Getter for parentSbbCMP cmp field.
	 * 
	 * @return
	 */
	public abstract XDMClientParentSbbLocalObject getParentSbbCMP();

	// SBB OBJECT LIFECYCLE METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	@Override
	public void sbbActivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	@Override
	public void sbbCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 * java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	@Override
	public void sbbLoad() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	@Override
	public void sbbPassivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	@Override
	public void sbbPostCreate() throws CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	@Override
	public void sbbRemove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	@Override
	public void sbbRolledBack(RolledBackContext arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	@Override
	public void sbbStore() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	@Override
	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
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
	@Override
	public void unsetSbbContext() {
		this.sbbContext = null;
	}

}
