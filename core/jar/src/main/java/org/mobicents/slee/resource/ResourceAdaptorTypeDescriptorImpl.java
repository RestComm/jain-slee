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

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.DeployedComponent;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia (Original)
 * @author M. Ranganathan ( hacks)
 */
public class ResourceAdaptorTypeDescriptorImpl 
	implements ResourceAdaptorTypeDescriptor, 
	DeployedComponent, Serializable {

	private static final long serialVersionUID = 8363960476724442115L;

    // This is the optional information element in the dd
    private String descriptor;
    // These elements uniquely identify the Resource Adaptor Type
    private ResourceAdaptorTypeIDImpl resourceAdaptorTypeId;
    
    // Class
    private ResourceAdaptorTypeClassEntry raTypeClassEntry;
    
    private ComponentKey[] eventTypeRefEntries;
    
    // Note that this is not the component ID
    private DeployableUnitID deployableUnitID;
      
    // The event types that this ResourceAdaptor type references.
    private EventTypeID[] eventTypes;
    
    public ResourceAdaptorTypeDescriptorImpl() {
       
    }    

    /**
     * @return Returns the descriptor.
     */
    public String getDescriptor() {
        return descriptor;
    }
    
    /**
     * @param descriptor The descriptor to set.
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    /**
     * @return Returns the eventTypeRefEntries.
     */
    public ComponentKey[] getEventTypeRefEntries() {
        return eventTypeRefEntries;
    }
    
    
    /**
     * @param eventTypeRefEntries The eventTypeRefEntries to set.
     */
    public void setEventTypeRefEntries(ComponentKey[] eventTypeRefEntries) {
        this.eventTypeRefEntries = eventTypeRefEntries;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.resourceAdaptorTypeId.getComponentKey().getName();
    }
   
    /**
     * @return Returns the raClassEntry.
     */
    public ResourceAdaptorTypeClassEntry getRaTypeClassEntry() {
        return raTypeClassEntry;
    }
    /**
     * @param raClassEntry The raClassEntry to set.
     */
    public void setRaTypeClassEntry(ResourceAdaptorTypeClassEntry raClassEntry) {
        this.raTypeClassEntry = raClassEntry;
    }
    /**
     * @return Returns the vendor.
     */
    public String getVendor() {
        return this.resourceAdaptorTypeId.getComponentKey().getVendor();
    }
    
    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return this.resourceAdaptorTypeId.getComponentKey().getVersion();
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptorTypeDescriptor#getEventTypes()
     */
    public EventTypeID[] getEventTypes() {
        return this.eventTypes;
    }
    
    /**
     * This method is called during deployment after resolving the event type refs to event type ids.
     * 
     * @param eventTypes
     */
    public void setEventTypes( EventTypeID[] eventTypes ) {
        this.eventTypes = eventTypes;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
     */
    public DeployableUnitID getDeployableUnit() {
        return this.deployableUnitID;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getSource()
     */
    public String getSource() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getID()
     */
    public ComponentID getID() {
        return this.resourceAdaptorTypeId;
    }



    /**
     * @param raTypeID
     */
    public void setResourceAdaptorTypeID(ResourceAdaptorTypeIDImpl raTypeID) {
        this.resourceAdaptorTypeId = raTypeID;
        
    }



    /**
     * Note that this needs to be checked in the container to make sure that this 
     * event type actually exists.
     * 
     * @param eventTypeRefEntry -- an array of component keys that references
     * 		event types.
     * 
     */
    public void setEventTypeRefs(ComponentKey[] eventTypeRefEntries) {
       this.eventTypeRefEntries = eventTypeRefEntries;   
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#setDeployableUnit(javax.slee.management.DeployableUnitID)
     */
    public void setDeployableUnit(DeployableUnitID deployableUnitID) {
        this.deployableUnitID = deployableUnitID;
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#checkDeployment()
     */
    public void checkDeployment() throws DeploymentException {
        //TODO Auto-generated method stub
    }



	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}

}
