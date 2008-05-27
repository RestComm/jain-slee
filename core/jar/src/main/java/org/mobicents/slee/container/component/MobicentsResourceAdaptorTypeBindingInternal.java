package org.mobicents.slee.container.component;

import java.util.HashSet;

import javax.slee.resource.ResourceAdaptorTypeID;

public interface MobicentsResourceAdaptorTypeBindingInternal {

	
	/**
     * @param activityContextInterfaceFactoryName The activityContextInterfaceFactoryName to set.
     */
    public void setActivityContextInterfaceFactoryName(
            String activityContextInterfaceFactoryName) ;
    
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) ;
    
    /**
     * @param resourceAdapterEntityBindings The resourceAdapterEntityBindings to set.
     */
    public void setResourceAdapterEntityBindings(
            HashSet resourceAdapterEntityBindings) ;
    
    
    /**
     * @param resourceAdapterTypeId The resourceAdapterTypeId to set.
     */
    public void setResourceAdapterTypeId(
            ResourceAdaptorTypeID resourceAdapterTypeId) ;
	
}
