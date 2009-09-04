package org.mobicents.client.slee.resource.http;

import javax.slee.resource.ActivityHandle;

/**
 * @author amit
 * @author martins
 *
 */
public class HttpClientActivityHandle implements ActivityHandle {
	
	private final String sessionId;
	
	/**
	 * 
	 * @param newSessionId
	 */
    public HttpClientActivityHandle(String newSessionId){
        this.sessionId = newSessionId;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((HttpClientActivityHandle)o).sessionId.equals(this.sessionId);
		}
		else {
			return false;
		}
    }    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	return sessionId.hashCode();
    }	

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HttpClientSession ID: " + sessionId;
    }
}
