/**
 * 
 */
package org.mobicents.slee.container.facilities;

import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.NameNotBoundException;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public interface ActivityContextNamingFacility extends SleeContainerModule,
		javax.slee.facilities.ActivityContextNamingFacility {

	/**
	 * 
	 * @param aciName
	 * @throws NullPointerException
	 * @throws TransactionRequiredLocalException
	 * @throws NameNotBoundException
	 * @throws FacilityException
	 */
	public void removeName(String aciName) throws NullPointerException,
			TransactionRequiredLocalException, NameNotBoundException,
			FacilityException;

}
