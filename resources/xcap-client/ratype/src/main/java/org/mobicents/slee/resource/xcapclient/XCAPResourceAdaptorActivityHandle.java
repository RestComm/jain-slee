package org.mobicents.slee.resource.xcapclient;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

/**
 * @author Eduardo Martins
 * @version 1.0
 * 
 */

public class XCAPResourceAdaptorActivityHandle implements ActivityHandle, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String id;
	
    public XCAPResourceAdaptorActivityHandle(String id){
        this.id = id;
    }
    
    public String getId() {
		return id;
	}
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((XCAPResourceAdaptorActivityHandle)o).id.equals(this.id);
		}
		else {
			return false;
		}
    }    
    
    private final static String PREFIX = "xcapActivityHandle:id=";
    
    public String toString() {
        return new StringBuilder(PREFIX).append(id).toString();
    }
    
    public int hashCode() {
    	return id.hashCode();
    }
}
