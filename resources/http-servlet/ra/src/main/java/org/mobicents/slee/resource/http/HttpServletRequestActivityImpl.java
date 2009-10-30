package org.mobicents.slee.resource.http;

import java.util.UUID;

import net.java.slee.resource.http.HttpServletRequestActivity;

public class HttpServletRequestActivityImpl extends AbstractHttpServletActivity implements HttpServletRequestActivity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public HttpServletRequestActivityImpl() {
		super(UUID.randomUUID().toString());
	}
	
	public String getRequestID() {
		return id;
	}

}
