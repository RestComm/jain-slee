package org.mobicents.slee.runtime.cache.tests;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class TransactionMockup implements Transaction {

	private int status = Status.STATUS_NO_TRANSACTION;
	private Set resources = new HashSet();
	
	public TransactionMockup() {
		status = Status.STATUS_ACTIVE;
	}
	
	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException, SystemException {
		if (getStatus() != Status.STATUS_ACTIVE) throw new IllegalStateException("Tx status is not ACTIVE. Instead status is: " + getStatus());
		
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			XAResource res = (XAResource)iter.next();
			try {
				res.commit(getXid(), false);
			} catch (XAException e) {
				throw new RuntimeException("failed to commmit", e);
			}
		}
		status = Status.STATUS_COMMITTED;
	}

	public boolean delistResource(XAResource arg0, int arg1)
			throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean enlistResource(XAResource newResource) throws RollbackException,
			IllegalStateException, SystemException {
		if (getStatus() != Status.STATUS_ACTIVE) throw new IllegalStateException("Tx status is not ACTIVE. Instead status is: " + getStatus());
		resources.add(newResource);
		return true;
	}

	public int getStatus() throws SystemException {
		return status;
	}

	public void registerSynchronization(Synchronization arg0)
			throws RollbackException, IllegalStateException, SystemException {
		// TODO Auto-generated method stub

	}

	public void rollback() throws IllegalStateException, SystemException {
		if (getStatus() != Status.STATUS_ACTIVE) throw new IllegalStateException("Tx status is not ACTIVE. Instead status is: " + getStatus());

		status = Status.STATUS_MARKED_ROLLBACK;
		
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			XAResource res = (XAResource)iter.next();
			try {
				res.rollback(getXid());
			} catch (XAException e) {
				throw new RuntimeException("failed to rollback cleanly", e);
			}
		}
		status = Status.STATUS_ROLLEDBACK;
	}

	public void setRollbackOnly() throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub

	}

	private Xid getXid() {
		return new XidMockup();
	}
}
