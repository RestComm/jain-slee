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

/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
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
 * Project: mobicents-jainslee-server-core<br>
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
