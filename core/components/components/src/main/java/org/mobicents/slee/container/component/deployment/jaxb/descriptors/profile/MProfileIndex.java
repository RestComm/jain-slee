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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;
import org.mobicents.slee.container.component.profile.ProfileIndexDescriptor;

/**
 * Represents indexed attribute from slee 1.0 specs. Start time:23:37:29
 * 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileIndex implements ProfileIndexDescriptor {

	private final String name;

	private final boolean unique;

	public MProfileIndex(ProfileIndex profileIndex10) {
		this.name = profileIndex10.getvalue();
		this.unique = Boolean.parseBoolean(profileIndex10.getUnique());
	}

	public String getName() {
		return name;
	}

	public boolean getUnique() {
		return unique;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() ==  this.getClass()) {
			return ((MProfileIndex)obj).name.equals(this.name);
		}
		else {
			return false;
		}
	}
}
