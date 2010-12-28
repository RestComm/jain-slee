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
 * 
 * Interface for SIP-Subscruption-Client.
 * 
 * @author baranowb
 * @author martins
 */
public interface SubscriptionClientChild {

	/**
	 * Set parent which will be notified about call results.
	 * 
	 * @param parent
	 */
	public void setParentSbb(SubscriptionClientParentSbbLocalObject parent);

	public boolean isSubscriptionActive();

	public String getSubscriber();

	public String getEventPackage();

	public String getNotifier();

	public int getExpires();

	public String getContentType();
	
	public String getContentSubType();
	
	/**
	 * @return
	 */
	public boolean isRefreshing();

	// TODO: should there be eventTemplate also ?
	/**
	 * creates an internal subscription
	 * 
	 * @param subscriber
	 * @param notifier
	 * @param eventPackage
	 * @param subscriptionId
	 * @param expires
	 * @param content
	 * @param contentType
	 * @param contentSubtype
	 * @throws SubscriptionException
	 */
	public void subscribe(String subscriber, String subscriberdisplayName, String notifier, String eventPackage, int expires, String content,
			String contentType, String contentSubtype) throws SubscriptionException;

	/**
	 * Requests the termination of an internal subscription.
	 * 
	 * @param subscriber
	 * @param notifier
	 * @param eventPackage
	 * @param subscriptionId
	 * @return
	 * @throws SubscriptionException
	 */
	public void unsubscribe() throws SubscriptionException;

}
