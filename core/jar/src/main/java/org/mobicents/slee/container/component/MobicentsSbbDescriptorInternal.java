package org.mobicents.slee.container.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

public interface MobicentsSbbDescriptorInternal {

	
	
	/**
     * Set the activity context interface attribute aliases.
     * 
     * @throws Exception
     */
    public void setActivityContextInterfaceAttributeAliases(
            HashMap aciAttributeAliases) ;
    
    public void addResourceAdapterEntityLink(String entityLink) ;
    
    /**
     * Add a resource adapter type.
     * 
     * @param resourceAdapterType --
     *            RA type to add
     */
    public void addResourceAdapterType(
            ResourceAdaptorTypeID resourceAdapterType) ;
    
    /**
     * setAddressProfileSpecAlias Key for the sbb address profile spec.
     * 
     * @throws Exception
     *             if the profile spec is not found.
     */
    public void setAddressProfileSpecAlias(String key) throws Exception ;
    
    public void setEjbRefs(HashSet ejbRefs) ;
    
    public void setEnvironmentEntries(HashSet envEntries) ;
    
    /**
     * set the profile spec references for the sbb.
     * 
     * @param pspecRefs -
     *            a hash map of profile spec references from the SBB parser.
     * 
     * @throws Exception
     */
    public void setProfileSpecReferences(HashMap pspecRefs) ;

    
    /**
     * Re-entrant flag
     * 
     * @param flag --
     *            the re-entrant flag read from the deplpyment descriptor
     */
    public void setReentrant(boolean flag) ;
    
    public void setResourceAdapterTypeBindings(HashSet resourceAdapterTypes) ;
    
    public void setSource(String source) ;
    
    /**
     * set the usage parameters interface
     * 
     * @throws Exception
     */
    public void setUsageParametersInterface(
            String usageParamsInterfaceClassName) throws Exception ;
    
}
