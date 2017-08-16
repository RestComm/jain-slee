/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.runtime.facilities.profile;

import java.io.Serializable;

import javax.slee.profile.ProfileTableActivity;


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
    
	private final ProfileTableActivityHandleImpl handle;
    
    public ProfileTableActivityImpl (ProfileTableActivityHandleImpl handle) {        
        this.handle = handle;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileTableActivity#getProfileTableName()
     */
    public String getProfileTableName() {
        return handle.getProfileTable();
         
    }
    
    public ProfileTableActivityHandleImpl getHandle() {
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

