
/*
*
* The Open SLEE Project.
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*
*/

package org.mobicents.slee.container.component;

/**
 * Declares the presence of the get Profile CMP method and associates the method
 * with a Profile Specification. This SBB Developer specifies the following aspects
 * of the get Profile CMP method using the get-profile-cmp-method element: \u00B7
 *  - The method name of the get Profile CMP method. \u00B7
 *  - The Profile Specification of the get Profile CMP method.
 *
 * @author Emil Ivov
 */

public class ProfileCMPMethod implements Comparable{

    private ComponentKey profileSpecKey = null;
    private String profileCMPMethod = null;

    public ProfileCMPMethod(ComponentKey profileSpecKey, String profileCMPMethod)
    {
        if (profileSpecKey != null && profileCMPMethod != null) {
         	this.profileSpecKey = profileSpecKey;
        	this.profileCMPMethod = profileCMPMethod;
        }
        else {
        	throw new IllegalArgumentException("null arg(s)");
        }
    }

    /**
     * Returns a key to a Profile Specification that is specified in a profile-spec
     * element within the same sbb element.
     * @return ComponentKey a key to a Profile Specification specified
     * within the same sbb element.
     */
    public ComponentKey getProfileSpecKey()
    {
        return profileSpecKey;
    }

    /**
     * Returns the name of the method used to get an object that implements a
     * Profile CMP interface from a Profile identifier. The Profile CMP interface
     * is specified by the Profile Specification identified by this.profileSpecKey
     *
     * @return String
     */
    public String getProfileCMPMethod()
    {
        return profileCMPMethod;
    }
    
    public boolean equals (Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		ProfileCMPMethod other = (ProfileCMPMethod) obj;
    		return other.profileSpecKey.equals(this.profileSpecKey)
            	&& other.profileCMPMethod == this.profileCMPMethod;
    	}
    	else {
    		return false;
    	}         
    }
    
    @Override
    public int hashCode() {
    	return profileSpecKey.hashCode()*31+profileCMPMethod.hashCode();
    }
    
    public int compareTo (Object obj) {
    	ProfileCMPMethod that = (ProfileCMPMethod) obj;
    	return this.profileSpecKey.compareTo(that.profileSpecKey);
    	
    } 


}
