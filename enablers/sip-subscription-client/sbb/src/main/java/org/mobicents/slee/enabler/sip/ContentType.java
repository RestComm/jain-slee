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
 * 
 * A SIP media content type.
 * 
 * Example of usage:
 * 
 * ContentType contentType = new
 * ContentType().setType("application").setSubType("pidf+xml");
 * 
 * @author martins
 * 
 */
public class ContentType implements Externalizable {

	private String type;
	private String subType;

	/**
	 * Retrieves the sub-type.
	 * 
	 * @return
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Sets the sub-type.
	 * 
	 * @param subType
	 */
	public ContentType setSubType(String subType) {
		this.subType = subType;
		return this;
	}

	/**
	 * Retrieves the type.
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 */
	public ContentType setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(type);
		out.writeUTF(subType);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		type = in.readUTF();
		subType = in.readUTF();
	}

}
