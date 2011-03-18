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

import org.mobicents.slee.enabler.sip.Notify;
import org.mobicents.slee.enabler.sip.SubscriptionClientChildSbbLocalObject;
import org.mobicents.slee.enabler.sip.SubscriptionClientParent;
import org.mobicents.slee.enabler.sip.SubscriptionClientParentSbbLocalObject;
import org.mobicents.slee.enabler.sip.SubscriptionException;
import org.mobicents.slee.enabler.sip.SubscriptionStatus;

/**
 * @author baranowb
 * 
 */
public abstract class SubscriptionClientParentSbb implements Sbb, SubscriptionClientParent {
	private static Tracer tracer;

	protected SbbContext sbbContext;

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
			tracer = sbbContext.getTracer(SubscriptionClientParentSbb.class.getSimpleName());
		}

	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public abstract void setNotifyCount(int b);

	public abstract int getNotifyCount();

	public abstract ChildRelation getSubscriptionClientChildSbbChildRelation();

	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
		SubscriptionClientChildSbbLocalObject child;
		try {
			child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().create();
			child.setParentSbb((SubscriptionClientParentSbbLocalObject) this.sbbContext.getSbbLocalObject());
			child.subscribe("sip:14313471@127.0.0.1:5090", "secret_name", "sip:14313471@127.0.0.1:5090", 61, "presence",null, "application", "pidf+xml"); // null
		} catch (TransactionRequiredLocalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SubscriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
		if (tracer.isFineEnabled())
			tracer.fine("Received Activtiy End: " + aci.getActivity());
		if (aci.getActivity() instanceof ServiceActivity) {
			ServiceActivity sa = (ServiceActivity) aci.getActivity();
			if (sa.getService().equals(this.sbbContext.getService())) {
				// and we still have child - this will happen if we term service
				// before schedule end :)
				sendUnsubscribe();

			}
		}
	}

	@Override
	public void subscribeSucceed(int responseCode,
			SubscriptionClientChildSbbLocalObject enabler) {
		tracer.info("subscribeSucceed: " + responseCode);
	}

	@Override
	public void unsubscribeSucceed(int responseCode,
			SubscriptionClientChildSbbLocalObject enabler) {
		tracer.info("unsubscribeSucceed: " + responseCode);		
	}

	@Override
	public void onNotify(Notify notify, SubscriptionClientChildSbbLocalObject enabler) {
		tracer.info("onNotify: " + notify);

		// check num of notifs
		
		int nots = getNotifyCount() + 1;
		setNotifyCount(nots);
		if (nots == 3) {
			// kill baby
			sendUnsubscribe();
		}
		
		//subscription is terminated ONLY, when there is Notify with termination status
		if(notify.getStatus() == SubscriptionStatus.terminated)
		{
			//kill child
			ChildRelation childRelation = this.getSubscriptionClientChildSbbChildRelation();
			if (childRelation.size() > 0) {
				SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().iterator().next();
				child.remove();
				
			} else {
				// do nothing, we did unsub before
			}
		}
		

	}

	@Override
	public void subscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("subscribeFailed");
		ChildRelation childRelation = this.getSubscriptionClientChildSbbChildRelation();
		if (childRelation.size() > 0) {
			SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().iterator().next();
			child.remove();
			
		} else {
			// do nothing, we did unsub before
		}
	}

	@Override
	public void resubscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("refreshFailed");
		ChildRelation childRelation = this.getSubscriptionClientChildSbbChildRelation();
		if (childRelation.size() > 0) {
			SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().iterator().next();
			child.remove();
			
		} else {
			// do nothing, we did unsub before
		}
	}

	@Override
	public void unsubscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("removeFailed");
		ChildRelation childRelation = this.getSubscriptionClientChildSbbChildRelation();
		if (childRelation.size() > 0) {
			SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().iterator().next();
			child.remove();
			
		} else {
			// do nothing, we did unsub before
		}
	}

	/**
	 * 
	 */
	private void sendUnsubscribe() {
		ChildRelation childRelation = this.getSubscriptionClientChildSbbChildRelation();
		if (childRelation.size() > 0) {
			SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this.getSubscriptionClientChildSbbChildRelation().iterator().next();
			try {
				child.unsubscribe();
			} catch (SubscriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			// do nothing, we did unsub before
		}
	}

}
