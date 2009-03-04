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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.resource.ResourceAdaptorDescriptor;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * Descriptor for a ResourceAdaptor.
 * 
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorDescriptorImpl implements ResourceAdaptorDescriptor , 
										 Serializable {

	private static final long serialVersionUID = -6932034951470181468L;
	private String description;
    private ResourceAdaptorIDImpl componentId;
    
    //Reference to the resource adaptor type variable.
    private ResourceAdaptorTypeIDImpl resourceAdaptorTypeID;
    // Not yet define 
    private RAClassesEntry raClasses;
    
    //Deployable Unit ID for this deployable unit.
    private DeployableUnitID deployableUnitID;
    
    //Collection of the configuration properties
    private HashMap configProperties = new HashMap();
    
    public ResourceAdaptorDescriptorImpl(){
        
    }
    
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return Returns the raClasses.
     */
    public RAClassesEntry getResourceAdaptorClasses() {
        return raClasses;
    }
    /**
     * @param raClasses The raClasses to set.
     */
    public void setResourceAdaptorClasses(RAClassesEntry raClasses) {
        this.raClasses = raClasses;
    }
    
    /**
     * @param resourceAdaptorTypeID The resourceAdaptorTypeID to set.
     */
    public void setResourceAdaptorType(ResourceAdaptorTypeIDImpl raType) {
        this.resourceAdaptorTypeID = raType;
    }

    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptorDescriptor#getResourceAdaptorType()
     */
    public ResourceAdaptorTypeID getResourceAdaptorType() {
        return this.resourceAdaptorTypeID;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
     */
    public DeployableUnitID getDeployableUnit() {
        return this.deployableUnitID;
    }

    public void setDeployableUnitID (DeployableUnitIDImpl deployableUnitID ) {
        this.deployableUnitID = deployableUnitID;
    }

	public void checkDeployment() throws DeploymentException {
		// TODO Auto-generated method stub

	}
	
	public void setDeployableUnit(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}
    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getSource()
     */
    public String getSource() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getID()
     */
    public ComponentID getID() {
       
        return this.componentId;
    }

    public void setID ( ResourceAdaptorIDImpl id) {
        this.componentId = id;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getName()
     */
    public String getName() {
       
        return this.componentId.getComponentKey().getName();
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getVendor()
     */
    public String getVendor() {
       
        return this.componentId.getComponentKey().getVendor();
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getVersion()
     */
    public String getVersion() {
       
        return this.componentId.getComponentKey().getVersion();
    }
    
    /**
     * Adds configuration property to a collection.
     * If property with the sampe name already exists it will be replaced.
     *
     * @param configProperty the class which represents configuration property 
     * been added.
     */
    public void add(ConfigPropertyDescriptor configProperty) {
        configProperties.put(configProperty.getName(), configProperty);
    }
    
    /**
     * Returns configuration property with name specified.
     *
     * @param name the name of the property been returned.
     * @return an object which represents the configuration property.
     */
    public ConfigPropertyDescriptor getConfigProperty(String name) {
        return (ConfigPropertyDescriptor) configProperties.get(name);
    }
    
    /**
     * Returns configuration property descriptors.
     *
     * @return collection of the descriptors.
     */
    public Collection getConfigPropertyDescriptors() {
        return configProperties.values();
    }

	public ResourceAdaptorTypeID[] getResourceAdaptorTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}
}
