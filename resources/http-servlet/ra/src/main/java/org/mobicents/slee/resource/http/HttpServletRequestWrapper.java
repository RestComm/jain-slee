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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author martins
 *
 */
public class HttpServletRequestWrapper implements HttpServletRequest {
	
	/**
	 * 
	 */
	private final HttpServletRequest request;
	
	/**
	 * 
	 * @param request
	 */
	public HttpServletRequestWrapper(HttpServletRequest request) {
		this.request = request;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		return request.getAuthType();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		return request.getContextPath();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String arg0) {
		return request.getDateHeader(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String arg0) {
		return request.getHeader(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	@SuppressWarnings("unchecked")
	public Enumeration getHeaderNames() {
		return request.getHeaderNames();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Enumeration getHeaders(String arg0) {
		return request.getHeaders(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0) {
		return request.getIntHeader(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return request.getMethod();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		return request.getPathInfo();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		return request.getQueryString();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		return request.getRequestURI();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		return request.getServletPath();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		return this.getSession(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		final HttpSession httpSession = request.getSession(arg0);
		if (httpSession == null) {
			return null;
		}
		else {
			return new HttpSessionWrapper(httpSession);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	@SuppressWarnings("deprecation")
	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		return request.isUserInRole(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		return request.getAttribute(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	@SuppressWarnings("unchecked")
	public Enumeration getAttributeNames() {
		return request.getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		return request.getContentLength();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		return request.getContentType();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		return request.getLocalName();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		return request.getLocalPort();
	}

	public Locale getLocale() {
		return request.getLocale();
	}

	@SuppressWarnings("unchecked")
	public Enumeration getLocales() {
		return request.getLocales();
	}

	public String getParameter(String arg0) {
		return request.getParameter(arg0);
	}

	@SuppressWarnings("unchecked")
	public Map getParameterMap() {
		return request.getParameterMap();
	}

	@SuppressWarnings("unchecked")
	public Enumeration getParameterNames() {
		return request.getParameterNames();
	}

	public String[] getParameterValues(String arg0) {
		return request.getParameterValues(arg0);
	}

	public String getProtocol() {
		return request.getProtocol();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@SuppressWarnings("deprecation")
	public String getRealPath(String arg0) {
		return request.getRealPath(arg0);
	}

	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return request.getRequestDispatcher(arg0);
	}

	public String getScheme() {
		return request.getScheme();
	}

	public String getServerName() {
		return request.getServerName();
	}

	public int getServerPort() {
		return request.getServerPort();
	}

	public boolean isSecure() {
		return request.isSecure();
	}

	public void removeAttribute(String arg0) {
		request.removeAttribute(arg0);		
	}

	public void setAttribute(String arg0, Object arg1) {
		request.setAttribute(arg0, arg1);		
	}

	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding(arg0);		
	}

}
