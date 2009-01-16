/*
 * Created on Mar 7, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource.tck;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import com.opencloud.sleetck.lib.resource.TCKActivityID;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class TCKActivityHandle implements ActivityHandle, Serializable {

    /**
     * @return Returns the activityID.
     */
    public TCKActivityID getActivityID() {
        return activityID;
    }
    
    private TCKActivityID activityID;
    
    public TCKActivityHandle(TCKActivityID activityId){
        if(activityId != null) {
        	this.activityID = activityId;
        }
        else {
        	throw new IllegalArgumentException("activityId is null");
        }
    }
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		return ((TCKActivityHandle)obj).activityID.equals(this.activityID);
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
        return this.activityID.hashCode();
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return this.activityID.toString();
    }
}
