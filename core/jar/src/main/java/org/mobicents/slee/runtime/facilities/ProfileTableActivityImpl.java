/*
 * ProfileTableActivityImpl.java
 * 
 * Created on Sep 29, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.profile.ProfileTableActivity;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.ActivityContext;

/**
 *Implementation of the profile table activity object.
 *
 *@author M. Ranganathan
 */
public class ProfileTableActivityImpl implements ProfileTableActivity, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9157166426621118148L;
    
    protected String profileTableName;
    
    /*
     * The Activity Context Interface for this service activity
     */
    private String activityContextId;
    
    public ProfileTableActivityImpl ( String profileTableName) {        
        if (profileTableName != null) {
        	this.profileTableName = profileTableName;
        	ActivityContext ac = SleeContainer.lookupFromJndi().getActivityContextFactory().getActivityContext(this);
        	this.activityContextId = ac.getActivityContextId();
        }
        else {
        	throw new IllegalArgumentException("profileTableName is null");
        }
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileTableActivity#getProfileTableName()
     */
    public String getProfileTableName() {
        return this.profileTableName;
         
    }
    
    /*public Object getProfileEvent () {
        return profileEvent;
    }*/

    public String getActivityContextId() {
        return this.activityContextId;
    }
    
    public boolean equals(Object obj){
    	if ((obj != null) && (obj.getClass() == this.getClass())) {
    		 return this.profileTableName.equals(((ProfileTableActivityImpl)obj).profileTableName);
    	}
    	else {
    		return false;
    	}
    }
    
    public int hashCode() {
        return this.profileTableName.hashCode();
    }
}

