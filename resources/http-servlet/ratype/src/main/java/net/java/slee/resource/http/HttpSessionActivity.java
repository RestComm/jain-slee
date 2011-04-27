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

package net.java.slee.resource.http;

/**
 * HttpSessionActivity represents HttpSession created from incoming
 * javax.servlet.http.HttpServletRequest. The implementing ResourceAdaptor can
 * create a HttpSession calling getSession() on incoming request and assign the
 * session.getId() to SessionId of HttpSessionActivity. <br/> The SBB can
 * expicitly end the HttpSessionActivity by calling invalidate() on HttpSession
 * Object or when ever the HttpSession times out Web Container will invalidate
 * session. <br/>
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * @version 1.0
 * 
 */
public interface HttpSessionActivity {

	/**
	 * Retrieves the Session's ID.
	 * 
	 * @return Session ID
	 */
	public String getSessionId();
	

}
