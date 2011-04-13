package org.mobicents.slee;

import javax.slee.NoSuchObjectLocalException;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;

/**
 * Extension for {@link SbbLocalObject}. Exposes more information about the
 * object.
 * 
 * @author martins
 * 
 */
public interface SbbLocalObjectExt extends SbbLocalObject {

	/**
	 * Retrieves the name of the child relation used to create this object.
	 * 
	 * @return null if this object is related with a root Sbb entity.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's child relation name could not be obtained due to
	 *             a system-level failure.
	 */
	public String getChildRelation() throws TransactionRequiredLocalException,
			SLEEException;

	/**
	 * Retrieves the name of this object.
	 * 
	 * @return
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's name could not be obtained due to a system-level
	 *             failure.
	 */
	public String getName() throws NoSuchObjectLocalException,
			TransactionRequiredLocalException, SLEEException;

	/**
	 * Retrieves the parent Sbb object.
	 * 
	 * @return null if this object is related with a root Sbb entity.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's parent could not be obtained due to a
	 *             system-level failure.
	 */
	public SbbLocalObjectExt getParent() throws NoSuchObjectLocalException,
			TransactionRequiredLocalException, SLEEException;

	/**
	 * Retrieves the ID of the service related with this object.
	 * 
	 * @return
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's service ID could not be obtained due to a
	 *             system-level failure.
	 */
	public ServiceID getServiceID() throws NoSuchObjectLocalException,
			TransactionRequiredLocalException, SLEEException;
}
