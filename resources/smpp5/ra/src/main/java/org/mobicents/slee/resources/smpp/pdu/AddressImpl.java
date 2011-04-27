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

package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AddressImpl implements Address {

	protected org.mobicents.protocols.smpp.Address protoAddress;
	
	protected AddressImpl(){
	}
	
	public AddressImpl(org.mobicents.protocols.smpp.Address protoAddress){
		this.protoAddress = protoAddress;
	}

	public AddressImpl(int ton, int npi, String address) {
		protoAddress = new org.mobicents.protocols.smpp.Address(ton, npi, address);
	}

	public String getAddress() {
		return this.protoAddress.getAddress();
	}

	public int getAddressNpi() {
		return this.protoAddress.getNPI();
	}

	public int getAddressTon() {
		return this.protoAddress.getTON();
	}

	@Override
	public int hashCode() {
		return this.protoAddress.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.protoAddress.equals(obj);
	}

	public org.mobicents.protocols.smpp.Address getProtoAddress() {
		return this.protoAddress;
	}

}
