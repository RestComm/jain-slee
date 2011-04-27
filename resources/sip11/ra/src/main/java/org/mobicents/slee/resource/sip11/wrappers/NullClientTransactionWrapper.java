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

/**
 * 
 */
package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Transaction;
import javax.slee.Address;

import org.mobicents.slee.resource.sip11.ClientTransactionActivityHandle;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * A dummy client tx wrapper for a non existent client transaction,
 * used when firing out of dialog late responses.
 * 
 * @author martins
 *
 */
public class NullClientTransactionWrapper extends ClientTransactionWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param activityHandle
	 */
	public NullClientTransactionWrapper(SipActivityHandle activityHandle, SipResourceAdaptor ra) {
		super((ClientTransactionActivityHandle) activityHandle,ra);
		setActivity(true);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#getWrappedTransaction()
	 */
	@Override
	public Transaction getWrappedTransaction() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#terminated()
	 */
	@Override
	public void terminated() {}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.Wrapper#getEventFiringAddress()
	 */
	@Override
	public Address getEventFiringAddress() { return null; }
}
