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

import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

/**
 * This activity for Mgcp Resource Adaptor type can be used as the context of
 * Mgcp commands related with a specific Mgcp connection.
 * 
 * @author eduardomartins
 * 
 */
public interface MgcpConnectionActivity {

	/**
	 * Retrieves the mgcp {@link ConnectionIdentifier} associated with this
	 * activity type.
	 * 
	 * @return null if the {@link ConnectionIdentifier} is not known yet.
	 */
	public String getConnectionIdentifier();

	/**
	 * Retrieves the mgcp {@link EndpointIdentifier} associated with this activity.
	 * @return
	 */
	public EndpointIdentifier getEndpointIdentifier();
	
	/**
	 * Forces the end of this activity.
	 */
	public void release();
	
}
