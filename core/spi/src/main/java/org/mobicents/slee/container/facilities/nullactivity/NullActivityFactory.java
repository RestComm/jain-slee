/**
 * 
 */
package org.mobicents.slee.container.facilities.nullactivity;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.nullactivity.NullActivity;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 *
 */
public interface NullActivityFactory extends SleeContainerModule, javax.slee.nullactivity.NullActivityFactory {
	
	/**
	 * Creates a null activity handle that can later be used to build a null activity.
	 * 
	 * @return
	 */
	public NullActivityHandle createNullActivityHandle();

	/**
	 * Creates a new null activity for the specified handle.
	 * 
	 * @param nullActivityHandle
	 * @param mandateTransaction
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws FactoryException
	 */
	public NullActivity createNullActivity(NullActivityHandle nullActivityHandle,boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException;

}
