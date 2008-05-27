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
import java.util.HashSet;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.DeployedComponent;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;

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

	static transient final String XML_DESCR_TCK = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n" +
    		"<!DOCTYPE resource-adaptor-type-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN\" \"http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_0.dtd\">" + "\n" + 
    		"<resource-adaptor-type-jar>" + "\n" +
    		"	<resource-adaptor-type>" + "\n" +
    		"		<description>" + "\n" +
    		"       This is the resource adaptor type for the JAIN SLEE TCK 1.0 resource."+ "\n" +
    		"		JAIN SLEE TCK 1.0 users must deploy this resource adaptor type into their SLEE, along with"+ "\n" +
    		"		a JAIN SLEE TCK 1.0 resource adaptor implementation for their SLEE." + "\n" +
    		"		See the JAIN SLEE TCK User Guide for details." + "\n" +
    		"		</description>" + "\n" +
    		"		<!-- id -->" + "\n" +
    		"		<resource-adaptor-type-name>TCK_Resource_Type</resource-adaptor-type-name>" + "\n" +
    		"		<resource-adaptor-type-vendor>jain.slee.tck</resource-adaptor-type-vendor>" + "\n" +
    		"		<resource-adaptor-type-version>1.0</resource-adaptor-type-version>" + "\n" +
    		"		<!-- classes -->" + "\n" +
    		"		<resource-adaptor-type-classes>" + "\n" +
            "			<activity-type>" + "\n" +
            "	    		<activity-type-name>com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity</activity-type-name>" + "\n" +
            "			</activity-type>" + "\n" +
            "			<activity-context-interface-factory-interface>" + "\n" +
            "	    		<activity-context-interface-factory-interface-name>" + "\n" +
            "        			com.opencloud.sleetck.lib.resource.sbbapi.TCKActivityContextInterfaceFactory" + "\n" +
            "    			</activity-context-interface-factory-interface-name>" + "\n" +
            "			</activity-context-interface-factory-interface>" + "\n" +
            "			<resource-adaptor-interface>" + "\n" +
            "				<resource-adaptor-interface-name>" + "\n" +
            "        			com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface" + "\n" +
            "		    	</resource-adaptor-interface-name>" + "\n" +
            "			</resource-adaptor-interface>" + "\n" +
    		"		</resource-adaptor-type-classes>" + "\n" +
    		"		<!-- events-->" + "\n" +
    		"		<event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X1</event-type-name>" + "\n" +
            "				<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "				<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"		<event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X2</event-type-name>" + "\n" +
            "			<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "			<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"		<event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X3</event-type-name>" + "\n" +
            "			<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "			<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"		<event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventY.Y1</event-type-name>" + "\n" +
            "			<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "			<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"       <event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventY.Y2</event-type-name>" + "\n" +
            "			<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "			<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"       <event-type-ref>" + "\n" +
            "			<event-type-name>com.opencloud.sleetck.lib.resource.events.TCKResourceEventY.Y3</event-type-name>" + "\n" +
            "			<event-type-vendor>jain.slee.tck</event-type-vendor>" + "\n" +
            "			<event-type-version>1.0</event-type-version>" + "\n" +
    		"		</event-type-ref>" + "\n" +
    		"	</resource-adaptor-type>" + "\n" +
    		"</resource-adaptor-type-jar>";

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
    
    
    
    public void parseTckRA() {
        descriptor = "This is the resource adaptor type for the JAIN SLEE TCK 1.0 resource."+ "\n" +
    		"		JAIN SLEE TCK 1.0 users must deploy this resource adaptor type into their SLEE, along with"+ "\n" +
    		"		a JAIN SLEE TCK 1.0 resource adaptor implementation for their SLEE." + "\n" +
    		"		See the JAIN SLEE TCK User Guide for details.";
        String name = "TCK_Resource_Type";
        String vendor = "jain.slee.tck";
        String version = "1.0";
        
        this.resourceAdaptorTypeId = new ResourceAdaptorTypeIDImpl( new ComponentKey (name,vendor,version));
        
        raTypeClassEntry = new ResourceAdaptorTypeClassEntry();
        raTypeClassEntry.setDescription("");
        
        ActivityTypeEntry[] atEntries = new ActivityTypeEntry[1];
        atEntries[0] = new ActivityTypeEntry();
        atEntries[0].setDescription("");
        atEntries[0].setActivityTypeName("com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity");
        raTypeClassEntry.setActivityTypeEntries(atEntries);
                                  
        ActivityContextInterfaceFactoryInterfaceEntry acifiEntry = new ActivityContextInterfaceFactoryInterfaceEntry();
        acifiEntry.setDescription("");
        acifiEntry.setInterfaceName("com.opencloud.sleetck.lib.resource.sbbapi.TCKActivityContextInterfaceFactory");
        
        raTypeClassEntry.setAcifInterfaceEntry(acifiEntry);
        
        
        ResourceAdaptorInterfaceEntry raiEntry = new ResourceAdaptorInterfaceEntry();
        raiEntry.setDescription("");
        raiEntry.setName("com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface");
        raTypeClassEntry.setRaInterfaceFactoryEntry(raiEntry);
        
        this.setRaTypeClassEntry(raTypeClassEntry);
        
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
