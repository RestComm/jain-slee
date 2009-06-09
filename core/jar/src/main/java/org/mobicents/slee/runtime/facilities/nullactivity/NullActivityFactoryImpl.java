/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.cache.NullActivityFactoryCacheData;

/**
 * Implementation of the null activity factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 * 
 */
public class NullActivityFactoryImpl implements NullActivityFactory {

	private SleeContainer sleeContainer;

	private static Logger logger = Logger.getLogger(NullActivityFactoryImpl.class);

	private final NullActivityFactoryCacheData cacheData;
	
	public NullActivityFactoryImpl(SleeContainer serviceContainer)
			throws Exception {
		this.sleeContainer = serviceContainer;
		cacheData = sleeContainer.getCache().getNullActivityFactoryCacheData();
		cacheData.create();
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

	/**
	 * Creates a null activity handle that can later be used to build a null activity.
	 * 
	 * @return
	 */
	public NullActivityHandle createNullActivityHandle() {
		return new NullActivityHandle(sleeContainer.getUuidGenerator().createUUID());
	}
	
	/**
	 * Creates a new instance of NullActivityImpl, binding it to a ActivityContext.
	 * @param mandateTransaction specifies if the method should require a transaction.
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws FactoryException
	 */
	public NullActivityImpl createNullActivityImpl(boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException {
		return createNullActivityImpl(createNullActivityHandle(),mandateTransaction);
	}
	
	public NullActivityImpl createNullActivityImpl(NullActivityHandle nullActivityHandle,boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException {
		// check mandated by SLEE TCK test CreateActivityWhileStoppingTest
		if (sleeContainer.getSleeState() != SleeState.RUNNING) {
			return null;
		}

		if (mandateTransaction) {
			sleeContainer.getTransactionManager().mandateTransaction();
		}

		// create activity
		NullActivityImpl nullActivity = new NullActivityImpl(nullActivityHandle);
		// get an activity context for it
		try {
			sleeContainer.getActivityContextFactory().createActivityContext(ActivityContextHandlerFactory.createNullActivityContextHandle(nullActivityHandle),ActivityFlags.REQUEST_ACTIVITY_UNREFERENCED_CALLBACK);
		} catch (ActivityAlreadyExistsException e) {
			throw new FactoryException(e.getMessage(),e);
		}
		
		if (logger.isDebugEnabled()) {
			logger
					.debug("NullActivityFactory.createNullActivity() Creating null activity "
							+ nullActivity);
		}
		
		// put in cache
		cacheData.addNullActivityId(nullActivityHandle.getId());
		
		return nullActivity;

	}

	public void activityEnded(NullActivityHandle nullActivityHandle) {
		try {
			// remove from cache
			cacheData.removeNullActivityId(nullActivityHandle.getId());
		}
		catch (Exception e) {
			logger.error("failed to remove null activity", e);
		}
	}
	
	public void restart() throws Exception {
		sleeContainer.getTransactionManager().mandateTransaction();
		if (logger.isDebugEnabled())
			logger.debug("NullActivityFactory.restart()");
		// Restore the cached image
		ActivityContextFactoryImpl acf = sleeContainer.getActivityContextFactory();
		for (String id : cacheData.getNullActivityIds()) {
			
			if (logger.isDebugEnabled())
				logger
					.debug("NullActivityFactory.restart(): restoring null activity "
							+ id);
			// recreate handle
			NullActivityHandle nullActivityHandle = new NullActivityHandle(id);
			// get an activity context for it
			acf.createActivityContext(ActivityContextHandlerFactory.createNullActivityContextHandle(nullActivityHandle));
		}
	}
	
	@Override
	public String toString() {
		return 	"Null Activity Factory: " +
				"\n+-- Null Activities: " + cacheData.getNullActivityIds().size();
	}

}
