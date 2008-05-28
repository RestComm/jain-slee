/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.resource.http;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Takes incoming Http requests and routes them to the JSLEE HttpServlet RA
 * 
 * @author Ivelin Ivanov
 *
 */
public class HttpServletRaServlet extends HttpServlet {

	public static int HTTP_ERROR_CODE_SERVICE_UNAVAILABLE = 503;
	
	private static Logger logger = Logger.getLogger(HttpServletRaServlet.class.getName());
	
	private static final long serialVersionUID = 7542822118420702996L;
	
	private static final String RA_ENTRY_POINT_PARAM = "ra-entry-point-jndi-name";
	
	private Servlet raEntryPoint = null;

	public void service(HttpServletRequest request, HttpServletResponse response) {
		Servlet ep = getRaEntryPont();
		if (ep == null) {
			try {
				response.sendError(HTTP_ERROR_CODE_SERVICE_UNAVAILABLE, "JSLEE Http Servlet RA");
			} catch (IOException e) {
				logger.log(Level.WARNING, "Failed to send 503 response to inform client that RA is not available", e);
			}
		} else {
			try {
				raEntryPoint.service(request, response);
			} catch (ServletException e) {
				logger.log(Level.WARNING, "Http Servlet RA Failed to handle http request.", e);
			} catch (IOException e) {
				logger.log(Level.WARNING, "Http Servlet RA Failed to handle http request.", e);
			}
		}
	}

	private synchronized Servlet getRaEntryPont() {
		if (raEntryPoint != null) {
			return raEntryPoint;
		} else {
			String raEntryPointJndiName = this.getInitParameter(RA_ENTRY_POINT_PARAM);
			Object obj;
			try {
				InitialContext ictx = new InitialContext();
				obj = ictx.lookup(raEntryPointJndiName);
				//assert obj instanceof Servlet : "Servlet type is expected to be bound at " + raEntryPointJndiName;
				raEntryPoint = (Servlet)obj;
				return raEntryPoint;
			} catch (NamingException e) {
				logger.log(Level.WARNING, "Failed to lookup Http Servlet RA Entry Point. JNDI location: " + raEntryPointJndiName);
			}
		}
		return null;
	}
	
}


