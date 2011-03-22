/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.enabler.xdmc.XDMClientChildSbbLocalObject;
import org.mobicents.slee.enabler.xdmc.XDMClientParent;
import org.mobicents.slee.enabler.xdmc.XDMClientParentSbbLocalObject;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff;
import org.mobicents.xcap.client.uri.DocumentSelectorBuilder;
import org.mobicents.xcap.client.uri.UriBuilder;

/**
 * @author baranowb
 * 
 */
public abstract class XDMClientParentSbb implements Sbb, XDMClientParent {
	private static Tracer tracer;

	protected SbbContext sbbContext;

	// protected String user = "clara";

	private String user = "sip:clara@127.0.0.1";
	private String documentName = "index";

	// ////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
	// ////////////////////////////////

	public void sbbActivate() {
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
	}

	public void sbbLoad() {
	}

	public void sbbPassivate() {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbRemove() {
	}

	public void sbbRolledBack(RolledBackContext arg0) {
	}

	public void sbbStore() {
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		if (tracer == null) {
			tracer = sbbContext.getTracer(XDMClientParentSbb.class.getSimpleName());
		}
		
		// get some conf
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			String user = (String) myEnv.lookup("user");
			if(user!=null)
			{
				this.user = user;
			}
			
			String document = (String) myEnv.lookup("document");
			if(document!=null)
			{
				this.documentName  = document;
			}
			
		} catch (NamingException e) {
			tracer.severe(e.getMessage(), e);
		}


	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public abstract ChildRelation getXDMClientChildSbbChildRelation();
	// ------------------ cmps --------------------
	
	public abstract void setSentUpdate(boolean f);
	
	public abstract boolean getSentUpdate();
	
	
	// ------------------ events -------------------
	
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {

		try {
			XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this.getXDMClientChildSbbChildRelation().create();
			String[] resourceURIS = new String[] {getDocumentSelector()};
			child.setParentSbb((XDMClientParentSbbLocalObject) this.sbbContext.getSbbLocalObject());
			// String aserted = null;
			// subscribe to changes - before doc is created.
			// assume we are bound to the same localhost.
			//from/to must match
			child.subscribe(new URI(user), new URI(user), 60, resourceURIS);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: " + aci.getActivity());
		if (aci.getActivity() instanceof ServiceActivity) {
			try {
				((XDMClientChildSbbLocalObject)getXDMClientChildSbbChildRelation().iterator().next()).unsubscribe(new URI(user), new URI(user));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * @return
	 */
	protected String getDocumentSelector() {
		
		return DocumentSelectorBuilder.getUserDocumentSelectorBuilder("rls-services", user, documentName)
		.toPercentEncodedString();
	}
	
	//public URI createURI(String xmdAddress, String xdmPort,String user)
	protected URI createURI() {
		try {
			// return new
			// URI("http://"+xmdAddress+":"+xdmPort+"/mobicents/rls-services/users/sip:"+user+"@"+xmdAddress+";pres-list=Default/index");
			String documentSelector = getDocumentSelector();
			UriBuilder uriBuilder = new UriBuilder().setSchemeAndAuthority("http://127.0.0.1:8080").setXcapRoot("/mobicents")
					.setDocumentSelector(documentSelector);
			URI documentURI = uriBuilder.toURI();
			return documentURI;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	protected String createDocument(boolean change)
	{

		try {
			InputStream is = this.getClass().getResourceAsStream("example.xml");
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			String content = writer.toString();
			if (change) {
				// TODO:
				content = content.replaceAll("presence.oma", "presence.mobicents");
			}
			is.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// --------- SBB LO methods ----------

	
	public void getResponse(URI uri, int responseCode, String mimetype, String content, String eTag) {
		tracer.info("getResponse(" + uri + ", " + responseCode + ", " + mimetype + ", " + content + ", " + eTag + ")");
	}

	
	public void putResponse(URI uri, int responseCode, String responseContent, String eTag) {
		tracer.info("putResponse(" + uri + ", " + responseCode + ", " + responseContent + ", " + eTag + ")");
		if(!getSentUpdate())
		{
			setSentUpdate(true);
			//now lets get doc, a bit modified.
			XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this.getXDMClientChildSbbChildRelation().iterator().next();
			URI xdmPutURI = createURI();
			String assertedIdentity = null;
			try {
				child.put(xdmPutURI, "application/rls-services+xml", createDocument(true).getBytes(), assertedIdentity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void deleteResponse(URI uri, int responseCode, String responseContent, String eTag) {
		tracer.info("deleteResponse(" + uri + ", " + responseCode + ", " + responseContent + ", " + eTag + ")");

	}

	
	public void subscriptionTerminated(XDMClientChildSbbLocalObject child, URI notifier) {
		tracer.info("subscriptionTerminated(" + notifier + ")");
		child.remove();
	}

	
	public void subscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier) {
		// TODO Auto-generated method stub

	}

	
	public void resubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier) {
		// TODO Auto-generated method stub

	}

	
	public void unsubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier) {
		// TODO Auto-generated method stub

	}

	
	public void subscribeSucceed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier) {
		XDMClientChildSbbLocalObject child = (XDMClientChildSbbLocalObject) this.getXDMClientChildSbbChildRelation().iterator().next();
		URI xdmPutURI = createURI();
		String assertedIdentity = null;
		try {
			child.put(xdmPutURI, "application/rls-services+xml", createDocument(false).getBytes(), assertedIdentity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void unsubscribeSucceed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscriptionNotification(XcapDiff xcapDiff) {
		tracer.info("subscriptionNotification(" + xcapDiff + ")");
		
	}
	
}
