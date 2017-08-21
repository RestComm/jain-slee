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

package org.mobicents.slee.container.deployment.profile.jpa;

public class ProfileAttributeArrayValueIdentity {

	private final String string;
	private final Object serializable;
	
	public ProfileAttributeArrayValueIdentity(String string, Object serializable) {
		this.string = string;
		this.serializable = serializable;
	}

	public ProfileAttributeArrayValueIdentity(ProfileEntityArrayAttributeValue value) {
		this.string = value.string;
		this.serializable = value.serializable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serializable == null) ? 0 : serializable.hashCode());
		result = prime * result
				+ ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ProfileAttributeArrayValueIdentity other = (ProfileAttributeArrayValueIdentity) obj;
			if (serializable == null) {
				if (other.serializable != null)
					return false;
			} else if (!serializable.equals(other.serializable))
				return false;
			if (string == null) {
				if (other.string != null)
					return false;
			} else if (!string.equals(other.string))
				return false;
			return true;
		}
		else {
			return false;
		}		
	}

	
	
}
