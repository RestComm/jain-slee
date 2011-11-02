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

package org.mobicents.slee.example.sjr.sip;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author martins
 *
 */
public class RegistrationBindingData implements Externalizable {

	private String address;
	private String contact;

	public String getAddress() {
		return address;
	}

	public RegistrationBindingData setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getContact() {
		return contact;
	}

	public RegistrationBindingData setContact(String contact) {
		this.contact = contact;
		return this;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		address = in.readUTF();
		contact = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(address);
		out.writeUTF(contact);
	}

}
