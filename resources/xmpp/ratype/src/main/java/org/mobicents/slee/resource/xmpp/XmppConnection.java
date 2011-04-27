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

package org.mobicents.slee.resource.xmpp;

import java.io.Serializable;

/**
 * This object represents a generic, not bound with any specific API, XMPP connection.
 * 
 * This object is the Activity Object for the XMPP Resource Adaptor.
 * 
 * @author Eduardo Martins
 * @version 2.1
 */
public class XmppConnection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String connectionId;
	private Object connection;
	private final String toString;
	
	public XmppConnection(String connectionId, Object connection) {
		this.connectionId = connectionId;
		this.connection = connection;
		this.toString = "XmppConnection { connectionId="+connectionId+",connection="+connection.toString()+"}";
	}
	
	public String getConnectionId() {
		return connectionId;
	}
	
	public Object getConnection() {
		return connection;
	}
	
	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((XmppConnection)o).connectionId.equals(this.connectionId);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return connectionId.hashCode();
	}
	
	public String toString() {
		return toString;
	}
}
