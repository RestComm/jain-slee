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

package net.java.slee.resource.mgcp;

import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;

import java.util.List;

/**
 * Sbb interface to interact with the Mgcp RA.
 * 
 * @author eduardomartins
 * 
 */
public interface JainMgcpProvider extends jain.protocol.ip.mgcp.JainMgcpProvider {
	
	/**
	 * Retrieves a connection activity for the specified {@link ConnectionIdentifier}. The activity is created if does not exists.
	 * @param connectionIdentifier
	 * @return
	 */
	public MgcpConnectionActivity getConnectionActivity(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves a connection activity for an unknown {@link ConnectionIdentifier}, to be used when sending {@link CreateConnection} events and receive further related messages from a Mgcp Server. The activity is created if does not exists.
	 * 
	 * @param transactionHandle the event to be send by server, which the Resource Adaptor will use to learn the returned {@link ConnectionIdentifier}
	 * @return
	 */
	public MgcpConnectionActivity getConnectionActivity(int transactionHandle, EndpointIdentifier endpointIdentifier);
	
	public List<MgcpConnectionActivity> getConnectionActivities(EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves an endpoint activity for the specified {@link EndpointIdentifier}. The activity is created if does not exists.
	 * 
	 * @return
	 */
	public MgcpEndpointActivity getEndpointActivity(EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves an unique transaction handler to be used on mgcp messages
	 * @return
	 */
	public int getUniqueTransactionHandler();
	
	/**
	 * Retrieves an unique valid {@link CallIdentifier} to be used on mgcp commands.
	 * @return
	 */
	public CallIdentifier getUniqueCallIdentifier();
	
	/**
	 * Retrieves an unique valid {@link RequestIdentifier} to be used on {@link NotificationRequest} commands.
	 * @return
	 */
	public RequestIdentifier getUniqueRequestIdentifier();
	
}
