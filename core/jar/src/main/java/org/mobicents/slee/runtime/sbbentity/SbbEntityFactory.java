package org.mobicents.slee.runtime.sbbentity;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.TransactionContext;

/**
 * 
 * SbbEntityFactory -- implements a map from SBB Entity Id to SBB Entity
 * instances. This is the sole place where Sbb Entities are created and
 * destroyed. We keep a map of SBB Entity ID to SBB Entity here and return Sbb
 * Entities that are stored in this map.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Tim Fox ( streamlining )
 * @author eduardomartins
 */
public class SbbEntityFactory {

	private static final Logger logger = Logger
			.getLogger(SbbEntityFactory.class);

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	protected static final SbbEntityLockFacility lockFacility = new SbbEntityLockFacility(sleeContainer);

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
	public static SbbEntity createSbbEntity(SbbID sbbId, ServiceID svcId,
			String parentSbbEntityId, String parentChildRelation,
			String rootSbbEntityId, String convergenceName) {
		
		final String sbbeId = sleeContainer.getUuidGenerator().createUUID();
		
		// no lock needed, this is a non root sbb entity creation, which is done only by holding parent root sbb entity lock
		
		// create sbb entity
		final SbbEntityImmutableData sbbEntityImmutableData = new SbbEntityImmutableData(sbbId, svcId, parentSbbEntityId, parentChildRelation, rootSbbEntityId, convergenceName);
		final SbbEntity sbbEntity = new SbbEntity(sbbeId, sbbEntityImmutableData);

		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, sleeContainer.getTransactionManager().getTransactionContext());

