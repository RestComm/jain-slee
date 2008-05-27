package org.mobicents.slee.container.component;

import java.util.HashSet;

import javax.slee.resource.ResourceAdaptorTypeID;

public class MobicentsResourceAdaptorTypeBindingInternalImpl implements
		MobicentsResourceAdaptorTypeBindingInternal, MobicentsResourceAdaptorTypeBinding {

	private String description;
    private ResourceAdaptorTypeID resourceAdapterTypeId;
    private String activityContextInterfaceFactoryName;
    private HashSet ResourceAdapterEntityBindings;
    

    /**
     * @return Returns the activityContextInterfaceFactoryName.
     */
    public String getActivityContextInterfaceFactoryName() {
        return activityContextInterfaceFactoryName;
    }
    /**
     * @param activityContextInterfaceFactoryName The activityContextInterfaceFactoryName to set.
     */
    public void setActivityContextInterfaceFactoryName(
            String activityContextInterfaceFactoryName) {
        this.activityContextInterfaceFactoryName = activityContextInterfaceFactoryName;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the resourceAdapterEntityBindings.
     */
    public HashSet getResourceAdapterEntityBindings() {
        return ResourceAdapterEntityBindings;
    }
    /**
     * @param resourceAdapterEntityBindings The resourceAdapterEntityBindings to set.
     */
    public void setResourceAdapterEntityBindings(
            HashSet resourceAdapterEntityBindings) {
        ResourceAdapterEntityBindings = resourceAdapterEntityBindings;
    }
    /**
     * @return Returns the resourceAdapterTypeId.
     */
    public ResourceAdaptorTypeID getResourceAdapterTypeId() {
        return resourceAdapterTypeId;
    }
    /**
     * @param resourceAdapterTypeId The resourceAdapterTypeId to set.
     */
    public void setResourceAdapterTypeId(
            ResourceAdaptorTypeID resourceAdapterTypeId) {
        this.resourceAdapterTypeId = resourceAdapterTypeId;
    }

}
