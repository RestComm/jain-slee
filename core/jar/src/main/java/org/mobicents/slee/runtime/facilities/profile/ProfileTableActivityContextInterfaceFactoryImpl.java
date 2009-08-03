/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 * Implmenetation of profile table activity context interface factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
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

		SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
		serviceContainer.getTransactionManager().mandateTransaction();
		try {
			// check if this is an assigned profile table
			// name.
			serviceContainer.getSleeProfileTableManager().getProfileTable(profileTableActivity.getProfileTableName());
		} catch (UnrecognizedProfileTableNameException e) {
			throw new FactoryException(e.getMessage());
		}

		ActivityContextHandle ach = ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableActivity.getProfileTableName()));
        ActivityContext ac = serviceContainer.getActivityContextFactory().getActivityContext(ach);
        if (ac == null) {
        	throw new UnrecognizedActivityException(profileTableActivity);
        }
        
		return new ActivityContextInterfaceImpl(ac);

	}
}
