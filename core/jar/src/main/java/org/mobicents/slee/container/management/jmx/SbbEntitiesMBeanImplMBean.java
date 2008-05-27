/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

public interface SbbEntitiesMBeanImplMBean
{
   /**
    * Returns an array of details about the SBB Entitiy.
    * 
    * @param sbbeId
    * @return
    */
   public Object[] retrieveSbbEntityInfo(String sbbeId);
   
   /**
	 * Array of the details about all SBB Entities. Retreives the current sbb
	 * entities by iterating over the whole sbb entity tree, of each root sbb
	 * entity, on a active service.
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public Object[] retrieveAllSbbEntities() throws ManagementException;

	/**
	 * Remove SBB Entitiy
	 * 
	 * @param sbbeId
	 */
	public void removeSbbEntity(String sbbeId);

	/**
	 * SBB Entities associated with SBB.
	 * 
	 * @param sbbId
	 * @return
	 */
	public Object[] retrieveSbbEntitiesBySbbId(String sbbId);
}
