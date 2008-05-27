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

import org.mobicents.slee.resource.*;

/**
 * This corresponds to an resource-adaptor-type-classes entry in the resource
 * adaptor type descriptor.
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorTypeClassEntry  implements Serializable {
    private String description;
    private ActivityTypeEntry[] activityTypeEntries;
    private ResourceAdaptorInterfaceEntry raInterfaceFactoryEntry;
    private ActivityContextInterfaceFactoryInterfaceEntry acifInterfaceEntry;
    
    /**
     * @return Returns the acifInterfaceEntry.
     */
    public ActivityContextInterfaceFactoryInterfaceEntry getAcifInterfaceEntry() {
        return acifInterfaceEntry;
    }
    /**
     * @param acifInterfaceEntry The acifInterfaceEntry to set.
     */
    public void setAcifInterfaceEntry(
            ActivityContextInterfaceFactoryInterfaceEntry acifInterfaceEntry) {
        this.acifInterfaceEntry = acifInterfaceEntry;
    }
    /**
     * @return Returns the activityTypeEntries.
     */
    public ActivityTypeEntry[] getActivityTypeEntries() {
        return activityTypeEntries;
    }
    /**
     * @param activityTypeEntries The activityTypeEntries to set.
     */
    public void setActivityTypeEntries(
            ActivityTypeEntry[] activityTypeEntries) {
        this.activityTypeEntries = activityTypeEntries;
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
     * @return Returns the raInterfaceFactoryEntry.
     */
    public ResourceAdaptorInterfaceEntry getRaInterfaceFactoryEntry() {
        return raInterfaceFactoryEntry;
    }
    /**
     * @param raInterfaceFactoryEntry The raInterfaceFactoryEntry to set.
     */
    public void setRaInterfaceFactoryEntry(
            ResourceAdaptorInterfaceEntry raInterfaceFactoryEntry) {
        this.raInterfaceFactoryEntry = raInterfaceFactoryEntry;
    }
}
