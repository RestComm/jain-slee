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
package org.mobicents.slee.enabler.sip;

/**
 * @author baranowb
 * @author emmartins
 */
public interface SubscriptionClientParent {
	/**
	 * Callback method indicating outcome of initial subscribe. If this
	 * indicates error, enabler must be discarded.
	 * 
	 * @param responseCode
	 * @param enabler
	 * 
	 */
	public void subscribeSucceed(int responseCode, SubscriptionClientChildSbbLocalObject enabler);

	/**
	 * Callback method indicating outcome of unsubscribe. If this indicates
	 * error, enabler must be discarded.
	 * 
	 * @param responseCode
	 *            response code to remove request
	 * @param enabler
	 */
	public void unsubscribeSucceed(int responseCode, SubscriptionClientChildSbbLocalObject enabler);

	/**
	 * Callback method which passes information about notification. If
	 * subscription is terminated, enabler must be discarded
	 * 
	 * @param notify
	 * @param enabler
	 */
	public void onNotify(Notify notify, SubscriptionClientChildSbbLocalObject enabler);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	public void subscribeFailed(int responseCode, SubscriptionClientChildSbbLocalObject sbbLocalObject);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	public void resubscribeFailed(int responseCode, SubscriptionClientChildSbbLocalObject sbbLocalObject);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	void unsubscribeFailed(int responseCode, SubscriptionClientChildSbbLocalObject sbbLocalObject);

}
