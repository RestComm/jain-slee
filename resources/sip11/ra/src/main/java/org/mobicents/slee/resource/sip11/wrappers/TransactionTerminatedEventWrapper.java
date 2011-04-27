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

package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;

public class TransactionTerminatedEventWrapper extends
		TransactionTerminatedEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7363453023732951366L;

	public TransactionTerminatedEventWrapper(Object source, ClientTransaction tx) {
		super(source, tx);
	}

	public TransactionTerminatedEventWrapper(Object source, ServerTransaction tx) {
		super(source, tx);
	}
	
	public String toString() {
		
		Transaction tx=null;
		if(super.getClientTransaction()==null)
			tx=super.getServerTransaction();
		else
			tx=super.getClientTransaction();
		
		return new StringBuilder("[TransactionTerminatedEventWrapper ISSERVER[").append((super.getServerTransaction()!=null))
			.append("] BRANCHID[").append(tx.getBranchId())
			.append("] STATE[").append(tx.getState())
			.append("] METHOD[").append(tx.getRequest().getMethod())
			.append("] DIALOG{ ").append(tx.getDialog()).append(" } ]").toString();
	}
	
}
