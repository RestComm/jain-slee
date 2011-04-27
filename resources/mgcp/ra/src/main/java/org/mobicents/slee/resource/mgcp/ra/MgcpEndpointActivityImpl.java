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

package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import net.java.slee.resource.mgcp.MgcpEndpointActivity;

public class MgcpEndpointActivityImpl implements MgcpEndpointActivity {

	private final EndpointIdentifier endpointIdentifier;
	
	private final MgcpResourceAdaptor ra;

	private MgcpEndpointActivityHandle activityHandle;
	
	/**
	 * TODO
	 */
	public MgcpEndpointActivityImpl(MgcpResourceAdaptor ra, EndpointIdentifier endpointIdentifier) {
		this.ra = ra;
		this.endpointIdentifier = endpointIdentifier;
		this.activityHandle=new MgcpEndpointActivityHandle(endpointIdentifier.toString());
		
	}
	
	/**
	 * TODO
	 * @return
	 */
	public EndpointIdentifier getEndpointIdentifier() {
		return endpointIdentifier;
	}
	
	
	
	@Override
	public int hashCode() {
		return endpointIdentifier.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpEndpointActivityImpl)obj).endpointIdentifier.toString().equals(this.endpointIdentifier.toString());
		}
		else {
			return false;
		}
	}
	
	public void release() {
		ra.endActivity(new MgcpEndpointActivityHandle(endpointIdentifier.toString()));
	}
	
	public MgcpEndpointActivityHandle getActivityHandle() {
		return activityHandle;
	}
}
