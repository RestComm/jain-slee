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

import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import java.util.UUID;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

public class MgcpConnectionActivityImpl implements MgcpConnectionActivity {

	private final String id;
	
	private String connectionIdentifier;
	private EndpointIdentifier endpointIdentifier;
	private final Integer transactionHandle;
	private MgcpResourceAdaptor ra;
	
	private MgcpConnectionActivityHandle activityHandle=null;
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this(connectionIdentifier.toString(),null,endpointIdentifier,ra);
	}
	
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(int transactionHandle, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this(null,Integer.valueOf(transactionHandle),endpointIdentifier,ra);
	}
	
	private MgcpConnectionActivityImpl(String connectionIdentifier,Integer transactionHandle, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this.id = UUID.randomUUID().toString();
		this.transactionHandle = transactionHandle;
		this.connectionIdentifier = connectionIdentifier;
		this.endpointIdentifier = endpointIdentifier;
		this.ra = ra;
		this.activityHandle=new MgcpConnectionActivityHandle(id);
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	public Integer getTransactionHandle() {
		return transactionHandle;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getConnectionIdentifier() {
		return connectionIdentifier;
	}
	
	/**
	 * TODO
	 * @param connectionIdentifier
	 */
	public void setConnectionIdentifier(ConnectionIdentifier connectionIdentifier) {
		this.connectionIdentifier = connectionIdentifier.toString();
	}
	
	public EndpointIdentifier getEndpointIdentifier() {
		return this.endpointIdentifier;
	}
	
	public void setEndpointIdentifier(EndpointIdentifier endpointIdentifier) {
		this.endpointIdentifier = endpointIdentifier;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpConnectionActivityImpl)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}

	public void release() {
		ra.endActivity(new MgcpConnectionActivityHandle(id));		
	}

	public MgcpConnectionActivityHandle getActivityHandle() {
		return activityHandle;
	}

	
	
}
