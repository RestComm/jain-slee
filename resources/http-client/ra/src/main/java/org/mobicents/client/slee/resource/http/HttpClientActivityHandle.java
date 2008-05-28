package org.mobicents.client.slee.resource.http;

import javax.slee.resource.ActivityHandle;

public class HttpClientActivityHandle implements ActivityHandle {
	
	private String sessionId;
	
    public HttpClientActivityHandle(String newSessionId){
        this.sessionId = newSessionId;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((HttpClientActivityHandle)o).sessionId.equals(this.sessionId);
		}
		else {
			return false;
		}
    }    
    
    public String toString() {
        return "HttpClientSession ID: " + sessionId;
    }
    
    public int hashCode() {
    	return sessionId.hashCode();
    }	

}
