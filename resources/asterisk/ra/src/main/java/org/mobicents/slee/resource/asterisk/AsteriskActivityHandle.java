/*
 * Created on 11/Abr/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.resource.asterisk;

import javax.slee.resource.ActivityHandle;

/**
 * @author Sancho
 * @version 1.0
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AsteriskActivityHandle implements ActivityHandle {
	String connectionId;
	
    public AsteriskActivityHandle(String connectionId){
        this.connectionId = connectionId;
   }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((AsteriskActivityHandle)o).connectionId.equals(this.connectionId);
		}
		else {
			return false;
		}
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // TODO Auto-generated method stub
        return connectionId.hashCode();
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return this.connectionId;
    }
	
}
