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

/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileTableInterface {

	private String description;
	private String profileTableInterfaceName;

	public MProfileTableInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileTableInterface profileTableInterface11)
	{	  
		this.description = profileTableInterface11.getDescription() == null ? null : profileTableInterface11.getDescription().getvalue();
		this.profileTableInterfaceName = profileTableInterface11.getProfileTableInterfaceName().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getProfileTableInterfaceName() {
		return profileTableInterfaceName;
	}

}
