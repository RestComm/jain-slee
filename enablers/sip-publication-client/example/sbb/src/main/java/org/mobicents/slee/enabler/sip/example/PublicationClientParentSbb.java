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
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.enabler.sip.PublicationClientChildSbbLocalObject;
import org.mobicents.slee.enabler.sip.PublicationClientParent;
import org.mobicents.slee.enabler.sip.PublicationClientParentSbbLocalObject;
import org.mobicents.slee.enabler.sip.PublicationException;
import org.mobicents.slee.enabler.sip.Result;




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
	
	public abstract ChildRelation getPublicationClientChildSbbChildRelation();
	
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		PublicationClientChildSbbLocalObject child;
		try {
			child = (PublicationClientChildSbbLocalObject)this.getPublicationClientChildSbbChildRelation().create();
			child.setParentSbb((PublicationClientParentSbbLocalObject) this.sbbContext.getSbbLocalObject());
			child.newPublication("alan@127.0.0.1:5090", "presence", fancyXML, "application", "pidf+xml", 61);
		} catch (TransactionRequiredLocalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PublicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: "+aci.getActivity());
		if(aci.getActivity() instanceof ServiceActivity)
		{
			ServiceActivity sa = (ServiceActivity) aci.getActivity();
			if(sa.getService().equals(this.sbbContext.getService()))
			{
				PublicationClientChildSbbLocalObject child = (PublicationClientChildSbbLocalObject)this.getPublicationClientChildSbbChildRelation().iterator().next();
				try {
					child.removePublication();
				} catch (PublicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void afterNewPublication(Result result, PublicationClientChildSbbLocalObject child) {
		tracer.info("GOT AFTER NEW PUB!: "+result);
		if(result.getStatusCode()>299)
		{
			//ie.
			child.remove();
		}
	}

	public void afterRefreshPublication(Result result, PublicationClientChildSbbLocalObject child) {
		tracer.info("GOT AFTER REF PUB!: "+result);

		if(result.getStatusCode()>299)
		{
			//ie.
			child.remove();
		}else
		{
			//lets update. just to see more flow.
			try {
				child.updatePublication(fancyXML, "application", "pidf+xml", 61);
			} catch (PublicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void afterUpdatePublication(Result result, PublicationClientChildSbbLocalObject child) {
		tracer.info("GOT AFTER UPD PUB!: "+result);
		if(result.getStatusCode()>299)
		{
			//ie.
			child.remove();
		}

	}

	public void afterRemovePublication(Result result, PublicationClientChildSbbLocalObject child) {
		tracer.info("GOT AFTER REM PUB!: "+result);
		child.remove();

	}

	@Override
	public void newPublicationFailed(PublicationClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("GOT NEW PUB FAILURE!");
		sbbLocalObject.remove();
	}

	@Override
	public void refreshPublicationFailed(PublicationClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("GOT REFRESH PUB FAILURE!");
		sbbLocalObject.remove();
		
	}

	@Override
	public void removePublicationFailed(PublicationClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("GOT REMOVE PUB FAILURE!");
		sbbLocalObject.remove();
		
	}

}
