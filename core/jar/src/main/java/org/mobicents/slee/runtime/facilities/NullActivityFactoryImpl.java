/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities;

import java.util.Iterator;
import java.util.Set;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityFactory;


import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

/**
 * Implementation of the null activity factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * 
 */
public class NullActivityFactoryImpl implements NullActivityFactory {

	private SleeContainer sleeContainer;

	private static Logger logger = Logger.getLogger(NullActivityFactoryImpl.class);

	private static String setNodeName = "nullactivities";

	private static String NULL_ACTIVITY_CACHE = TransactionManagerImpl.RUNTIME_CACHE;

	private Set nullActivitiesActivityContextIds; // This is used to recreate the
	// factory.

	public NullActivityFactoryImpl(SleeContainer serviceContainer)
			throws Exception {
		this.sleeContainer = serviceContainer;
		nullActivitiesActivityContextIds = new CacheableSet(NULL_ACTIVITY_CACHE + "-" + setNodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.nullactivity.NullActivityFactory#createNullActivity()
	 */
	public NullActivity createNullActivity()
			throws TransactionRequiredLocalException, FactoryException {
		return createNullActivityImpl(true);
	}

	/*
	 * This version of the createNullActivity() method does not require a
	 * transaction to be in progress. It is used for null activities created via
	 * the JCA adaptor. It should never be called by SBB or other code which is
	 * in a transaction.
	 */
	public NullActivity createNullActivityNoTx() throws FactoryException {
		try {
			return createNullActivityImpl(false);		
		}
		catch (TransactionRequiredLocalException e) {
			throw new FactoryException("Failed to create null activity without tx. Exception msg: "+e.getMessage());
		}
	}

	/**
	 * Creates a new instance of NullActivityImpl, binding it to a ActivityContext.
	 * @param mandateTransaction specifies if the method should require a transaction.
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws FactoryException
	 */
	public NullActivityImpl createNullActivityImpl(boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException {
		return createNullActivityImpl(null,mandateTransaction);
	}
	
	public NullActivityImpl createNullActivityImpl(String activityContextId,boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException {
		// check mandated by SLEE TCK test CreateActivityWhileStoppingTest
		if (!sleeContainer.getSleeState().equals(SleeState.RUNNING)) {
			return null;
		}

		if (mandateTransaction) {
			sleeContainer.getTransactionManager().mandateTransaction();
		}

		// create activity
		NullActivityImpl nullActivity = new NullActivityImpl();
		// get an activity context for it
		ActivityContextFactoryImpl acf = (ActivityContextFactoryImpl) sleeContainer
				.getActivityContextFactory();
		ActivityContext activityContext = null;
		if (activityContextId == null) {
			activityContext = acf.getActivityContext(nullActivity);
		}
		else {
			activityContext = acf.createActivityContext(nullActivity, activityContextId);
		}
		if (logger.isDebugEnabled()) {
			logger
					.debug("NullActivityFactory.createNullActivity() Creating null activity "
							+ nullActivity
							+ " received ac "
							+ activityContext.getActivityContextId());
		}
		nullActivitiesActivityContextIds.add(activityContext.getActivityContextId());
		return nullActivity;

	}

	public void removeNullActivity(String acid) {
		nullActivitiesActivityContextIds.remove(acid);
	}

	public void restart() throws Exception {
		SleeContainer.getTransactionManager().mandateTransaction();
		if (logger.isDebugEnabled())
			logger.debug("NullActivityFactory.restart()");
		// Restore the cached image
		ActivityContextFactoryImpl acf = (ActivityContextFactoryImpl) SleeContainer
				.lookupFromJndi().getActivityContextFactory();
		for (Iterator it = nullActivitiesActivityContextIds.iterator(); it.hasNext();) {
			String acid = (String) it.next();
			if (logger.isDebugEnabled())
				logger
					.debug("NullActivityFactory.restart(): restoring null activity "
							+ acid);
			NullActivityImpl nullActivity = new NullActivityImpl();
			acf.createActivityContext(nullActivity, acid);
		}
	}
	
	@Override
	public String toString() {
		return 	"Null Activity Factory: " +
				"\n+-- Null Activities: " + nullActivitiesActivityContextIds.size();
	}

}
