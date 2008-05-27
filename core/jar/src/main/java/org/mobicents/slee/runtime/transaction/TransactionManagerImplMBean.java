/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on Feb 9, 2005                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import javax.management.ObjectName;


/**
 * @author Ivelin Ivanov
 *
 */
public interface TransactionManagerImplMBean extends SleeTransactionManager, org.jboss.system.ServiceMBean {
    
    /**
     * Set the runtime treecache instance to be used by the SLEE
     * @param newTreeCacheName
     */
	public void setRuntimeTreeCacheName(ObjectName newTreeCacheName);
    /**
     * Set the peristent treecache instance to to be used by the SLEE
     * 
     * @param newTreeCacheName
     */
    public void setDeploymentTreeCacheName(ObjectName newTreeCacheName);
    
    /**
     * Set the profile treecache instance to be used by the slee
     * @param newTreeCacheName
     */
    public void setProfileTreeCacheName(ObjectName newTreeCacheName);
    
    /**
     * 
     * Set the TreeCache instance to be used by the SLEE
     * 
     * @param newTreeCache
     */
    public void setTreeCacheName(ObjectName newTreeCacheName);
        
    /**
     * 
     * Get the default TreeCache instance used by the SLEE
     * 
     * @param newTreeCache
     */
    public ObjectName getTreeCacheName();
    
}