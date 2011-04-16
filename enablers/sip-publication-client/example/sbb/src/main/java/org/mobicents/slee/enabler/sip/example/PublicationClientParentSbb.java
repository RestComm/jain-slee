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
package org.mobicents.slee.enabler.sip.example;


import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.enabler.sip.PublicationClientChildSbbLocalObject;
import org.mobicents.slee.enabler.sip.PublicationClientParent;

/**
 * @author baranowb
 *
 */
public abstract class PublicationClientParentSbb implements Sbb, PublicationClientParent {
	
	private static Tracer tracer;

	protected SbbContext sbbContext;
	
	protected static final String fancyXML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<presence xmlns=\"urn:ietf:params:xml:ns:pidf\" entity=\"sip:alan@127.0.0.1:5090\">"+
"	<tuple id=\"1a\">"+
"		<status>"+
"        	<basic>open</basic>"+
"		</status>"+
"		<contact priority=\"0.8\">sip:alan@127.0.0.1:5060</contact>"+
"		<timestamp>2001-10-27T16:49:29Z</timestamp>"+
"	</tuple>"+
"</presence>";

	//////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
    //////////////////////////////////

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
			tracer = sbbContext.getTracer(PublicationClientParentSbb.class.getSimpleName());
		}
		
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}
	
	public abstract void setUpdateCMP(boolean b);
	public abstract boolean getUpdateCMP();
	
	public abstract ChildRelationExt getPublicationClientChildSbbChildRelation();
	
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		PublicationClientChildSbbLocalObject child;
		try {
			child = (PublicationClientChildSbbLocalObject)this.getPublicationClientChildSbbChildRelation().create("default");
			child.newPublication("sip:alan@127.0.0.1:5090", "presence", fancyXML, "application", "pidf+xml", 61);
		} catch (Throwable e) {
			tracer.severe("failure",e);
		}
	}
	
	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: "+aci.getActivity());
		if(aci.getActivity() instanceof ServiceActivity) {
			PublicationClientChildSbbLocalObject child = (PublicationClientChildSbbLocalObject)this.getPublicationClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME);
			try {
				child.removePublication();
			} catch (Throwable e) {
				tracer.severe("failure",e);
			}

		}
	}
	
	@Override
	public void newPublicationSucceed(PublicationClientChildSbbLocalObject child) {
		//lets update. just to see more flow.
		tracer.info("newPublicationSucceed()");
		try {
			child.modifyPublication(fancyXML, "application", "pidf+xml", 61);
		} catch (Throwable e) {
			tracer.severe("failure",e);
		}
	}
	
	@Override
	public void modifyPublicationSucceed(
			PublicationClientChildSbbLocalObject child) {
		tracer.info("modifyPublicationSucceed()");
	}

	@Override
	public void removePublicationSucceed(
			PublicationClientChildSbbLocalObject child) {
		tracer.info("removePublicationSucceed()");
		child.remove();
	}
	
	@Override
	public void newPublicationFailed(int errorCode, PublicationClientChildSbbLocalObject child) {
		tracer.info("newPublicationFailed( error = "+errorCode+")");
		child.remove();
	}

	@Override
	public void modifyPublicationFailed(int errorCode,
			PublicationClientChildSbbLocalObject child) {
		tracer.info("modifyPublicationFailed( error = "+errorCode+")");
		child.remove();
	}
	
	@Override
	public void refreshPublicationFailed(int errorCode, PublicationClientChildSbbLocalObject child) {
		tracer.info("refreshPublicationFailed( error = "+errorCode+")");
		child.remove();		
	}

	@Override
	public void removePublicationFailed(int errorCode, PublicationClientChildSbbLocalObject child) {
		tracer.info("removePublicationFailed( error = "+errorCode+")");
		child.remove();		
	}

}
