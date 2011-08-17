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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A parameter for the SIP event header.
 * 
 * Example of usage:
 * 
 * EventPackageParameter eventPackageParameter = new
 * EventPackageParameter().setName("diff-processing").setValue("aggregate");
 * 
 * @author martins
 * 
 */
public class EventPackageParameter implements Externalizable {

	private String name;
	private String value;

	/**
	 * Retrieves the parameter name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param name
	 * @return
	 */
	public EventPackageParameter setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Retrieves the parameter value.
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the parameter value.
	 * 
	 * @param value
	 * @return
	 */
	public EventPackageParameter setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
		out.writeUTF(value);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		name = in.readUTF();
		value = in.readUTF();
	}

}
