package org.mobicents.slee.resource.http;

import net.java.slee.resource.http.HttpSessionActivity;

import org.apache.log4j.Logger;

/**
 * 
 * @author amit.bhayani
 * 
 */
public class HttpSessionActivityImpl implements HttpSessionActivity {

	private static Logger logger = Logger
			.getLogger(HttpSessionActivityImpl.class);

	private String sessionId;

	private HttpServletResourceAdaptor ra;

	public HttpSessionActivityImpl(HttpServletResourceAdaptor ra,
			String sessionId) {
		this.sessionId = sessionId;
		this.ra = ra;
	}

	public String getSessionId() {
		return this.sessionId;
	}

}
