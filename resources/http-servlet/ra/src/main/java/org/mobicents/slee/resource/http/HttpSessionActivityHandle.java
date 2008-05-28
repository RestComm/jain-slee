package org.mobicents.slee.resource.http;

import javax.slee.resource.ActivityHandle;

/**
 * 
 * Activity handle wrapper for HttpSession activity with given unique session ID
 * 
 * @author Ivelin Ivanov
 * 
 */

public class HttpSessionActivityHandle implements ActivityHandle {
	
	private String sessionId;
	
    public HttpSessionActivityHandle(String newSessionId){
        this.sessionId = newSessionId;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((HttpSessionActivityHandle)o).sessionId.equals(this.sessionId);
		}
		else {
			return false;
		}
    }    
    
    public String toString() {
        return "HttpSession ID: " + sessionId;
    }
    
    public int hashCode() {
    	return sessionId.hashCode();
    }
}
