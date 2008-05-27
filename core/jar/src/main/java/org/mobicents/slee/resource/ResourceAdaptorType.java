/*
 * Created on Oct 21, 2004
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
package org.mobicents.slee.resource;

import org.jboss.util.naming.NonSerializableFactory;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * 
 * An object that tracks all the resource adaptors for a given resource
 * adaptor type id. There can be multiple resource adaptors given a resource
 * adaptor type. There can be multiple resource adaptor entities for a given
 * resource adaptor. This structure maps a given resource adaptor type to 
 * multiple resource adaptor IDs.
 * 
 * 
 * @author F.Moggia
 * @author M. Ranganathan ( hacks )
 */
public class ResourceAdaptorType implements Serializable{
    private ResourceAdaptorTypeIDImpl resourceAdaptorTypeID;
    // Every resource adaptor type can have multiple Resource Adaptor Implementation
    private HashSet resourceAdaptorIDs;
    private ResourceAdaptorTypeDescriptorImpl raTypeDescr;
    private String activityContextInterfaceJNDIName;
    
    public ResourceAdaptorType(ResourceAdaptorTypeDescriptorImpl ratDescr) {
        this.resourceAdaptorIDs = new HashSet();
        this.raTypeDescr = ratDescr;
        this.resourceAdaptorTypeID =  new ResourceAdaptorTypeIDImpl
            	(new ComponentKey(ratDescr.getName(), ratDescr.getVendor(), ratDescr.getVersion()));
        
    }
    
    /**
     * @return Returns the key.
     */
    public ResourceAdaptorTypeIDImpl getResourceAdaptorTypeID() {
        return this.resourceAdaptorTypeID;
    }
   
    /**
     * @return Returns the raTypeDescr.
     */
    public ResourceAdaptorTypeDescriptorImpl getRaTypeDescr() {
        return raTypeDescr;
    }
    
    /**
     * @return Returns the resourceAdaptors.
     */
    public HashSet getResourceAdaptorIDs() {
        return this.resourceAdaptorIDs;
    }
   
    
    
    public void addResourceAdaptor ( ResourceAdaptorIDImpl resourceAdaptorID) {
        this.resourceAdaptorIDs.add(resourceAdaptorID); 
    }
    
   

    public String getActivityContextInterfaceFactoryJNDIName() {
        return this.activityContextInterfaceJNDIName;
        
    }
    
    public void setActivityContextInterfaceJNDIName(String name) {
        this.activityContextInterfaceJNDIName = name;
    }
    
}
