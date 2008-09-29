package org.mobicents.slee.resource.http;

import net.java.slee.resource.http.HttpSessionActivity;

/**
 * 
 * @author amit.bhayani
 * @author martins
 * 
 */
public class HttpSessionActivityImpl implements HttpSessionActivity {

	private String sessionId;

	public HttpSessionActivityImpl(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public int hashCode() {
		return sessionId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((HttpSessionActivityImpl)obj).sessionId.equals(this.sessionId);
		}
		else {
			return false;
		}
	}
}
