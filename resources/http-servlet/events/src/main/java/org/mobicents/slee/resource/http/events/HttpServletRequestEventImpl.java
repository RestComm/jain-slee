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

package org.mobicents.slee.resource.http.events;

import java.rmi.server.UID;
import java.util.EventObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public class HttpServletRequestEventImpl extends EventObject implements
		HttpServletRequestEvent {

	private static final long serialVersionUID = -7683636914960112816L;

	private HttpServletRequest hreq = null;

	private String id = null;

	private HttpServletResponse hresponse;

	public HttpServletRequestEventImpl(HttpServletRequest req,
			HttpServletResponse response, Object source) {
		super(source);
		this.hreq = req;
		this.hresponse = response;
		this.id = new UID().toString();
	}

	public HttpServletRequest getRequest() {
		return hreq;
	}

	public HttpServletResponse getResponse() {
		return hresponse;
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
			return ((HttpServletRequestEventImpl)o).id.equals(this.id);
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}

}
