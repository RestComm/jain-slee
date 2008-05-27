package org.mobicents.slee.container;

import java.util.Map;
import java.util.Set;

import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

public class DeploymentCacheManager {

	//  A set of deployable unit ids mapped to deployable Unit descriptors.
    private CacheableMap deployableUnitIDtoDescriptorMap;

    // A set of URLs mapped to deployable Unit IDs;
    private CacheableMap urlToDeployableUnitIDMap;

    //A table of referring components.
    // This maps a component to all the components that refer to it.

    private CacheableMap referringComponents;

    // A table of service Deployments -- maps a service ID to all the
    // Deployable Units that reference it.

    private CacheableMap componentIDToDeployableUnitIDMap;

    //      A hash map that maps sbb component ids to sbb component descriptors.
    private CacheableMap sbbComponents;

    // A hash map that maps profile component ids to profile component
    // descriptors.
    private CacheableMap profileComponents;

    //for serviceInfo the key is serviceID and the lookup return the
    // ServiceComponent
    private CacheableMap serviceComponents;

    private CacheableMap serviceResourceAdaptorEntities;
    
    private CacheableSet activeServiceIDs;

    private static final String DU_ID_TO_DESCRIPTOR_MAP = "deployableUnitIDtoDescriptorMap";

    private static final String URL_TO_DEPLOYABLE_UNIT_ID_MAP = "urlToDeployableUnitIDMap";

    private static final String COMPONENT_ID_TO_DEPLOYABLE_UNIT_ID_MAP = "componentIDToDeployableUnitIDMap";

    private static final String REFERRING_COMPONENTS = "referringComponents";

    private static final String SBB_COMPONENTS = "sbbComponents";

    private static final String PROFILE_COMPONENTS = "profileComponents";

    private static final String SERVICE_COMPONENTS = "serviceComponents";
    
    private static final String SERVICE_RESOURCE_ADAPTOR_ENTITIES = "serviceResourceAdaptorEntities";
    
    private static final String ACTIVE_SERVICE_IDS = "activeServiceIDs";

	private Logger logger = Logger.getLogger(DeploymentCacheManager.class.getName());

    public void printNode() {
        if (logger .isDebugEnabled()) {
            logger.debug("DeploymentCache { ");
            logger.debug("deployableUnitIDToDescriptorMap = "
                    + deployableUnitIDtoDescriptorMap);
            logger.debug("urlToDeployableUnitIDMap = "
                    + urlToDeployableUnitIDMap);
            logger.debug("referringComponents = " + referringComponents);
            logger.debug("componentIDToDeployableUnitIDMap = "
                    + componentIDToDeployableUnitIDMap);
            logger.debug("sbbComponents = " + sbbComponents);
            logger.debug("profileComponents = " + profileComponents);
            logger.debug("serviceComponents = " + serviceComponents);
            logger.debug("serviceResourceAdaptorEntities = " + serviceResourceAdaptorEntities);
            logger.debug("activeServiceIDs = " + activeServiceIDs);
            logger.debug("}");

        }
    }

    public DeploymentCacheManager() {
    	this.serviceComponents = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE + ":" + SERVICE_COMPONENTS);
        this.serviceResourceAdaptorEntities = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + SERVICE_RESOURCE_ADAPTOR_ENTITIES);
        this.referringComponents = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + REFERRING_COMPONENTS);
        this.profileComponents = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + PROFILE_COMPONENTS);
        // Get the various MBeans that should be initialized by now.
        this.componentIDToDeployableUnitIDMap = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + COMPONENT_ID_TO_DEPLOYABLE_UNIT_ID_MAP);
        this.sbbComponents = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + SBB_COMPONENTS);
        this.urlToDeployableUnitIDMap = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + URL_TO_DEPLOYABLE_UNIT_ID_MAP);
        this.deployableUnitIDtoDescriptorMap = new CacheableMap(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + DU_ID_TO_DESCRIPTOR_MAP);
        this.activeServiceIDs = new CacheableSet(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + ACTIVE_SERVICE_IDS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.runtime.Cacheable#getNodeName()
     */
    public String getNodeName() {
        return "deployment";
    }

    public Map getServiceComponents() {
        return serviceComponents;

    }
    
    public Map getServiceResourceAdaptorEntities() {
        return serviceResourceAdaptorEntities;

    }
    
    public Set getActiveServiceIDs() {
        return activeServiceIDs;

    }

    public Map getReferringComponents() {
        return referringComponents;
    }

    public Map getProfileComponents() {
        return profileComponents;
    }

    public Map getComponentIDToDeployableUnitIDMap() {
        return componentIDToDeployableUnitIDMap;
    }

    public Map getUrlToDeployableUnitIDMap() {
        return urlToDeployableUnitIDMap;
    }

    public Map getSbbComponents() {
        return sbbComponents;
    }

    public Map getDeployableUnitIDtoDescriptorMap() {
        return deployableUnitIDtoDescriptorMap;
    }

	public Set newReferringDuSet() {
    	Set refs = new CacheableSet(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + COMPONENT_ID_TO_DEPLOYABLE_UNIT_ID_MAP + ":" +
    			"newReferringDuSet" + new java.rmi.server.UID());
    	return refs;
	}
    
	public Set newReferringCompSet() {
    	Set refs = new CacheableSet(TransactionManagerImpl.DEPLOYMENT_CACHE  + ":" + COMPONENT_ID_TO_DEPLOYABLE_UNIT_ID_MAP + ":" +
    			"newReferringCompSet" + new java.rmi.server.UID());
    	return refs;
	}

}
