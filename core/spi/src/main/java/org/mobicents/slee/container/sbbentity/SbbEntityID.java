package org.mobicents.slee.container.sbbentity;

import java.io.Externalizable;

import javax.slee.ServiceID;

/**
 * 
 * @author martins
 *
 */
public interface SbbEntityID extends Externalizable {

	/**
	 * Retrieves the parent's sbb entity identifier.
	 * @return null if root sbb entity.
	 */
	public SbbEntityID getParentSBBEntityID();
	
	/**
	 * Retrieves the parent's child relation.
	 * @return null if root sbb entity.
	 */
	public String getParentChildRelation();
	
	/**
	 * Retrieves the first element of a sbb entity ID, the identifier of its Service.
	 * @return
	 */
	public ServiceID getServiceID();
	
	/**
	 * Retrieves the second element of a sbb entity ID, the convergence name.
	 * @return
	 */
	public String getServiceConvergenceName();
	
	/**
	 * Indicates if the sbb entity identifier is related to a root sbb entity
	 * @return true if {@link SbbEntityID#getParentSBBEntityID()} is null
	 */
	public boolean isRootSbbEntity();
	
	/**
	 * Retrieves the related root sbb entity identifier.
	 * @return same id if root sbb entity.
	 */
	public SbbEntityID getRootSBBEntityID();
	
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
	@Override
	public String toString();
}
