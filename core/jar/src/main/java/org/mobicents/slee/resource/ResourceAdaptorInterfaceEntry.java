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
 * 15.1.2 Resource adaptor type deployment descriptor
 * 
 * A resource-adaptor-interface element. This element is optional. It specifies
 * the Java type of the resource adaptor interface. Each resource adaptor of
 * this resource adaptor type provides an implementation of this resource
 * adaptor interface, An SBB can bind an object of this Java type into its JNDI
 * component environment using a resource-adaptor-entity-binding element in the
 * SBB’s deployment descriptor. The resource-adaptor-interface element contains
 * the following sub-elements: <p> A description element. This is an optional
 * informational element. <p> A resource-adaptor-interface-name element. This
 * element specifies the Java type of the resource adaptor interface.
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorInterfaceEntry implements Serializable {
    private String description;

    private String name;

    /**
     * @return Returns the description.
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

    /**
     * @return Returns the interfaceName.
     */
    public String getName() {
        return name;
    }

    /**
     * @param interfaceName
     *            The interfaceName to set.
     */
    public void setName(String name) {
        this.name = name;
    }

}