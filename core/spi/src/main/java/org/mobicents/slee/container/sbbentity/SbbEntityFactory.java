/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 *
 */
public interface SbbEntityFactory extends SleeContainerModule {

	/**
	 * Creates a new root sbb entity
	 * 
	 * @param sbbId
	 * @param svcId
	 * @param convergenceName
	 * @return
	 */
	public SbbEntity createRootSbbEntity(SbbID sbbId, ServiceID svcId,
			String convergenceName);
	
	/**
	 * Creates a new non root sbb entity.
	 * 
	 * @param sbbId
	 * @param svcId
	 * @param parentSbbEntityId
	 * @param parentChildRelation
	 * @param rootSbbEntityId
	 * @param convergenceName
	 * @return
	 */
	public SbbEntity createSbbEntity(SbbID sbbId, ServiceID svcId,
			String parentSbbEntityId, String parentChildRelation,
			String rootSbbEntityId, String convergenceName);
	
	/**
	 * Retrieves the sbb entity with the specified id.
	 * @param sbbEntityId
	 * @param lockRoot indicates if the sbb entity should be locked or not, to ensure serial access.
	 * @return
	 */
	public SbbEntity getSbbEntity(String sbbEntityId, boolean lock);

	/**
	 * 
	 * @return
	 */
	public Set<String> getSbbEntityIDs();
		
	/**
	 * Removes the specified sbb entity.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param removeFromParent
	 *            indicates if the entity should be remove from it's parent also
	 * @param useCurrentClassLoader if false the factory will switch to the sbb's class loader before removing the entity.
	 * @throws SystemException 
	 * @throws TransactionRequiredException 
	 */
	public void removeSbbEntity(SbbEntity sbbEntity,
			boolean removeFromParent, boolean useCurrentClassLoader) throws TransactionRequiredException, SystemException;

}
