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

import javax.servlet.http.HttpSession;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;

import net.java.slee.resource.http.HttpServletRaSbbInterface;
import net.java.slee.resource.http.HttpSessionActivity;

/**
 * 
 * Implementation class for HttpServletRaSbbInterface
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * @author martins
 * 
 */
public class HttpServletRaSbbInterfaceImpl implements HttpServletRaSbbInterface {

	/**
	 * 
	 */
	private final HttpServletResourceAdaptor ra;
	
	/**
	 * 
	 * @param ra
	 */
	public HttpServletRaSbbInterfaceImpl(HttpServletResourceAdaptor ra) {
		this.ra = ra;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.http.HttpServletRaSbbInterface#getHttpSessionActivity(javax.servlet.http.HttpSession)
	 */
	public HttpSessionActivity getHttpSessionActivity(HttpSession httpSession)
			throws NullPointerException, IllegalArgumentException, IllegalStateException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		if (httpSession == null) {
			throw new NullPointerException("null http session");
		}
		if (!(httpSession instanceof HttpSessionWrapper)) {
			throw new IllegalArgumentException();
		}
		final HttpSessionWrapper httpSessionWrapper = (HttpSessionWrapper) httpSession;
		final HttpSessionActivityImpl activity = new HttpSessionActivityImpl(httpSession.getId());
		if (httpSessionWrapper.getResourceEntryPoint() == null) {
			ra.getSleeEndpoint().startActivitySuspended(activity,activity);
			httpSessionWrapper.setResourceEntryPoint(ra.getName());
		}
		return activity;
	}
}
