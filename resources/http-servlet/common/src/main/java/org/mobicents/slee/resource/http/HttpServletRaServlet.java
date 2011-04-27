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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Takes incoming Http requests and routes them to the JSLEE HttpServlet RA
 * 
 * @author Ivelin Ivanov
 * @author martins
 *
 */
public class HttpServletRaServlet extends HttpServlet {

	private static int HTTP_ERROR_CODE_SERVICE_UNAVAILABLE = 503;
	
	private static Logger logger = Logger.getLogger(HttpServletRaServlet.class.getName());
	
	private static final long serialVersionUID = 7542822118420702996L;
		
	private String servletName = null;

	public void service(HttpServletRequest request, HttpServletResponse response) {
		if (servletName == null) {
			servletName = getServletConfig().getServletName();
		}
		HttpServletResourceEntryPoint resourceEntryPoint = HttpServletResourceEntryPointManager.getResourceEntryPoint(servletName);
		if (resourceEntryPoint == null) {
			replyNotAvailableError(servletName, response);
		} else {
			resourceEntryPoint.onRequest(request, response);
		}
	}
	
	public static void replyNotAvailableError(String name, HttpServletResponse response) {
		try {
			response.sendError(HTTP_ERROR_CODE_SERVICE_UNAVAILABLE, "JSLEE Http Servlet RA Entity with name "+name+" is not available");
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to send 503 response to inform client that RA is not available", e);
		}
	}
}


