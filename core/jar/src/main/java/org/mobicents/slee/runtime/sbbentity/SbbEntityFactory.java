/*
 * Created on Feb 3, 2005
 * 
 * The Mobicents Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime.sbbentity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.MobicentsUUIDGenerator;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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

	/**
	 * 
	 * @return
	 */
	private static String genId() {
		return MobicentsUUIDGenerator.getInstance().createUUID();
	}

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	protected static final SbbEntityLockFacility lockFacility = new SbbEntityLockFacility();

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

		try {
			final String sbbeId = genId();
			return _createSbbEntity(sbbeId, sbbId, svcId, parentSbbEntityId,
					parentChildRelation, rootSbbEntityId, convergenceName);
		} catch (Throwable ex) {
			throw new SLEEException(
					"Exception in creating non root sbb entity!", ex);
		}
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

		try {
			final String sbbeId = genId();
			return _createSbbEntity(sbbeId, sbbId, svcId, null, null, sbbeId,
					convergenceName);
		} catch (Throwable ex) {
			throw new SLEEException("Exception in creating root sbb entity!",
					ex);
		}
	}

	/**
	 * 
	 * @param sbbeId
	 * @param sbbId
	 * @param svcId
	 * @param parentSbbEntityId
	 * @param parentChildRelation
	 * @param rootSbbEntityId
	 * @param convergenceName
	 * @return
	 * @throws Exception
	 */
	private static SbbEntity _createSbbEntity(final String sbbeId, SbbID sbbId,
			ServiceID svcId, String parentSbbEntityId,
			String parentChildRelation, String rootSbbEntityId,
			String convergenceName) throws Exception {
		
		final SleeTransactionManager txManager = sleeContainer
				.getTransactionManager();

		// create lock
		ReentrantLock lock = new ReentrantLock();
		lock.lock();

		// put lock
		final ReentrantLock anotherLock = lockFacility.putIfAbsent(sbbeId,
				lock);
		if (anotherLock != null) {
			// concurrent sbb entity creation
			// switch lock
			lock.unlock();
			lock = anotherLock;
			lockOrFail(lock,sbbeId);
			// we hold the lock now
			try {
				return getSbbEntity(sbbeId);
			} catch (IllegalStateException e) {
				// it seems the concurrent sbb entity creation rolledback
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(),e);
				}				
			}
		};

		// create sbb entity
		final SbbEntityImmutableData sbbEntityImmutableData = new SbbEntityImmutableData(sbbId, svcId, parentSbbEntityId, parentChildRelation, rootSbbEntityId, convergenceName);
		final SbbEntity sbbEntity = new SbbEntity(sbbeId, sbbEntityImmutableData);

		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, txManager);

		// add tx actions to unlock it when the tx ends
		try {
			SbbEntityUnlockTransactionalAction rollbackAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,true);
			SbbEntityUnlockTransactionalAction commitAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,false);
			txManager.addAfterRollbackAction(rollbackAction);
			txManager.addAfterCommitAction(commitAction);
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}

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

		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		
		// Look for it in the per transaction cache
		SbbEntity sbbEntity = getSbbEntityFromTx(sbbeId, txMgr);
		
		if (sbbEntity == null) {
			// not found in tx
			SbbEntityUnlockTransactionalAction rollbackAction = null;
			SbbEntityUnlockTransactionalAction commitAction = null;
			
			if (useLock) {
				rollbackAction = new SbbEntityUnlockTransactionalAction(false,true);
				commitAction = new SbbEntityUnlockTransactionalAction(false,false);
				
				final ReentrantLock lock = lockFacility.get(sbbeId);
				if (lock != null) {
					lockOrFail(lock,sbbeId);
					rollbackAction.setReentrantLock(lock);
					commitAction.setReentrantLock(lock);
					try {
						txMgr.addAfterRollbackAction(rollbackAction);
						txMgr.addAfterCommitAction(commitAction);
					} catch (SystemException e) {
						throw new SLEEException(e.getMessage(), e);
					}
				}
			}
			
			if (logger.isDebugEnabled())
				logger.debug("Loading sbb entity " + sbbeId + " from cache");
			
			sbbEntity = new SbbEntity(sbbeId);
			
			if (useLock) {
				rollbackAction.setSbbEntity(sbbEntity);
				commitAction.setSbbEntity(sbbEntity);
			}
									
			// store it in the tx, we need to do it due to sbb local object and
			// current storing in sbb entity per tx
			storeSbbEntityInTx(sbbEntity, txMgr);
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
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	private static void storeSbbEntityInTx(SbbEntity sbbEntity,
			SleeTransactionManager txManager) {
		try {
			txManager.getTransactionContext().getData().put(
					sbbEntity.getSbbEntityId(), sbbEntity);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param sbbeId
	 * @param txManager
	 * @return
	 * @throws SystemException
	 */
	private static SbbEntity getSbbEntityFromTx(String sbbeId,
			SleeTransactionManager txManager) {
		try {
			return (SbbEntity) txManager.getTransactionContext().getData().get(
					sbbeId);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(), e);
		}
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
}