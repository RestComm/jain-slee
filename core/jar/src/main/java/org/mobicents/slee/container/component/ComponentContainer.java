package org.mobicents.slee.container.component;

import javax.slee.InvalidStateException;
import javax.slee.management.*;


/**
 * The ComponentContainer interface represents entity that, based on a
 * ComponentDescriptor, install a component in the slee (e.g. Such an
 * implementation is the SleeContainer for example). This is an implementation 
 * interface -- will be removed.
 *
 * @author Emil Ivov
 */

public interface ComponentContainer {

    /**
     * Installs the specified component into the slee.
     * @param impl the component to install
     * @throws DeploymentException if installation fails
     * @todo Declare a more specific exception
     */
    public void install(ComponentDescriptor impl, DeployableUnitDescriptor deployableUnitDescriptor) 
    	throws Exception;
    
    
    /**
     * Add a deployable unit descriptor.
     *
     *@param deployableUnitID -- ID assigned to the deployable Unit.
     *@param url -- url from where the deployable unit was loaded.
     *@param deployableUnitDescriptor -- descriptor for the deployable unit.
     */
    public void addDeployableUnit(DeployableUnitDescriptor deployableUnitID);
    
    
    /**
     * Undeploy deployable unit.
     * 
     * @param deployableUnitID -- id of deployable unit to undeploy.
     * @throws Exception -- if could not remove for any reason
     *
     */
    public void removeDeployableUnit(DeployableUnitID deployableUnitID)
    	throws  Exception;
    
    /**
     * Force remove of all components installed as part of this DU even 
     * before DU installation is complete.
     *
     */
    public void removeDU ( DeployableUnitDescriptorImpl deployableUnitDescriptor) throws Exception;
    
   
    
    

}
