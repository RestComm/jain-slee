/*
 * ResourceAdaptorEntityBinding.java
 * 
 * Created on Nov 4, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.component;

import java.io.Serializable;

/**
 * Zero or more resource-adaptor-entity-binding elements. Each
 * resource-adaptor-entity-binding element binds an object that implements the
 * resource adaptor interface of the resource adaptor type into the JNDI comp
 * onent environment of the SBB (see Section 6.13.3). Each
 * resource-adaptorentity- binding element contains the following sub-elements:
 * A description element. This is an optional informational element. A
 * resource-adaptor-object–name element. This element specifies the location
 * within the JNDI component environment to which the object that implements the
 * resource adaptor interface will be bound. A resource-adaptor-entity-link
 * element. This is an optional element. It identifies the resource adaptor
 * entity that provides the object that should be bound into the JNDI component
 * environment of the SBB. The identified resource adaptor entity must be an
 * instance of a resource adaptor whose resource adaptor type is specified by
 * the resourceadaptor- type-ref sub-element of the enclosing
 * resource-adaptortype- binding element.
 * 
 * @author M. Ranganathan
 *  
 */
public class ResourceAdaptorEntityBinding implements Serializable {
    private String description;

    private String resourceAdapterObjectName;

    private String resourceAdaptorEntityLink;

    public ResourceAdaptorEntityBinding(String description,
            String resourceAdapterObjectName, String resourceAdapterEntityLink) {
        this.description = description;
        this.resourceAdaptorEntityLink = resourceAdapterEntityLink;
        this.resourceAdapterObjectName = resourceAdapterObjectName;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the R.A. Ojbect name -- 
     * the resource adaptor object name is the JDNI name where the 
     * SBB will expect to find the resource adaptor entity in comp/env.
     * @return Returns the resourceAdapterObjectName.
     */
    public String getResourceAdapterObjectName() {
        return resourceAdapterObjectName;
    }

    /**
     * @return Returns the resourceAdaptorEntityLink.
     */
    public String getResourceAdaptorEntityLink() {
        return resourceAdaptorEntityLink;
    }
}

