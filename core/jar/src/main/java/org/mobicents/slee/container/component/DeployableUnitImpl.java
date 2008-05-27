/*
 * DeployableUnitImpl.java
 * 
 * Created on Nov 18, 2004
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

import java.util.Date;
import java.util.HashSet;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

/**
 *
 */
public class DeployableUnitImpl implements DeployableUnitDescriptor {
    
    private String url;
    
    private Date deploymentDate;
    
    private HashSet components;
    
    private DeployableUnitIDImpl deployableUnitID;
    

    
    /**
     * @param deploymentDate The deploymentDate to set.
     */
    public void setDeploymentDate(Date deploymentDate) {
        this.deploymentDate = deploymentDate;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /* (non-Javadoc)
     * @see javax.slee.management.DeployableUnitDescriptor#getURL()
     */
    public String getURL() {
       
        return this.url;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.DeployableUnitDescriptor#getDeploymentDate()
     */
    public Date getDeploymentDate() {
       
        return this.deploymentDate;
    }
    
    

    /* (non-Javadoc)
     * @see javax.slee.management.DeployableUnitDescriptor#getComponents()
     */
    public ComponentID[] getComponents() {
       
        ComponentID[] componentIDs = new ComponentID[ components.size()];
        this.components.toArray(componentIDs);
        return componentIDs;
    }
    
    public void addComponent( ComponentID componentId) {
        this.components.add(componentId);
    }

    public DeployableUnitID getDeployableUnitID() {
        return this.deployableUnitID;
    }
}

