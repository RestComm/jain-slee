/*
 * The Open SLEE project
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component;

import java.io.Serializable;

import javax.slee.management.DeploymentException;
import javax.slee.management.EventTypeDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.ComponentID;

/**
 * This is fake implementation to hide package access to some other features of real implementation.
 * @baranowb
 * @version 1.0
 */

public interface MobicentsEventTypeDescriptor extends EventTypeDescriptor, DeployedComponent, Serializable {
    


    /**
     * Get the name of the interface or class used in the event type.
     *
     * @return the name of the interface or class used in the event type.
     */
    public String getEventClassName() ;

    /**
     * Sets the identifier of the deployable unit from which this component was
     * installed.
     *
     * @param deployableUnit the identifier of the deployable unit from which
     * this component was installed.
     */
    public void setDeployableUnit(DeployableUnitID deployableUnit) ;

    /**
     * Get the identifier of the deployable unit from which this component was
     * installed.
     *
     * @return the identifier of the deployable unit from which this component
     *   was installed.
     */
    public DeployableUnitID getDeployableUnit() ;

   

    /**
     * Get the name of the source object from which this component was installed.
     *
     * @return the name of the source object from where this component was
     *   installed.
     */
    public String getSource() ;

    /**
     * Sets the component identifier for this descriptor.
     *
     * @param componentID the component identifier for this descriptor.
     */
    public void setID(ComponentID componentID) ;

    /**
     * Get the component identifier for this descriptor.
     *
     * @return the component identifier for this descriptor.
     */
    public ComponentID getID() ;

   

    /**
     * Get the name of the component.
     *
     * @return the name of the component.
     */
    public String getName() ;

    /**
     * Get the vendor of the component.
     *
     * @return the vendor of the component.
     */
    public String getVendor();

    /**
     * Get the version of the component.
     *
     * @return the version of the component.
     */
    public String getVersion() ;
	
	
	
	
}
