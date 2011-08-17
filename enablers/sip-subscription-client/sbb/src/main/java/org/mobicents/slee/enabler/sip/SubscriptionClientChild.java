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

package org.mobicents.slee.enabler.sip;

/**
 * 
 * An application enabler, which may be used to integrate SIP Subscription
 * Client functionality into a SLEE application. The enabler supports
 * subscribing state with respect to single and list resources.
 * 
 * @author baranowb
 * @author martins
 */
public interface SubscriptionClientChild {

	/**
	 * Retrieves the subscription data.
	 * 
	 * @return
	 */
	public SubscriptionData getSubscriptionData();

	/**
	 * Requests the creation of a new SIP subscription.
	 * 
	 * @param subscriptionData the data for the subscription
	 * @throws SubscriptionException
	 */
	public void subscribe(SubscriptionData subscriptionData)
			throws SubscriptionException;

	/**
	 * Requests the creation of a new SIP subscription.
	 * 
	 * @param subscriptionData the data for the subscription
	 * @param initialSubscribeContent content to be sent in the initial subscribe.
	 * @throws SubscriptionException
	 */
	public void subscribe(SubscriptionData subscriptionData, SubscriptionRequestContent initialSubscribeContent)
			throws SubscriptionException;
	
	/**
	 * Requests the termination of an internal subscription.
	 * 
	 * @throws SubscriptionException
	 */
	public void unsubscribe() throws SubscriptionException;

}
