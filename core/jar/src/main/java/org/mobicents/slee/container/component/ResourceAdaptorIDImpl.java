/*
 * Created on Oct 26, 2004
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
package org.mobicents.slee.container.component;

import java.io.Serializable;

import javax.slee.resource.ResourceAdaptorID;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorIDImpl extends ComponentIDImpl implements 
ResourceAdaptorID, Serializable{

    
    
    public ResourceAdaptorIDImpl(String name, String vendor, String version) {
        super(new ComponentKey(name, vendor, version));
        
    }
    
    public ResourceAdaptorIDImpl(ComponentKey key) {
        super(key);
    }

    
    public String toString() {
        return "ResourceAdaptorID[" + id + "]";
    }
    
    public ComponentKey getComponentKey(){
        return id;
    }
    
   

}
