/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;
import org.mobicents.slee.container.component.profile.ProfileIndexDescriptor;

/**
 * Represents indexed attribute from slee 1.0 specs. Start time:23:37:29
 * 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
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
