package org.mobicents.slee.runtime.transaction;

import javax.slee.transaction.CommitListener;
import javax.slee.transaction.RollbackListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;

import org.jboss.test.kernel.junit.MicrocontainerTest;

public class SleeTransactionManagerTest extends MicrocontainerTest {
	
	private SleeTransactionManagerImpl _txMgr;
		
	public SleeTransactionManagerTest(String name) {
		super(name);
	}
	
	private SleeTransactionManagerImpl getSleeTransactionManagerImpl() {
		if (_txMgr == null) {
			_txMgr = (SleeTransactionManagerImpl) getBean("Mobicents.JAINSLEE.TransactionManagerMBean");
		}
		return _txMgr;
	}
	
	public void testCommit() throws Exception {
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();
		txMgr.begin();
		txMgr.mandateTransaction();
		txMgr.commit();		
		assertTrue("getTransaction should return null instead it returned "+txMgr.getTransaction(), txMgr.getTransaction() == null);
	}
	
	public void testRequireTransaction() throws Exception {
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();
		if (!txMgr.requireTransaction()) {
			fail("requireTransaction() didn't create a transaction");
		}
		txMgr.commit();		
		assertTrue("getTransaction should return null instead it returned "+txMgr.getTransaction(), txMgr.getTransaction() == null);
	}
	
	public void testRollback() throws Exception {
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();
		txMgr.begin();
		txMgr.mandateTransaction();
		txMgr.rollback();		
		assertTrue("getTransaction should return null instead it returned "+txMgr.getTransaction(), txMgr.getTransaction() == null);
	}
	
	public void testRollbackOnly() throws Exception {
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();
		txMgr.begin();
		txMgr.mandateTransaction();
		assertFalse("tx manager getRollbackOnly should be false", txMgr.getRollbackOnly());		
		txMgr.setRollbackOnly();
		assertTrue("tx manager getRollbackOnly after setRollbackOnly should be true", txMgr.getRollbackOnly());
		assertTrue("tx state should be marked for rollback, instead it is "+txMgr.getStatus(), txMgr.getStatus() == Status.STATUS_MARKED_ROLLBACK);
		try {
			txMgr.commit();	
			fail("tx committed when it should had rollback");
		} catch (RollbackException e) {
			// test passes
		}
	}
	
	public void testAsyncCommit() throws Exception {
		
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();
		txMgr.begin();
		CommitListener listener = new CommitListener() {
			public void committed() {
				getLog().info("committed()");
			}
			public void heuristicMixed(HeuristicMixedException arg0) {
				getLog().error("heuristicMixed()",arg0);
				fail();				
			}
			public void heuristicRollback(HeuristicRollbackException arg0) {
				getLog().error("heuristicRollback()",arg0);
				fail();
			}
			public void rolledBack(RollbackException arg0) {
				getLog().error("rolledBack()",arg0);
				fail();
			}
			public void systemException(SystemException arg0) {
				getLog().error("systemException()",arg0);
				fail();
			}			
		};
		txMgr.asyncCommit(listener);
		// javadoc in slee 1.1 specs define that the thread should not be associated with the tx
		if (txMgr.getTransaction() != null) {
			getLog().error("the thread should not be associated with the tx");
		}		
	}
	
	public void testAsyncRollback() throws Exception {
		
		SleeTransactionManagerImpl txMgr = getSleeTransactionManagerImpl();		
		txMgr.begin();
		RollbackListener listener = new RollbackListener() {
			public void rolledBack() {
				getLog().info("rolledBack()");
			}
			public void systemException(SystemException arg0) {
				getLog().error("systemException()",arg0);
				fail();				
			}
		};
		txMgr.asyncRollback(listener);
		// javadoc in slee 1.1 specs define that the thread should not be associated with the tx
		if (txMgr.getTransaction() != null) {
			getLog().error("the thread should not be associated with the tx");
		}		
	}
	
}
