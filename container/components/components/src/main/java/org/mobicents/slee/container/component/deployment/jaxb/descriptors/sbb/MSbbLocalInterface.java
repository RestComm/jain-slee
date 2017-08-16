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

/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbLocalInterface;
import org.mobicents.slee.container.component.sbb.SbbLocalInterfaceDescriptor;

/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbLocalInterface implements SbbLocalInterfaceDescriptor {
	
	private boolean isolateSecurityPermissions=false;
	private String sbbLocalInterfaceName=null;
	
	public boolean isIsolateSecurityPermissions() {
		return isolateSecurityPermissions;
	}
	
	public String getSbbLocalInterfaceName() {
		return sbbLocalInterfaceName;
	}
	
	public MSbbLocalInterface(SbbLocalInterface sbbLocalInterface) {
		super();
		this.sbbLocalInterfaceName=sbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
	}
	public MSbbLocalInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbLocalInterface llSbbLocalInterface) {
		super();
		this.sbbLocalInterfaceName=llSbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
		String v=llSbbLocalInterface.getIsolateSecurityPermissions();
		if(v!=null && Boolean.parseBoolean(v))
		{
			this.isolateSecurityPermissions=true;
		}
	}
	
	
	
	
}
