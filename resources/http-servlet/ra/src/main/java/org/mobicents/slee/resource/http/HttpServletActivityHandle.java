package org.mobicents.slee.resource.http;

import javax.slee.resource.ActivityHandle;

/**
 * 
 * Activity handle wrapper for HttpServlet RA activity with given unique ID
 * 
 * @author Ivelin Ivanov
 * 
 */

public class HttpServletActivityHandle implements ActivityHandle {
	
	private Object id;
	
    public HttpServletActivityHandle(Object id){
        this.id = id;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((HttpServletActivityHandle)o).id.equals(this.id);
		}
		else {
			return false;
		}
    }    
    
    public String toString() {
        return "HttpServletActivityHandle:" + id;
    }
    
    public int hashCode() {
    	return id.hashCode();
    }
}
