package org.mobicents.slee.runtime.sbbentity;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.transaction.TransactionContext;

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
public class SbbEntityFactoryImpl extends AbstractSleeContainerModule implements SbbEntityFactory {

	private static final Logger logger = Logger
			.getLogger(SbbEntityFactoryImpl.class);

	private static final boolean doTraceLogs = logger.isTraceEnabled();
	
	protected SbbEntityLockFacility lockFacility;

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		this.lockFacility = new SbbEntityLockFacility(sleeContainer);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntityFactory#createSbbEntity(javax.slee.SbbID, javax.slee.ServiceID, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public SbbEntityImpl createSbbEntity(SbbID sbbId, ServiceID svcId,
			String parentSbbEntityId, String parentChildRelation,
			String rootSbbEntityId, String convergenceName) {
		
		final String sbbeId = sleeContainer.getUuidGenerator().createUUID();
		
		return _createSbbEntity(sbbeId, sbbId, svcId, parentSbbEntityId, parentChildRelation, rootSbbEntityId, convergenceName);
	}

	public SbbEntityImpl _createSbbEntity(String sbbeId, SbbID sbbId, ServiceID svcId,
			String parentSbbEntityId, String parentChildRelation,
			String rootSbbEntityId, String convergenceName) {
		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();

		// get lock
		final ReentrantLock lock = lockFacility.get(sbbeId);
		lockOrFail(lock,sbbeId);
		// we hold the lock now
				
		// create sbb entity
		final SbbEntityImmutableData sbbEntityImmutableData = new SbbEntityImmutableData(sbbId, svcId, parentSbbEntityId, parentChildRelation, rootSbbEntityId, convergenceName);
		final SbbEntityCacheData cacheData = new SbbEntityCacheData(sbbeId,sleeContainer.getCluster().getMobicentsCache());
		cacheData.create();
		cacheData.setSbbEntityImmutableData(sbbEntityImmutableData);
		
		final SbbEntityImpl sbbEntity = new SbbEntityImpl(sbbeId, sbbEntityImmutableData, cacheData, this);

		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, txContext);

		// add tx actions to unlock it when the tx ends
		SbbEntityUnlockTransactionalAction rollbackAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,true,lockFacility);
		SbbEntityUnlockTransactionalAction commitAction = new SbbEntityUnlockTransactionalAction(sbbEntity,lock,true,false,lockFacility);
		txContext.getAfterRollbackActions().add(rollbackAction);
		txContext.getAfterCommitActions().add(commitAction);
		
		return sbbEntity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntityFactory#createRootSbbEntity(javax.slee.SbbID, javax.slee.ServiceID, java.lang.String)
	 */
	public SbbEntityImpl createRootSbbEntity(SbbID sbbId, ServiceID svcId,
			String convergenceName) {
		
		final String sbbeId = new StringBuilder().append(svcId.hashCode()).append(convergenceName).toString();
		
		return _createSbbEntity(sbbeId, sbbId, svcId, null, null, sbbeId, convergenceName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntityFactory#getSbbEntity(java.lang.String, boolean)
	 */
	public SbbEntityImpl getSbbEntity(String sbbeId,boolean useLock) {

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
		SbbEntityImpl sbbEntity = getSbbEntityFromTx(sbbeId, txContext);
		
		if (sbbEntity == null) {
			
			if (doTraceLogs)
				logger.trace("Loading sbb entity " + sbbeId + " from cache");
			
			if (useLock) {				
				final ReentrantLock lock = lockFacility.get(sbbeId);
				lockOrFail(lock,sbbeId);
				txContext.getAfterRollbackActions().add(new SbbEntityUnlockTransactionalAction(sbbEntity,lock,false,true,lockFacility));
				txContext.getAfterCommitActions().add(new SbbEntityUnlockTransactionalAction(sbbEntity,lock,false,false,lockFacility));									
			}
			
			// not found in tx, get from cache
			final SbbEntityCacheData cacheData = new SbbEntityCacheData(sbbeId,sleeContainer.getCluster().getMobicentsCache());
			if (!cacheData.exists()) {
				lockFacility.remove(sbbeId);
				return null;
			}
			
			sbbEntity = new SbbEntityImpl(sbbeId,cacheData,this);				
												
			// store it in the tx, we need to do it due to sbb local object and
			// current storing in sbb entity per tx
			storeSbbEntityInTx(sbbEntity, txContext);
		}
		return sbbEntity;
	}


	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntityFactory#removeSbbEntity(org.mobicents.slee.runtime.sbbentity.SbbEntityImpl, boolean, boolean)
	 */
	public void removeSbbEntity(SbbEntity sbbEntity,
			boolean removeFromParent, boolean useCurrentClassLoader) throws TransactionRequiredException, SystemException {
	
		if (useCurrentClassLoader) {
			removeSbbEntityWithCurrentClassLoader(sbbEntity, removeFromParent);
		}
		else {
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
	}

	/**
	 * Removes the specified sbb entity but without changing to sbb's class
	 * loader first.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param removeFromParent
	 *            indicates if the entity should be remove from it's parent also
	 * @throws SystemException 
	 * @throws TransactionRequiredException 
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	private void removeSbbEntityWithCurrentClassLoader(
			SbbEntity sbbEntity, boolean removeFromParent) throws TransactionRequiredException, SystemException {
		
		// remove entity
		sbbEntity.remove(removeFromParent);
		// remove from tx data
		sleeContainer.getTransactionManager().getTransactionContext().getData().remove(
				sbbEntity.getSbbEntityId());			
	}

	// --- helpers

	/**
	 * 
	 * @param sbbEntity
	 * @param txManager
	 */
	@SuppressWarnings("unchecked")
	private static void storeSbbEntityInTx(SbbEntityImpl sbbEntity,
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
	private static SbbEntityImpl getSbbEntityFromTx(String sbbeId,
			TransactionContext txContext) {
		return txContext != null ? (SbbEntityImpl) txContext.getData().get(
					sbbeId) : null;		
	}
	
	/**
	 * 
	 * @param lock
	 * @param sbbeId
	 * @throws SLEEException
	 */
	private static void lockOrFail(ReentrantLock lock, String sbbeId) throws SLEEException {
		if (doTraceLogs) {
			logger.trace(Thread.currentThread()+" trying to acquire lock "+lock+" for sbb entity with id "+sbbeId);
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
		if (doTraceLogs) {
			logger.trace(Thread.currentThread()+" acquired lock "+lock+" for sbb entity with id "+sbbeId);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getSbbEntityIDs() {
		final SbbEntityFactoryCacheData cacheData = new SbbEntityFactoryCacheData(sleeContainer.getCluster());
		if (cacheData.exists()) {
			return cacheData.getSbbEntities();
		}
		else {
			return Collections.emptySet();
		}
	}
}