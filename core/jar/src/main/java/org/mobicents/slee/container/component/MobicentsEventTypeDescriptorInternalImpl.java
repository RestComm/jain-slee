package org.mobicents.slee.container.component;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

public class MobicentsEventTypeDescriptorInternalImpl implements MobicentsEventTypeDescriptor, MobicentsEventTypeDescriptorInternal {

	private String eventClassName = null;
    private DeployableUnitID deployableUnit = null;
    private String source = null;
    private ComponentID componentID = null;
    private ComponentKey componentKey = null;

    public MobicentsEventTypeDescriptorInternalImpl() {
    }

    /**
     * Sets the name of the interface or class used in the event type.
     *
     * @param eventClassName the name of the interface or class used in the
     * event type.
     */
    public void setEventClassName(String eventClassName) {
        this.eventClassName = eventClassName;
    }

    /**
     * Get the name of the interface or class used in the event type.
     *
     * @return the name of the interface or class used in the event type.
     */
    public String getEventClassName() {
        return eventClassName;
    }

    /**
     * Sets the identifier of the deployable unit from which this component was
     * installed.
     *
     * @param deployableUnit the identifier of the deployable unit from which
     * this component was installed.
     */
    public void setDeployableUnit(DeployableUnitID deployableUnit) {
        this.deployableUnit = deployableUnit;
    }

    /**
     * Get the identifier of the deployable unit from which this component was
     * installed.
     *
     * @return the identifier of the deployable unit from which this component
     *   was installed.
     */
    public DeployableUnitID getDeployableUnit() {
        return deployableUnit;
    }

    /**
     * Sets the name of the source object from which this component was installed.
     *
     * @param source the name of the source object from where this component was
     *   installed.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Get the name of the source object from which this component was installed.
     *
     * @return the name of the source object from where this component was
     *   installed.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the component identifier for this descriptor.
     *
     * @param componentID the component identifier for this descriptor.
     */
    public void setID(ComponentID componentID) {
        this.componentID = componentID;
    }

    /**
     * Get the component identifier for this descriptor.
     *
     * @return the component identifier for this descriptor.
     */
    public ComponentID getID() {
        return componentID;
    }

    /**
     * Sets the component key of this component.
     *
     * @param componentKey  the key of the component.
     */
    public void setComponentKey(ComponentKey componentKey) {
        this.componentKey = componentKey;
    }

    /**
     * Get the name of the component.
     *
     * @return the name of the component.
     */
    public String getName() {
        return componentKey.getName();
    }

    /**
     * Get the vendor of the component.
     *
     * @return the vendor of the component.
     */
    public String getVendor() {
        return componentKey.getVendor();
    }

    /**
     * Get the version of the component.
     *
     * @return the version of the component.
     */
    public String getVersion() {
        return componentKey.getVersion();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#checkDeployment()
     */
    public void checkDeployment() throws DeploymentException {
        // TODO Auto-generated method stub
        
    }

	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}


}
