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

package org.mobicents.slee.example.sjr.data;

import java.util.List;

import javax.sip.header.ContactHeader;

/**
 * 
 * @author martins
 *
 */
public interface DataSourceChildSbbLocalInterface {

	/**
	 * Initiates the data source
	 */
	public void init();

	/**
	 * Retrieves bindings of specified sip address, result is provided
	 * asynchronously through {@link DataSourceParentSbbLocalInterface}.
	 * 
	 * @param address
	 */
	public void getBindings(String address);

	/**
	 * Removes a specific binding.
	 * 
	 * @param contact
	 * @param address
	 */
	public void removeBinding(String contact, String address);

	/**
	 * Removes all bindings that match the parameters.
	 * 
	 * @param address
	 * @param callId
	 * @param cSeq
	 */
	public void removeBindings(String address, String callId, long cSeq);

	/**
	 * Update bindings for the specified address.
	 * 
	 * @param address
	 * @param callId
	 * @param cSeq
	 * @param contacts
	 */
	public void updateBindings(String address, String callId, long cSeq,
			List<ContactHeader> contacts);

}
