/*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/

package org.mobicents.slee.container.component;


import java.io.Serializable;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * This class implements the ComponentID interface in the Jain SLEE specs
 * (p.)It uses the class ComponentKey.
 * 
 * 
 * @author Francesco Moggia
 * @author M. Ranganathan
 * 
 */
public  class ComponentIDImpl implements ComponentID,Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String componentType;
    protected ComponentKey id;
    public static final String SBB_ID = "SbbID";
    public static final String SERVICE_ID = "ServiceID" ;
    public static final String RESOURCE_ADAPTOR_TYPE_ID = "ResourceAdaptorTypeID";
    public static final String RESOURCE_ADAPTOR_ID = "ResourceAdaptorID";
    public static final String PROFILE_SPECIFICATION_ID = "ProfileSpecificationID";
    public static final String EVENT_TYPE_ID = "EventTypeID";
    
    public ComponentIDImpl(ComponentKey key) {
    	if (key != null) {
    		this.id = key;
    		this.componentType=getComponentIDType();
    	}
    	else {
    		throw new IllegalArgumentException("Cant create ComponentID of type: "+getComponentIDType()+" with key set to null!!!");
    	}
    }
    
   private String getComponentIDType()
   {
	   if ( this instanceof SbbID) 
			return SBB_ID;
		else if  ( this instanceof ServiceID ) 
			return SERVICE_ID;
		else if  ( this instanceof ResourceAdaptorTypeID )
			return RESOURCE_ADAPTOR_TYPE_ID;
		else if  ( this instanceof ResourceAdaptorID )
			return RESOURCE_ADAPTOR_ID;
		else if  ( this  instanceof ProfileSpecificationID )
			return PROFILE_SPECIFICATION_ID;
		else if ( this instanceof EventTypeID )
			return EVENT_TYPE_ID;
		else return "Unknown";
   }
    
    public ComponentKey getComponentKey(){
        return id;
    }
    
    public boolean isEventTypeID() {
        return this.componentType.equalsIgnoreCase(EVENT_TYPE_ID);
    }
    
    public boolean isSbbID() {
        return this.componentType.equalsIgnoreCase(SBB_ID);
        
    }
    
    public boolean isServiceID() {
        return this.componentType.equalsIgnoreCase(SERVICE_ID);
    }
    
    /**
     * @return
     */
    public boolean isProfileSpecificationID() {        
        return this.componentType.equalsIgnoreCase(PROFILE_SPECIFICATION_ID);
    }
    
    public boolean isResourceAdaptorTypeID() {
        return this.componentType.equalsIgnoreCase(RESOURCE_ADAPTOR_TYPE_ID);
    }
    
    public String getAsText () {
    	return componentType +"[" + id.toString() + "]" ;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return componentType.hashCode()*31+id.hashCode();
    }
    
    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		ComponentIDImpl other = (ComponentIDImpl) obj;
    		return this.componentType.equals( other.componentType) && this.id.equals(other.id);
    	}
    	else {
    		return false;
    	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
       
        return getAsText();
    }  

}
