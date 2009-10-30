package org.mobicents.slee.resource.http;

import net.java.slee.resource.http.HttpSessionActivity;

/**
 * 
 * @author amit.bhayani
 * @author martins
 * 
 */
public class HttpSessionActivityImpl extends AbstractHttpServletActivity implements HttpSessionActivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HttpSessionActivityImpl(String sessionId) {
		super(sessionId);
	}

	public String getSessionId() {
		return id;
	}
	
}
