package org.mobicents.slee.resource.http;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 
 * Provides an entry point for a designated HttpServlet to the Resource Adaptor.
 * It bridges incoming Http requests via an HttpServlet to the RA.
 * 
 * Implements the generic Servlet interface in order to eliminate class type
 * dependecny between the http servlet and the RA. Only the service() method is
 * implemented, the rest through {@link UnsupportedOperationException};
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * 
 */
public class HttpServletResourceEntryPoint implements Servlet {
	private static Logger logger = Logger
			.getLogger(HttpServletResourceEntryPoint.class.getName());

	// Location where this instance is bound in JNDI
	private String jndiName = null;

	private HttpServletResourceAdaptor httpra;

	private static final String RA_ENTRY_POINT_PARAM = "ra-entry-point-jndi-name";

	public HttpServletResourceEntryPoint(String name,
			HttpServletResourceAdaptor newRA) {		
		this.jndiName = "java:slee/resources/" + name
				+ "/http-servlet-resource-entry-point";
		this.httpra = newRA;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void service(HttpServletRequest request, HttpServletResponse response) {
		httpra.onRequest(request, response);
	}

	public void destroy() {
		throw new UnsupportedOperationException();
	}

	public ServletConfig getServletConfig() {
		throw new UnsupportedOperationException();
	}

	public String getServletInfo() {
		throw new UnsupportedOperationException();
	}

	public void init(ServletConfig arg0) throws ServletException {
		throw new UnsupportedOperationException();
	}

	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		// Explicitly create the session if it doenot exist
		HttpSession session = ((HttpServletRequest) request).getSession();

		/**
		 * Set the HttpServletResourceEntryPoint in Session so that HttpServletRaSessionListener can
		 * retrieve it latter to end the HttpSessionActivity
		 */
		session.setAttribute(RA_ENTRY_POINT_PARAM, this);

		// assert request instanceof HttpServletRequest: "invalid argument type.
		// request should be instanceof HttpServletRequest";
		// assert response instanceof HttpServletResponse: "invalid argument
		// type. response should be instanceof HttpServletResponse";
		this.service((HttpServletRequest) request,
				(HttpServletResponse) response);
	}

	public void endHttpSessionActivity(String sessionId) {
		this.httpra.endHttpSessionActivity(sessionId);
	}

}
