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

package org.mobicents.slee.resource.tftp.events;

import java.rmi.server.UID;
import java.util.EventObject;


import net.java.slee.resource.tftp.events.RequestEvent;

import org.apache.commons.net.tftp.TFTPPacket;
import org.mobicents.slee.resource.tftp.TFTPTransfer;

public class RequestEventImpl extends EventObject implements RequestEvent {
	private static final long serialVersionUID = 1L;

	private TFTPPacket req = null;
	private TFTPTransfer tt;
	private String id = null;

	public RequestEventImpl(TFTPPacket req, TFTPTransfer source) {
		super(source);
		this.req = req;
		this.tt = source;
		this.id = new UID().toString();
	}

	public TFTPPacket getRequest() {
		return req;
	}

	public String getTypeDescr() {
		switch (req.getType()) {
		case TFTPPacket.READ_REQUEST:
			return "READ";
		case TFTPPacket.WRITE_REQUEST:
			return "WRITE";
		case TFTPPacket.DATA:
			return "DATA";
		case TFTPPacket.ACKNOWLEDGEMENT:
			return "ACK";
		case TFTPPacket.ERROR:
			return "ERROR";
		default:
			return null;
		}
	}

	/**
	 * Returns an event ID, unique in respect to the VM where it was generated
	 */
	public String getId() {
		return id;
	}

	public String toString() {
		return getClass().getName() + "[id=" + id + "]";
	}

	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((RequestEventImpl)o).id.equals(this.id);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}
}
