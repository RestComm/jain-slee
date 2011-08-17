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

package org.mobicents.slee.enabler.sip.example;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.enabler.sip.ContentType;
import org.mobicents.slee.enabler.sip.Notify;
import org.mobicents.slee.enabler.sip.SubscriptionClientChildSbbLocalObject;
import org.mobicents.slee.enabler.sip.SubscriptionClientParent;
import org.mobicents.slee.enabler.sip.SubscriptionData;
import org.mobicents.slee.enabler.sip.SubscriptionException;
import org.mobicents.slee.enabler.sip.SubscriptionStatus;

/**
 * @author baranowb
 * 
 */
public abstract class SubscriptionClientParentSbb implements Sbb,
		SubscriptionClientParent {
	private static Tracer tracer;

	protected SbbContext sbbContext;

	// ////////////////////////////////
	// SBB OBJECT LIFECYCLE METHODS //
	// ////////////////////////////////

	public void sbbActivate() {
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
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
			tracer = sbbContext.getTracer(SubscriptionClientParentSbb.class
					.getSimpleName());
		}

	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public abstract void setNotifyCount(int b);

	public abstract int getNotifyCount();

	public abstract ChildRelationExt getSubscriptionClientChildSbbChildRelation();

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		SubscriptionClientChildSbbLocalObject child;
		try {
			child = (SubscriptionClientChildSbbLocalObject) this
					.getSubscriptionClientChildSbbChildRelation().create(
							ChildRelationExt.DEFAULT_CHILD_NAME);
			ContentType[] acceptedContentTypes = { new ContentType().setType(
					"application").setSubType("pidf+xml") };
			SubscriptionData subscriptionData = new SubscriptionData()
					.setSubscriberURI("sip:14313471@127.0.0.1:5090")
					.setNotifierURI("sip:14313471@127.0.0.1:5090")
					.setExpires(61).setEventPackage("presence")
					.setAcceptedContentTypes(acceptedContentTypes)
					.setSupportResourceLists(true);
			child.subscribe(subscriptionData);
		} catch (Throwable e) {
			tracer.severe("Failed to subscribe", e);
		}

	}

	public void onActivityEndEvent(javax.slee.ActivityEndEvent event,
			ActivityContextInterface aci) {
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
	public void onNotify(Notify notify,
			SubscriptionClientChildSbbLocalObject enabler) {
		tracer.info("onNotify: " + notify);

		// check num of notifs
		int nots = getNotifyCount() + 1;
		setNotifyCount(nots);
		if (nots == 3) {
			// kill baby
			sendUnsubscribe();
		}

		// subscription is terminated ONLY, when there is Notify with
		// termination status
		if (notify.getStatus() == SubscriptionStatus.terminated) {
			// kill child
			SbbLocalObjectExt child = getSubscriptionClientChildSbbChildRelation()
					.get(ChildRelationExt.DEFAULT_CHILD_NAME);
			if (child != null) {
				child.remove();
			}
		}
	}

	@Override
	public void subscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("subscribeFailed");
		SbbLocalObjectExt child = getSubscriptionClientChildSbbChildRelation()
				.get(ChildRelationExt.DEFAULT_CHILD_NAME);
		if (child != null) {
			child.remove();
		}
	}

	@Override
	public void resubscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("refreshFailed");
		SbbLocalObjectExt child = getSubscriptionClientChildSbbChildRelation()
				.get(ChildRelationExt.DEFAULT_CHILD_NAME);
		if (child != null) {
			child.remove();
		}
	}

	@Override
	public void unsubscribeFailed(int responseCode,
			SubscriptionClientChildSbbLocalObject sbbLocalObject) {
		tracer.info("removeFailed");
		SbbLocalObjectExt child = getSubscriptionClientChildSbbChildRelation()
				.get(ChildRelationExt.DEFAULT_CHILD_NAME);
		if (child != null) {
			child.remove();
		}
	}

	/**
	 * 
	 */
	private void sendUnsubscribe() {
		SubscriptionClientChildSbbLocalObject child = (SubscriptionClientChildSbbLocalObject) this
				.getSubscriptionClientChildSbbChildRelation().get(
						ChildRelationExt.DEFAULT_CHILD_NAME);
		if (child != null) {
			try {
				child.unsubscribe();
			} catch (SubscriptionException e) {
				tracer.severe("Failed to unsubscribe", e);
			}
		}
	}

}
