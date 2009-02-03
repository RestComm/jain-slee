package org.mobicents.slee.container.component.deployment;


import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;

/**
 *An implementation interface that allows us to set the deployable unit id.
 *
 *@author M. Ranganathan
 */
public interface DeployedComponent {
    
   
    public void setDeployableUnit( DeployableUnitID deployableUnitID);
    public DeployableUnitID getDeployableUnit( );
    
    public void checkDeployment() throws DeploymentException;
 

}

