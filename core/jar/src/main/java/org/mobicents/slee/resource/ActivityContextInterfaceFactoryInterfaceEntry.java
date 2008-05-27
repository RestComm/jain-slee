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
 * 15.1.2 Resource adaptor type deployment descriptor (page 240)
 * 
 * An activity-context-interface-factory-interface element. This element is
 * optional. It contains the following sub-elements:
 * 
 * <p>
 * A description element. This is an optional informational element.
 * 
 * <p>
 * An activity-context-interface-factory-interface-name element. This element
 * identifies the interface name of the Activity Context Interface Factory of
 * the resource adaptor type.
 * 
 * @author F.Moggia
 */

public class ActivityContextInterfaceFactoryInterfaceEntry implements
        Serializable {
    private String description;

    private String interfaceName;

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
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * @param interfaceName
     *            The interfaceName to set.
     */
    public void setInterfaceName(String name) {
        this.interfaceName = name;
    }
}