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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * 
 * @author martins
 *
 */
public class HttpServletRaSessionListener implements HttpSessionListener {

	private static final Logger log = Logger
			.getLogger(HttpServletRaSessionListener.class.getName());

	/**
	 * even if multiple listeners are instanciated only one becomes active
	 */
	private static AtomicBoolean masterListener = new AtomicBoolean(true);
	
	private boolean active;
	
	public HttpServletRaSessionListener() {
		if (masterListener.compareAndSet(true, false)) {
			active = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		if (active) {
			if (log.isDebugEnabled()) 
				log.debug("sessionCreated sessionId = "
						+ httpSessionEvent.getSession().getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		if (active) {
			if (log.isDebugEnabled())
				log.debug("sessionDestroyed called for sessionId = "
						+ httpSessionEvent.getSession().getId());

			HttpSession session = httpSessionEvent.getSession();
			HttpSessionWrapper wrapper = new HttpSessionWrapper(session);
			String name = wrapper.getResourceEntryPoint();
			if (name != null) {
				HttpServletResourceEntryPoint resourceEntryPoint = HttpServletResourceEntryPointManager.getResourceEntryPoint(name);
				if (resourceEntryPoint != null) {
					resourceEntryPoint.onSessionTerminated(session.getId());
				}
			}
		}
	}

}
