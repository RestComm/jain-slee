/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 * Implmenetation of profile table activity context interface factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 */
public class ProfileTableActivityContextInterfaceFactoryImpl implements
		ProfileTableActivityContextInterfaceFactory {

	public static String JNDI_NAME = "profiletableactivitycontextinterfacefactory";

	public ProfileTableActivityContextInterfaceFactoryImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileTableActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.profile.ProfileTableActivity)
	 */
	public ActivityContextInterface getActivityContextInterface(
			ProfileTableActivity profileTableActivity)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityException, FactoryException {
		if (profileTableActivity == null
				|| profileTableActivity.getProfileTableName() == null) {

			throw new NullPointerException("null profile table activity");

		}

		SleeContainer serviceContainer = getServiceContainer();
		SleeContainer.getTransactionManager().mandateTransaction();
		try {
			// check if this is an assigned profile table
			// name.
			serviceContainer.getSleeProfileManager().profileTableExists(
					profileTableActivity.getProfileTableName());
		} catch (SystemException e) {
			throw new FactoryException(e.getMessage());
		}

		String acid = serviceContainer.getActivityContextFactory()
				.getActivityContextId(profileTableActivity);
		ActivityContextInterfaceImpl acii = new ActivityContextInterfaceImpl(
				acid);

		return acii;
	}

	public SleeContainer getServiceContainer() {
		return SleeContainer.lookupFromJndi();
	}

}
