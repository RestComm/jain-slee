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

/**
 * See Spec Section 15.1.2 (page 240)
 * 
 * Resource adaptor type deployment descriptor.
 * This corresponds to the <activity-type> node of the resource adaptor
 * deployment descriptor. Zero or more activity-type elements. The Java types
 * of all the resource adaptor type’s Activity objects must be declared. Each
 * activity-type element declares one of these Java types. Each activity-type
 * element contains the following sub-elements: 
 * <p>
 *  A description element. This is
 * an optional informational element. This element should document the
 * conditions that end Activity objects of this Java type. 
 * <p>
 * An
 * activity-type-name element. This element identifies the name of the Java
 * type. The Java type must be either an interface or a class.
 * 
 * @author F.Moggia
 */
public class ActivityTypeEntry implements Serializable {
    private String description;

    private String activityTypeName;

    /**
     * @return Returns the activityTypeName. 
     */
    public String getActivityTypeName() {
        return activityTypeName;
    }

    /**  Set the activity type name -- this element identifies
     *  the name of the Java  type. The Java type must be either
     *  an interface or a class.
     * 
     * @param activityTypeName
     *            The activityTypeName to set.
     * 
   
     * 
     */
    public void setActivityTypeName(String className) {
        this.activityTypeName = className;
    }

    /**
     * @return Returns the description element.
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}