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

package net.java.slee.resource.sip;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;

/**
 * The event that signals the arrival of a CANCEL SIP Request.
 * 
 */
public class CancelRequestEvent extends RequestEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the {@link ServerTransaction} that matches this event
	 */
	private final ServerTransaction matchingTransaction;

	public CancelRequestEvent(Object source,
			ServerTransaction serverTransaction,
			ServerTransaction matchingTransaction, Dialog dialog,
			Request request) {
		super(source, serverTransaction, dialog, request);
		this.matchingTransaction = matchingTransaction;
	}

	/**
	 * Retrieves the {@link ServerTransaction} that matches this event.
	 * 
	 * @return null if there is no matching transaction.
	 */
	public ServerTransaction getMatchingTransaction() {
		return this.matchingTransaction;
	}

	@Override
	public String toString() {
		return new StringBuilder("CancelRequestEvent[ cancelST = ").append(getServerTransaction())
			.append(", inviteST = ").append(matchingTransaction)
			.append(", inviteDialog = ").append(getDialog())
			.append(" ]").toString();
	}
}