		return sbbEntity;		
	}

	/**
	 * Creates a new root sbb entity
	 * 
	 * @param sbbId
	 * @param svcId
	 * @param convergenceName
	 * @return
	 */
	public static SbbEntity createRootSbbEntity(SbbID sbbId, ServiceID svcId,
			String convergenceName) {
		
		final String sbbeId = new StringBuilder().append(svcId.hashCode()).append(convergenceName).toString();
		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();

		// get lock
		final ReentrantLock lock = lockFacility.get(sbbeId);
		lockOrFail(lock,sbbeId);
		// we hold the lock now
				
		// create sbb entity
		final SbbEntityImmutableData sbbEntityImmutableData = new SbbEntityImmutableData(sbbId, svcId, null, null, sbbeId, convergenceName);
		final SbbEntity sbbEntity = new SbbEntity(sbbeId, sbbEntityImmutableData);

		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, txContext);

		// add tx actions to unlock it when the tx ends
		SbbEntityUnlockTransactionalAction rollbackAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,true);
		SbbEntityUnlockTransactionalAction commitAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,false);
		txContext.getAfterRollbackActions().add(rollbackAction);
		txContext.getAfterCommitActions().add(commitAction);
		
		return sbbEntity;
	}

	/**
	 * 
	 * @param sbbeId
	 * @return
	 */
	public static SbbEntity getSbbEntityWithoutLock(String sbbeId) {
		return _getSbbEntity(sbbeId, false);
	}
	
	/**
	 * 
	 * @param sbbeId
	 * @return
	 */
	public static SbbEntity getSbbEntity(String sbbeId) {
		return _getSbbEntity(sbbeId, true);
	}
	
	/**
	 * Call this function when you want to get an instantiated SbbEntity from
	 * the cache.
	 * 
	 * @param sbbeId
	 * @return
	 */
	private static SbbEntity _getSbbEntity(String sbbeId,boolean useLock) {

		if (sbbeId == null)
			throw new NullPointerException("Null Sbbeid");

		/*
		 * NB!! We must not use a map to cache sbb entities. There can be
		 * multiple active transactions each of which accesses the same sbb
		 * entity at any one time in the SLEE. Therefore, by using a map to
		 * store the sbb entities one tx can see the transactional state of the
		 * other tx before it has committed. I.e. we would have no transaction
		 * isolation. Instead, if we want to cache the sbb entity for the
		 * lifetime of the tx for performance reasons, we need to store in a
		 * *per transaction* cache
		 */

		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		
		// Look for it in the per transaction cache
		SbbEntity sbbEntity = getSbbEntityFromTx(sbbeId, txContext);
		
		if (sbbEntity == null) {
			
			if (logger.isDebugEnabled())
				logger.debug("Loading sbb entity " + sbbeId + " from cache");
			
			// not found in tx, get from cache
			sbbEntity = new SbbEntity(sbbeId);
			
			if (useLock) {				
				if (!sbbEntity.isRootSbbEntity()) {
					// locks the root sbb entity
					_getSbbEntity(sbbEntity.getRootSbbId(),true);
				}				
				else {
					final ReentrantLock lock = lockFacility.get(sbbeId);
					if (lock != null) {
						lockOrFail(lock,sbbeId);
						txContext.getAfterRollbackActions().add(new SbbEntityUnlockTransactionalAction(sbbEntity,lock,false,true));
						txContext.getAfterCommitActions().add(new SbbEntityUnlockTransactionalAction(sbbEntity,lock,false,false));					
					}
				}
			}
									
			// store it in the tx, we need to do it due to sbb local object and
			// current storing in sbb entity per tx
			storeSbbEntityInTx(sbbEntity, txContext);
		}
		return sbbEntity;
	}

	

	/**
	 * Removes the specified sbb entity. The sbb class loader is used on this
	 * operation.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param removeFromParent
	 *            indicates if the entity should be remove from it's parent also
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public static void removeSbbEntity(SbbEntity sbbEntity,
			boolean removeFromParent) throws TransactionRequiredException,
			SystemException {

		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					sbbEntity.getSbbComponent().getClassLoader());
			removeSbbEntityWithCurrentClassLoader(sbbEntity, removeFromParent);
		} finally {
			// restore old class loader
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	/**
	 * Removes the specified sbb entity but without changing to sbb's class
	 * loader first.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param removeFromParent
	 *            indicates if the entity should be remove from it's parent also
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public static void removeSbbEntityWithCurrentClassLoader(
			SbbEntity sbbEntity, boolean removeFromParent)
			throws TransactionRequiredException, SystemException {
		
		// remove entity
		sbbEntity.remove(removeFromParent);
		// remove from tx data
		sleeContainer.getTransactionManager().getTransactionContext().getData().remove(
				sbbEntity.getSbbEntityId());			
	}

	/**
	 * Removes the specified sbb entity.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param removeFromParent
	 *            indicates if the entity should be remove from it's parent also
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public static void removeSbbEntity(String sbbEntityId,
			boolean removeFromParent) throws TransactionRequiredException,
			SystemException {
		removeSbbEntity(getSbbEntity(sbbEntityId), removeFromParent);
	}

	// --- helpers

	/**
	 * 
	 * @param sbbEntity
	 * @param txManager
	 */
	@SuppressWarnings("unchecked")
	private static void storeSbbEntityInTx(SbbEntity sbbEntity,
			TransactionContext txContext) {
		if (txContext != null)
			txContext.getData().put(
					sbbEntity.getSbbEntityId(), sbbEntity);		
	}

	/**
	 * 
	 * @param sbbeId
	 * @param txManager
	 * @return
	 */
	private static SbbEntity getSbbEntityFromTx(String sbbeId,
			TransactionContext txContext) {
		return txContext != null ? (SbbEntity) txContext.getData().get(
					sbbeId) : null;		
	}
	
	/**
	 * 
	 * @param lock
	 * @param sbbeId
	 * @throws SLEEException
	 */
	private static void lockOrFail(ReentrantLock lock, String sbbeId) throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug(Thread.currentThread()+" trying to acquire lock "+lock+" for sbb entity with id "+sbbeId);
		}
		boolean locked;
		try { 
			locked = lock.tryLock(10, TimeUnit.SECONDS);
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		if (!locked) {
			throw new SLEEException("timeout while acquiring lock "+lock+" for sbb entity with id "+sbbeId);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(Thread.currentThread()+" acquired lock "+lock+" for sbb entity with id "+sbbeId);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Set<String> getSbbEntityIDs() {
		final SbbEntityFactoryCacheData cacheData = new SbbEntityFactoryCacheData(sleeContainer.getCluster());
		if (cacheData.exists()) {
			return cacheData.getSbbEntities();
		}
		else {
			return null;
		}
	}
}