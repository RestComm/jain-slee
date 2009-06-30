package org.mobicents.slee.runtime.sbbentity;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class SbbEntityUnlockTransactionalAction implements TransactionalAction {

	private static final Logger logger = Logger.getLogger(SbbEntityUnlockTransactionalAction.class);
	
	private SbbEntity sbbEntity;
	private ReentrantLock reentrantLock;
	
	private final boolean sbbEntityCreationTx;
	private final boolean afterRollbackAction;
	
	public SbbEntityUnlockTransactionalAction(boolean sbbEntityCreationTx, boolean afterRollbackAction) {
		this.sbbEntityCreationTx = sbbEntityCreationTx;
		this.afterRollbackAction = afterRollbackAction;
	}
	
	public SbbEntityUnlockTransactionalAction(SbbEntity sbbEntity,
			ReentrantLock reentrantLock, boolean sbbEntityCreationTx, boolean afterRollbackAction) {
		this.sbbEntity = sbbEntity;
		this.reentrantLock = reentrantLock;
		this.sbbEntityCreationTx = sbbEntityCreationTx;
		this.afterRollbackAction = afterRollbackAction;
	}
	
	public void setReentrantLock(ReentrantLock reentrantLock) {
		this.reentrantLock = reentrantLock;
	}
	
	public void setSbbEntity(SbbEntity sbbEntity) {
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
				SbbEntityFactory.lockFacility.remove(sbbEntity.getSbbEntityId());
			}
		}

		if (reentrantLock != null) {
			reentrantLock.unlock();
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread()+" released lock "+reentrantLock+ "for "+sbbEntity);
			}
		}
	}
}
