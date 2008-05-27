/*
 * ResourceAdaptorTypeIDImpl.java
 * 
 * Created on Nov 17, 2004
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

package org.mobicents.slee.resource;

import java.io.Serializable;

import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.ComponentKey;

import javax.slee.management.DeployableUnitID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 *An implementation of the ResourceAdaptorType ID.
 *
 *@author M. Ranganathan.
 */
public class ResourceAdaptorTypeIDImpl 
	extends ComponentIDImpl implements Serializable,ResourceAdaptorTypeID, DeployableUnitID {
    private String description;
    
    public ResourceAdaptorTypeIDImpl( ComponentKey componentKey ) {
        super ( componentKey);
    }
    
    public void setDescription ( String description ) {
        this.description = description;
    }
    
    public String getDescription () {
        return this.description;
    }
    
  

}

