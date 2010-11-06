package org.mobicents.slee.runtime.sbbentity;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.transaction.TransactionalAction;

public class SbbEntityUnlockTransactionalAction implements TransactionalAction {

	private static final Logger logger = Logger.getLogger(SbbEntityUnlockTransactionalAction.class);
	
	private SbbEntityImpl sbbEntity;
	private ReentrantLock reentrantLock;
	private final SbbEntityLockFacility lockFacility;
	
	private final boolean sbbEntityCreationTx;
	private final boolean afterRollbackAction;
	
	public SbbEntityUnlockTransactionalAction(boolean sbbEntityCreationTx, boolean afterRollbackAction,SbbEntityLockFacility lockFacility) {
		this.sbbEntityCreationTx = sbbEntityCreationTx;
		this.afterRollbackAction = afterRollbackAction;
		this.lockFacility = lockFacility;
	}
	
	public SbbEntityUnlockTransactionalAction(SbbEntityImpl sbbEntity,
			ReentrantLock reentrantLock, boolean sbbEntityCreationTx, boolean afterRollbackAction,SbbEntityLockFacility lockFacility) {
		this(sbbEntityCreationTx,afterRollbackAction,lockFacility);
		this.sbbEntity = sbbEntity;
		this.reentrantLock = reentrantLock;
	}
	
	public void setReentrantLock(ReentrantLock reentrantLock) {
		this.reentrantLock = reentrantLock;
	}
	
	public void setSbbEntity(SbbEntityImpl sbbEntity) {
		this.sbbEntity = sbbEntity;
	}
	
	public void execute() {
		if (sbbEntity != null) {
			boolean removeLock = false;
			if (afterRollbackAction) {
				if (sbbEntityCreationTx) {
					// the tx created the sbb entity but rolledback
					removeLock = true;					
				}
			}
			else {
				if (sbbEntity.isRemoved()) {
					// the tx committed and the sbb entity was removed
					removeLock = true;					
				}
			}
			if (removeLock) {
				lockFacility.remove(sbbEntity.getSbbEntityId());
			}
		}		
		if (reentrantLock != null) {
			reentrantLock.unlock();
			if (logger.isTraceEnabled()) {
				logger.trace(Thread.currentThread()+" released lock "+reentrantLock+ "for "+sbbEntity);
			}
		}
	}
}
