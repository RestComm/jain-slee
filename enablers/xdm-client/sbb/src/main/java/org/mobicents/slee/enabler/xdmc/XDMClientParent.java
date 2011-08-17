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

package org.mobicents.slee.enabler.xdmc;

import java.net.URI;

import org.mobicents.slee.enabler.sip.SubscriptionStatus;
import org.mobicents.slee.enabler.sip.TerminationReason;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff;

/**
 * @author martins
 * 
 */
public interface XDMClientParent {

	/**
	 * Provides the response for an XML resource GET request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param mimetype
	 * @param content
	 * @param eTag
	 */
	public void getResponse(URI uri, int responseCode, String mimetype,
			String content, String eTag);

	/**
	 * Provides the response for an XML resource PUT request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void putResponse(URI uri, int responseCode, String responseContent,
			String eTag);

	/**
	 * Provides the response for an XML resource DELETE request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void deleteResponse(URI uri, int responseCode,
			String responseContent, String eTag);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param responseCode
	 * @param sbbLocalObject
	 * @param notifier
	 */
	public void subscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, String notifier);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param responseCode
	 * @param sbbLocalObject
	 * @param notifier
	 */
	public void resubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, String notifier);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param responseCode
	 * @param sbbLocalObject
	 * @param notifier
	 */
	public void unsubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, String notifier);
	
	/**
	 * Callback method indicating termination of subscription.
	 * 
	 * @param sbbLocalObject
	 * @param notifier
	 * @param reason
	 */
	public void subscriptionTerminated(XDMClientChildSbbLocalObject sbbLocalObject, String notifier, TerminationReason reason); //FIXME: add resourceList to callback?
	
	/**
	 * Notifies an update in XDMS resources subscribed by the enabler.
	 * This callback is used also to notify on resubscription.
	 * 
	 * @param xcapDiff - the xcap diff notified
	 * @param status - the subscription status
	 */
	public void subscriptionNotification(XcapDiff xcapDiff, SubscriptionStatus status);
	
}
