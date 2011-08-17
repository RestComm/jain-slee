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
 * @author baranowb
 *
 */
public enum TerminationReason {
	 /**The subscription has been terminated because the resource
    state which was being monitored no longer exists.  Clients SHOULD
    NOT attempt to re-subscribe.  The "retry-after" parameter has no
    semantics for "noresource".*/
	noresource,
	/**
	 *  The subscription has been terminated due to change in
    authorization policy.  Clients SHOULD NOT attempt to re-subscribe.
    The "retry-after" parameter has no semantics for "rejected".
	 */
	rejected,
	/**
	 * The subscription has been terminated, but the subscriber
    SHOULD retry immediately with a new subscription.  One primary use
    of such a status code is to allow migration of subscriptions
    between nodes.  The "retry-after" parameter has no semantics for
    "deactivated".
	 */
	deactivated,
	/**
	 * The subscription has been terminated, but the client
    SHOULD retry at some later time.  If a "retry-after" parameter is
    also present, the client SHOULD wait at least the number of
    seconds specified by that parameter before attempting to re-
    subscribe.
	 */
	probation,
	/**
	 * The subscription has been terminated because it was not
    refreshed before it expired.  Clients MAY re-subscribe
    immediately.  The "retry-after" parameter has no semantics for
    "timeout".
	 */
	timeout,
	/**
	 * The subscription has been terminated because the notifier
    could not obtain authorization in a timely fashion.  If a "retry-
    after" parameter is also present, the client SHOULD wait at least
    the number of seconds specified by that parameter before
    attempting to re-subscribe; otherwise, the client MAY retry
    immediately, but will likely get put back into pending state.
	 */
	giveup,
	extension;
	
	public static TerminationReason fromString(String s)
	{
		if(s== null)
		{
			return null;
		}
		try{
			return TerminationReason.valueOf(s.toLowerCase());
		}catch(Exception e)
		{
			return extension;
		}
		
		
	}
}
