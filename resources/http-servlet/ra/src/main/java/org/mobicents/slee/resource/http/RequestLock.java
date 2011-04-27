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

package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public class RequestLock {

	private ConcurrentHashMap<HttpServletRequestEvent, Object> map = new ConcurrentHashMap<HttpServletRequestEvent, Object>();

	public Object getLock(HttpServletRequestEvent event) {
		Object obj = map.get(event);
		if (obj == null) {
			obj = createLock(event);
		}
		return obj;
	}

	private Object createLock(HttpServletRequestEvent event) {
		Object object = new Object();
		Object anotherObject = map.putIfAbsent(event, object);
		if (anotherObject == null) {
			return object;
		}
		else {
			return anotherObject;
		}
	}

	public Object removeLock(HttpServletRequestEvent event) {
		return map.remove(event);
	}

}
