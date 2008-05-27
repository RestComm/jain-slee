/*
 * Created on Oct 25, 2004
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

/**
 * 
 * This part of the Resource Adaptor is not yet defined. Indicates a 
 * set of classes used to implement the resource adaptor.
 * 
 * @author F.Moggia
 */
public class RAClassesEntry  implements java.io.Serializable {
    private String resourceAdaptorClassName;
    
    public RAClassesEntry(){
        
    }
    
    public RAClassesEntry ( String raClassName ) {
        this.resourceAdaptorClassName = raClassName;
    }
    

    
    /**
     * @return Returns the resourceAdaptorClass class name.
     */
    public String getResourceAdaptorClass() {
        return resourceAdaptorClassName;
    }
    /**
     * @param resourceAdaptorClass The resourceAdaptorClass to set.
     */
    public void setResourceAdaptorClass(String resourceAdaptorClass) {
        this.resourceAdaptorClassName = resourceAdaptorClass;
    }
}
