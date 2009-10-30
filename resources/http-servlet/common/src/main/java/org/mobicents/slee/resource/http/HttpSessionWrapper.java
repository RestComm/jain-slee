package org.mobicents.slee.resource.http;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class HttpSessionWrapper implements HttpSession {

	private final HttpSession httpSession;
	
	private static final String RESOURCE_ENTRY_POINT_ATTR_NAME = "_ENTRY_POINT";

	public HttpSessionWrapper(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	/**
	 * 
	 * @return
	 */
	public String getResourceEntryPoint() {
		return (String) httpSession.getAttribute(RESOURCE_ENTRY_POINT_ATTR_NAME);
	}
	
	/**
	 * 
	 * @param resourceEntryPoint
	 */
	public void setResourceEntryPoint(String resourceEntryPoint) {
		httpSession.setAttribute(RESOURCE_ENTRY_POINT_ATTR_NAME,resourceEntryPoint);
	}
	
	public Object getAttribute(String arg0) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		return httpSession.getAttribute(arg0);
	}

	@SuppressWarnings("unchecked")
	public Enumeration getAttributeNames() {
		return httpSession.getAttributeNames();
	}

	public long getCreationTime() {
		return httpSession.getCreationTime();
	}

	public String getId() {
		return httpSession.getId();
	}

	public long getLastAccessedTime() {
		return httpSession.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
		return httpSession.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
		return httpSession.getServletContext();
	}

	public HttpSessionContext getSessionContext() {
		return httpSession.getSessionContext();
	}

	public Object getValue(String arg0) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		return httpSession.getValue(arg0);
	}

	public String[] getValueNames() {
		return httpSession.getValueNames();
	}

	public void invalidate() {
		httpSession.invalidate();
	}

	public boolean isNew() {
		return httpSession.isNew();
	}

	public void putValue(String arg0, Object arg1) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		httpSession.putValue(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		httpSession.removeAttribute(arg0);
	}

	public void removeValue(String arg0) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		httpSession.removeValue(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		if (arg0.equals(RESOURCE_ENTRY_POINT_ATTR_NAME)) {
			throw new SecurityException("it is forbidden to use the attribute name "+arg0);
		}
		httpSession.setAttribute(arg0, arg1);
	}

	public void setMaxInactiveInterval(int arg0) {
		httpSession.setMaxInactiveInterval(arg0);		
	}

}
