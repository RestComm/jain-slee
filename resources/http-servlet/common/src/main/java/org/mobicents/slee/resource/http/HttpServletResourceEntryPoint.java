package org.mobicents.slee.resource.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Provides an entry point for a designated HttpServlet to the Resource Adaptor.
 * It bridges incoming Http requests via an HttpServlet to the RA.
 * 
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * 
 */
public interface HttpServletResourceEntryPoint {
	
	public void onRequest(HttpServletRequest request, HttpServletResponse response);
	
	public void onSessionTerminated(String sessionId);

}
