package org.mobicents.slee.runtime.facilities.profile;

import java.io.Serializable;

import javax.slee.profile.ProfileTableActivity;

import org.mobicents.slee.container.SleeContainer;


/**
 *Implementation of the profile table activity object.
 *
 *@author M. Ranganathan
 *@author martins
 */
public class ProfileTableActivityImpl implements ProfileTableActivity, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9157166426621118148L;
    
	private final ProfileTableActivityHandle handle;
    
    public ProfileTableActivityImpl (ProfileTableActivityHandle handle) {        
        this.handle = handle;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileTableActivity#getProfileTableName()
     */
    public String getProfileTableName() {
        return handle.getProfileTable();
         
    }
    
    protected ProfileTableActivityHandle getHandle() {
		return handle;
	}
    
    public boolean equals(Object obj){
    	if ((obj != null) && (obj.getClass() == this.getClass())) {
    		 return this.handle.equals(((ProfileTableActivityImpl)obj).handle);
    	}
    	else {
    		return false;
    	}
    }
    
    public int hashCode() {
        return handle.hashCode();
    }
    
}

