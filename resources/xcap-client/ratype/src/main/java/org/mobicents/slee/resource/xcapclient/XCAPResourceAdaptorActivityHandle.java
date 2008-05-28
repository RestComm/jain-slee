package org.mobicents.slee.resource.xcapclient;

import javax.slee.resource.ActivityHandle;

/**
 * @author Eduardo Martins
 * @version 1.0
 * 
 */

public class XCAPResourceAdaptorActivityHandle implements ActivityHandle {
	
	private String id;
	
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
    
    public String toString() {
        return "activityHandle:id="+id;
    }
    
    public int hashCode() {
    	return id.hashCode();
    }
}
