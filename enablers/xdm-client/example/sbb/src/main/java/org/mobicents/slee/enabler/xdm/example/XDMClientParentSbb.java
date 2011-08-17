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

package org.mobicents.slee.enabler.xdm.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.enabler.sip.SubscriptionStatus;
import org.mobicents.slee.enabler.sip.TerminationReason;
import org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject;
import org.mobicents.slee.enabler.xdmc.XDMClientParent;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff;
import org.mobicents.xcap.client.uri.DocumentSelectorBuilder;
import org.mobicents.xcap.client.uri.UriBuilder;

/**
 * @author baranowb
 * 
 */
public abstract class XDMClientParentSbb implements Sbb, XDMClientParent {

	private static Tracer tracer;

	private enum StateMachine {
		created, updated, deleted
	};

	protected SbbContextExt sbbContext;

	// protected String user = "clara";

	private String user = "sip:clara@127.0.0.1";
	private String documentName = "index";

	// ////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
	// ////////////////////////////////

	@Override
	public void sbbActivate() {
	}

	@Override
	public void sbbCreate() throws CreateException {
	}

	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
	}

	@Override
	public void sbbLoad() {
	}

	@Override
	public void sbbPassivate() {
	}

	@Override
	public void sbbPostCreate() throws CreateException {
	}

	@Override
	public void sbbRemove() {
	}

	@Override
	public void sbbRolledBack(RolledBackContext arg0) {
	}

	@Override
	public void sbbStore() {
	}

	@Override
	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = (SbbContextExt) sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(XDMClientParentSbb.class
					.getSimpleName());
		}

		// get some conf
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			String user = (String) myEnv.lookup("user");
			if (user != null) {
				this.user = user;
			}

			String document = (String) myEnv.lookup("document");
			if (document != null) {
				this.documentName = document;
			}

		} catch (NamingException e) {
			tracer.severe(e.getMessage(), e);
		}

	}

	@Override
	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public abstract ChildRelationExt getXDMClientChildSbbChildRelation();

	// ------------------ cmps --------------------

	public abstract void setStateMachine(StateMachine stateMachine);

	public abstract StateMachine getStateMachine();

	// ------------------ events -------------------

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {

		try {
			XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this
					.getXDMClientChildSbbChildRelation().create(ChildRelationExt.DEFAULT_CHILD_NAME);
			String[] resourceURIS = new String[] { getDocumentSelector() };
			// String aserted = null;
			// subscribe to changes - before doc is created.
			// assume we are bound to the same localhost.
			// from/to must match
			child.subscribe(user, user, 60, resourceURIS);
		} catch (Exception e) {
			tracer.severe("failed to subscribe changes to doc", e);
		}
	}

	public void onActivityEndEvent(javax.slee.ActivityEndEvent event,
			ActivityContextInterface aci) {
			((XDMClientChildSbbLocalObject) getXDMClientChildSbbChildRelation()
					.get(ChildRelationExt.DEFAULT_CHILD_NAME)).unsubscribe(user, user);		
	}

	// --------- SBB LO methods ----------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#getResponse(java.net.URI,
	 * int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void getResponse(URI uri, int responseCode, String mimetype,
			String content, String eTag) {
		tracer.info("getResponse(" + uri + ", " + responseCode + ", "
				+ mimetype + ", " + content + ", " + eTag + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#putResponse(java.net.URI,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public void putResponse(URI uri, int responseCode, String responseContent,
			String eTag) {
		tracer.info("putResponse(" + uri + ", " + responseCode + ", "
				+ responseContent + ", " + eTag + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#deleteResponse(java.net
	 * .URI, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteResponse(URI uri, int responseCode,
			String responseContent, String eTag) {
		tracer.info("deleteResponse(" + uri + ", " + responseCode + ", "
				+ responseContent + ", " + eTag + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#subscriptionTerminated
	 * (org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject,
	 * java.net.URI, org.mobicents.slee.enabler.sip.TerminationReason)
	 */
	@Override
	public void subscriptionTerminated(XDMClientChildSbbLocalObject child,
			String notifier, TerminationReason reason) {
		tracer.info("subscription terminated, reason = " + reason);
		getXDMClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME).remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.enabler.xdmc.XDMClientParent#subscribeFailed(int,
	 * org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject,
	 * java.net.URI)
	 */
	@Override
	public void subscribeFailed(int responseCode,
			XDMClientChildSbbLocalObject child, String notifier) {
		tracer.severe("Failed to subscribe, response = " + responseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#resubscribeFailed(int,
	 * org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject,
	 * java.net.URI)
	 */
	@Override
	public void resubscribeFailed(int responseCode,
			XDMClientChildSbbLocalObject child, String notifier) {
		tracer.severe("Failed to resubscribe, response = " + responseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#unsubscribeFailed(int,
	 * org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject,
	 * java.net.URI)
	 */
	@Override
	public void unsubscribeFailed(int responseCode,
			XDMClientChildSbbLocalObject child, String notifier) {
		tracer.severe("Failed to unsubscribe, response = " + responseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.enabler.xdmc.XDMClientParent#subscriptionNotification
	 * (org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff,
	 * org.mobicents.slee.enabler.sip.SubscriptionStatus)
	 */
	@Override
	public void subscriptionNotification(XcapDiff xcapDiff,
			SubscriptionStatus status) {
		tracer.info("subscriptionNotification( xcap diff = " + xcapDiff
				+ ", subscrption status = " + status + ")");
		if (status == SubscriptionStatus.active) {
			if (getStateMachine() == null) {
				tracer.info("subscription activated, creating doc");
				setStateMachine(StateMachine.created);
				putDocument(false);
			} else if (getStateMachine() == StateMachine.created) {
				tracer.info("notified that doc creation succeed, updating doc");
				setStateMachine(StateMachine.updated);
				putDocument(true);
			} else if (getStateMachine() == StateMachine.updated) {
				tracer.info("notified that doc update succeed, deleting doc");
				setStateMachine(StateMachine.deleted);
				deleteDocument();
			}
		}
	}

	// aux

	private void putDocument(boolean update) {
		XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this
				.getXDMClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME);
		String assertedIdentity = null;
		try {
			InputStream is = this.getClass().getResourceAsStream("example.xml");
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			String content = writer.toString();
			if (update) {
				content = content.replaceAll("presence.oma",
						"presence.mobicents");
			}
			child.put(createURI(), "application/rls-services+xml",
					content.getBytes(), assertedIdentity);
		} catch (IOException e) {
			tracer.severe("failed to put doc", e);
		}
	}

	private void deleteDocument() {
		XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this
				.getXDMClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME);
		URI xdmPutURI = createURI();
		String assertedIdentity = null;
		try {
			child.delete(xdmPutURI, assertedIdentity);
		} catch (IOException e) {
			tracer.severe("failed to delete doc", e);
		}
	}

	private String getDocumentSelector() {
		return DocumentSelectorBuilder.getUserDocumentSelectorBuilder(
				"rls-services", user, documentName).toPercentEncodedString();
	}

	private URI createURI() {
		try {
			// return new
			// URI("http://"+xmdAddress+":"+xdmPort+"/mobicents/rls-services/users/sip:"+user+"@"+xmdAddress+";pres-list=Default/index");
			String documentSelector = getDocumentSelector();
			// FIXME use configuration to set scheme and authority and xcap root
			UriBuilder uriBuilder = new UriBuilder()
					.setSchemeAndAuthority("http://127.0.0.1:8080")
					.setXcapRoot("/mobicents/")
					.setDocumentSelector(documentSelector);
			URI documentURI = uriBuilder.toURI();
			
			return documentURI;
		} catch (URISyntaxException e) {
			tracer.severe("failed to create uri", e);
		}
		return null;
	}
}
