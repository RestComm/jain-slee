package org.mobicents.slee.runtime.sbbentity;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.slee.SLEEException;
import javax.slee.ServiceID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

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
	
	@Override
	public SbbEntity createNonRootSbbEntity(SbbEntityID parentSbbEntityID,String parentChildRelation) {

		// warning: if this childID becomes something else then a uuid then the
		// equals and hashcode of NonRootSbbEntityID must be changed
		final String childID = sleeContainer.getUuidGenerator().createUUID();

		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();

		// get lock
		final ReentrantLock lock = lockFacility.get(parentSbbEntityID.getRootSBBEntityID());
		lockOrFail(lock,parentSbbEntityID.getRootSBBEntityID());
		// we hold the lock now
				
		// create sbb entity
		final NonRootSbbEntityID sbbeId = new NonRootSbbEntityID(parentSbbEntityID, parentChildRelation, childID);
		final SbbEntityCacheData cacheData = new SbbEntityCacheData(sbbeId,sleeContainer.getCluster().getMobicentsCache());
		cacheData.create();
		
		final SbbEntityImpl sbbEntity = new SbbEntityImpl(sbbeId, cacheData, true, this);

		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, txContext);

		// add tx actions to unlock root when the tx ends
		final TransactionalAction txAction = new TransactionalAction() {			
			@Override
			public void execute() {
				lock.unlock();				
			}
		};
		txContext.getAfterRollbackActions().add(txAction);
		txContext.getAfterCommitActions().add(txAction);

		return sbbEntity;

	}
	
	@Override
	public SbbEntity createRootSbbEntity(ServiceID serviceID,
			String convergenceName) {
		
		final RootSbbEntityID sbbeId = new RootSbbEntityID(serviceID, convergenceName);

		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();

		// get lock
		final ReentrantLock lock = lockFacility.get(sbbeId);
		lockOrFail(lock,sbbeId);
		// we hold the lock now
				
		// create sbb entity
		final SbbEntityCacheData cacheData = new SbbEntityCacheData(sbbeId,sleeContainer.getCluster().getMobicentsCache());
		SbbEntityImpl sbbEntity = null;
		if (cacheData.create()) {
			sbbEntity = new SbbEntityImpl(sbbeId, cacheData, true, this);
			// add tx actions wrt lock
			final TransactionalAction rollbackTxAction = new TransactionalAction() {			
				@Override
				public void execute() {
					lockFacility.remove(sbbeId);
					lock.unlock();
				}
			};
			final TransactionalAction commitTxAction = new TransactionalAction() {			
				@Override
				public void execute() {
					lock.unlock();				
				}
			};
			txContext.getAfterRollbackActions().add(rollbackTxAction);
			txContext.getAfterCommitActions().add(commitTxAction);
			
		}
		else {
			sbbEntity = new SbbEntityImpl(sbbeId, cacheData, false, this);
			// add tx actions wrt lock
			final TransactionalAction txAction = new TransactionalAction() {			
				@Override
				public void execute() {
					lock.unlock();
				}
			};
			txContext.getAfterRollbackActions().add(txAction);
			txContext.getAfterCommitActions().add(txAction);			
		}
		
		// store it in the tx, we need to do it due to sbb local object and
		// current storing in sbb entity per tx
		storeSbbEntityInTx(sbbEntity, txContext);
		
		return sbbEntity;		
	}
	
	@Override
	public Set<SbbEntityID> getRootSbbEntityIDs(ServiceID serviceID) {
		final SbbEntityFactoryCacheData cacheData = new SbbEntityFactoryCacheData(sleeContainer.getCluster());
		if (cacheData.exists()) {
			return cacheData.getRootSbbEntityIDs(serviceID);
		}
		else {
			return Collections.emptySet();
		}
	}
	
	@Override
	public SbbEntity getSbbEntity(SbbEntityID sbbeId, boolean lockSbbEntity) {
		
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
			
			// lock before retrieving from cache?
			ReentrantLock lock = null;
			SbbEntityID lockedSbbEntityID = null;
			if (lockSbbEntity) {
				lockedSbbEntityID = sbbeId.getRootSBBEntityID();
				lock = lockFacility.get(lockedSbbEntityID);
				lockOrFail(lock,lockedSbbEntityID);											
			}															
						
			// get sbb entity data from cache
			final SbbEntityCacheData cacheData = new SbbEntityCacheData(sbbeId,sleeContainer.getCluster().getMobicentsCache());
			if (!cacheData.exists() && lock != null) {
				lockFacility.remove(lockedSbbEntityID);
				lock.unlock();
				return null;
			}
			
			// build sbb entity instance
			sbbEntity = new SbbEntityImpl(sbbeId,cacheData,false,this);				
			
			if(lock != null) {
				// add tx actions wrt lock				
				final ReentrantLock fLock = lock;
				final TransactionalAction txAction = new TransactionalAction() {			
					@Override
					public void execute() {
						fLock.unlock();
					}
				};
				txContext.getAfterRollbackActions().add(txAction);
				txContext.getAfterCommitActions().add(txAction);
			}
				
			// store it in the tx, we need to do it due to sbb local object and
			// current storing in sbb entity per tx
			storeSbbEntityInTx(sbbEntity, txContext);
		}
		return sbbEntity;
		
	}
	
	@Override
	public void removeSbbEntity(SbbEntity sbbEntity,
			boolean useCurrentClassLoader) {
	
		if (useCurrentClassLoader) {
			removeSbbEntityWithCurrentClassLoader(sbbEntity);
		}
		else {
			ClassLoader oldClassLoader = Thread.currentThread()
			.getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						sbbEntity.getSbbComponent().getClassLoader());
				removeSbbEntityWithCurrentClassLoader(sbbEntity);
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
	 */
	private void removeSbbEntityWithCurrentClassLoader(
			final SbbEntity sbbEntity) {		
		// remove entity
		sbbEntity.remove();
		// remove from tx data
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		final SbbEntityID sbbEntityID = sbbEntity.getSbbEntityId();
		txContext.getData().remove(sbbEntityID);	
		// if sbb entity is not root add a tx action to ensure lock is removed
		if (sbbEntityID.isRootSbbEntity()) {
			TransactionalAction txAction = new TransactionalAction() {
				@Override
				public void execute() {
					lockFacility.remove(sbbEntityID);
				}
			};
			txContext.getAfterCommitActions().add(txAction);
		}
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
	private static SbbEntityImpl getSbbEntityFromTx(SbbEntityID sbbeId,
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
	private static void lockOrFail(ReentrantLock lock, SbbEntityID sbbeId) throws SLEEException {
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
	public Set<SbbEntityID> getSbbEntityIDs() {
		final SbbEntityFactoryCacheData cacheData = new SbbEntityFactoryCacheData(sleeContainer.getCluster());
		if (cacheData.exists()) {
			return cacheData.getSbbEntities();
		}
		else {
			return Collections.emptySet();
		}
	}
}