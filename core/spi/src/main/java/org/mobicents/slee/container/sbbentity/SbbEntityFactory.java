/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

import javax.slee.ServiceID;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public interface SbbEntityFactory extends SleeContainerModule {

	/**
	 * Creates a new root sbb entity.
	 * 
	 * @param serviceID
	 * @param convergenceName
	 * @return
	 */
	public SbbEntity createRootSbbEntity(ServiceID serviceID,
			String convergenceName);

	/**
	 * Creates a new non root sbb entity, if it does not exists. 
	 * 
	 * @param parentSbbEntityID
	 * @param parentChildRelation
	 * @return
	 */
	public SbbEntity createNonRootSbbEntity(SbbEntityID parentSbbEntityID,
			String parentChildRelation);

	/**
	 * Retrieves the sbb entity with the specified id.
	 * 
	 * @param sbbEntityID
	 * @param lock
	 *            indicates if the sbb entity should be locked or not, to ensure
	 *            serial access.
	 * @return
	 */
	public SbbEntity getSbbEntity(SbbEntityID sbbEntityID, boolean lock);

	/**
	 * 
	 * @param serviceID
	 * @return
	 */
	public Set<SbbEntityID> getRootSbbEntityIDs(ServiceID serviceID);
	
	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntityIDs();

	/**
	 * Removes the specified sbb entity.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param useCurrentClassLoader
	 *            if false the factory will switch to the sbb's class loader
	 *            before removing the entity.
	 */
	public void removeSbbEntity(SbbEntity sbbEntity,
			boolean useCurrentClassLoader);

}
