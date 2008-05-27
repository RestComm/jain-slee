package org.mobicents.slee.resource.xmpp;

import javax.slee.resource.ActivityHandle;


/**
 * @author Eduardo Martins
 * @version 2.1
 * 
 */

public class XmppActivityHandle implements ActivityHandle {
	
	private String connectionId;
	
    public XmppActivityHandle(String connectionId){
        this.connectionId = connectionId;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((XmppActivityHandle)o).connectionId.equals(this.connectionId);
		}
		else {
			return false;
		}
    }    
    
    public String toString() {
        return connectionId;
    }
    
    public int hashCode() {
    	return connectionId.hashCode();
    }
}
