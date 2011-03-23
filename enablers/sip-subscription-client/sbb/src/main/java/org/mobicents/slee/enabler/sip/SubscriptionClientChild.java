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

import java.util.Map;

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
	
	public String getSubscriber();

	public String getEventPackage();

	public String getNotifier();

	/**
	 * creates an internal subscription
	 * 
	 * @param subscriber - usually an URI, for instance sip:joe.doe@mobicents.org. It identifies entity on behalf which enabler wants to subscribe
	 * @param notifier - usually an URI identifying notifier.
	 * @param expires - timeout value, measured in seconds, it indicates time gap between resubscribes/termination of subscription
	 * @param eventPackage - event package to which subscirber wants to subscribe, ie. <b>presence</b>, <b>presence.oma</b>,<b>xcap-diff</b> .. etc.
	 * @param eventsParameters - parameters which should be passed with event, ie. <b>diff-processing</b>
	 * @param acceptedContentType - main MIME type of expected event content
	 * @param acceptedContentSubtype - sub MIME type of expected event content
	 * @throws SubscriptionException
	 */
	public void subscribe(String subscriber, String subscriberdisplayName, String notifier, int expires, String eventPackage, Map<String, String> eventsParameters,
			String acceptedContentType, String acceptedContentSubtype) throws SubscriptionException;

	/**
	 * Similar to
	 * {@link #subscribe(String, String, String, int, String, Map, String, String)}
	 * , however this method allows to send content within subscribe. It is
	 * useful in case like XCAP Diff subscription for instance.
	 * 
	 * @param contentType - main MIME type of content
	 * @param contentSubType - sub MIME type of content
	 * @param content - content, ie. xml resource list.
	 * @throws SubscriptionException
	 */
	public void subscribe(String subscriber, String subscriberdisplayName, String notifier, int expires, String eventPackage, Map<String, String> eventParameters,
			String acceptedContentType, String acceptedContentSubtype, String contentType, String contentSubType, String content) throws SubscriptionException;

	/**
	 * Requests the termination of an internal subscription.
	 * @throws SubscriptionException
	 */
	public void unsubscribe() throws SubscriptionException;

}
