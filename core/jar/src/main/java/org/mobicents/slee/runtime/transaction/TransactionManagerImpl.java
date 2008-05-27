/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.cache.CacheException;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.cache.lock.TimeoutException;
import org.jboss.cache.lock.UpgradeException;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.mx.util.MBeanProxyCreationException;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

/**
 * Implementation of SLEE Tx manager.
 * 
 * @author Tim Fox - Complete re-write.
 * @author M. Ranganathan
 * @author J. Deruelle
 * @author Ralf Siedow
 * @author Ivelin Ivanov
 * 
 */
public class TransactionManagerImpl extends ServiceMBeanSupport 
	implements TransactionManagerImplMBean {

	private static Logger logger = Logger
			.getLogger(TransactionManagerImpl.class);

	public static String TCACHE = "tcache";

	public static String DEPLOYMENT_CACHE = "deploymentCache";

	public static String PROFILE_CACHE = "profileCache";

	public static String RUNTIME_CACHE = "runtimeCache";

	private HashMap treeCaches;

	private ObjectName tcacheName;

	private ObjectName deploymentTreeCacheName;

	private ObjectName runtimeTreeCacheName;

	private ObjectName profileTreeCacheName;

	private TransactionManager txMgr;

	private static String getFileAndLine() {
		Exception ex = new Exception();
		StringBuffer sbuf = new StringBuffer().append("\n");
		for (int i = 2; i < 8; i++) {
			String fname = ex.getStackTrace()[i].getFileName();
			int line = ex.getStackTrace()[i].getLineNumber();
			sbuf.append("[" + fname).append(":" + line + "]");
		}
		return sbuf.toString();
	}

	public static String getRootFqn(String cacheName) {
		return "";

	}

	/**
	 * Map which holds debug info on transactions initiated by the SLEE. Helps
	 * debug tx lock situations
	 */
	private Map sleeTransactions = new ConcurrentReaderHashMap();

	/**
	 * A map of objects that are intended to be used at the time when an ongoing
	 * transaction completes
	 */
	private Map txDeferredTable = new ConcurrentReaderHashMap();

	public TransactionManagerImpl() {
		super(TransactionManagerImplMBean.class);
		this.treeCaches = new HashMap();

	}

	/*
	 * Check whether a transaction is associated to the current thread, and
	 * throw an exception if there isn't one. This is to be used for methods
	 * with "Mandatory" transactional semantics
	 */
	public void mandateTransaction() throws TransactionRequiredLocalException {
		Transaction tx = null;
		try {
			tx = getTransaction();

			if (tx == null)
				throw new TransactionRequiredLocalException(
						"Transaction Mandatory");

			if (!((tx.getStatus() == Status.STATUS_ACTIVE) || ((tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)))) {
				throw new IllegalStateException(
						"There is no active tx, tx is in state: "
								+ tx.getStatus());
			}
		} catch (SystemException e) {
			logger.error(
					"Caught SystemException in getting transaction/ status", e);
		}

	}

	public static Integer makeKey(Transaction tx) {
		return new Integer(tx.hashCode());
	}

	public void setRollbackOnly() throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("setrollbackonly called on tx:"
					+ makeKey(getTransaction()));
		txMgr.setRollbackOnly();
	}

	public boolean getRollbackOnly() throws SystemException {
		Transaction tx = getTransaction();
		return tx.getStatus() == Status.STATUS_MARKED_ROLLBACK;
	}

	/*
	 * Check whether a transaction is associated to the current thread, and if
	 * not start a new one This is to be used for methods with "Required"
	 * transactional semantics Returns true if a new transaction had to be
	 * started
	 */
	public boolean requireTransaction() {
		try {
			Transaction tx = getTransaction();

			if (tx == null) {
				// No transaction so start a new one
				this.begin();
				return true;
			}

			if (!((tx.getStatus() == Status.STATUS_ACTIVE) || ((tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)))) {
				throw new IllegalStateException(
						"Transaction is in illegal state: " + tx.getStatus());
			}
		} catch (SystemException e) {
			logger.error("Caught SystemException in checking transaction", e);
		}
		return false;
	}

	public boolean isInTx() throws SystemException {
		return getTransaction() != null;
	}

	/**
	 * Get the transaction associated with the current thread, or null if none
	 */

	public Transaction getTransaction() {
		try {
			return txMgr.getTransaction();
		} catch (SystemException e) {
			throw new RuntimeException("Failed to obtain active JTA transaction");
		}
	}

	class SynchronizationHandler implements Synchronization {
		private Transaction tx;

		public SynchronizationHandler(Transaction tx) {
			this.tx = tx;

			// record debug info about the slee tx map
			List llist = Collections.synchronizedList(new LinkedList());
			llist.add(new Exception());
			sleeTransactions.put(tx, llist);
		}

		public void afterCompletion(int status) {

			// remove debug info about the slee tx map
			sleeTransactions.remove(tx);
			if (logger.isDebugEnabled())
				logger.debug("afterCompletion, status is " + status + " tx is "
						+ makeKey(tx));

			switch (status) {

			case Status.STATUS_COMMITTED:
				if (logger.isDebugEnabled())
					logger.debug("Transaction: " + makeKey(tx)
							+ " has committed");
				executeAfterCommitActions(tx);

				break;
			case Status.STATUS_ROLLEDBACK:
				if (logger.isDebugEnabled())
					logger.debug("Transaction: " + makeKey(tx)
							+ " has rolled-back");
				executeAfterRollbackActions(tx);

				break;
			default:
				if (logger.isDebugEnabled())
					logger.debug("Transaction is in state:" + status);
				throw new IllegalStateException("Transaction is in state "
						+ status);
			}
		}

		public void beforeCompletion() {
			if (logger.isDebugEnabled())
				logger.debug("beforeCompletion, tx is " + makeKey(tx));
			executePrepareActions(tx);
		}
	}

	/* Begin a transaction */
	public void begin() throws SystemException {

		Transaction tx = getTransaction();
		if (tx != null) {
			String err = "Transaction already started, cannot nest tx. Ongoing Tx: "
					+ tx;
			SystemException se = new SystemException(err);
			logger.error(err, se);
			logger.error(displayOngoingSleeTransactions());
			throw se;
		} else {
			try {
				txMgr.begin();
				if (logger.isDebugEnabled())
					logger.debug("Transaction started.");
				logTxID();
			} catch (NotSupportedException e) {
				if (logger.isDebugEnabled())
					logger.error("Failed to begin transaction.", e);
				throw new SystemException("Failed to begin transaction." + e);
			}
		}
		if (logger.isDebugEnabled())
			logger.debug("Tx after begin:" + txMgr.getTransaction());

		tx = getTransaction();

		if (tx != null) {
			if (logger.isDebugEnabled())
				logger.debug("Tx state is "
						+ txMgr.getTransaction().getStatus());
		}
		// Register for call-backs
		SynchronizationHandler handler = new SynchronizationHandler(tx);

		try {
			tx.registerSynchronization(handler);
		} catch (RollbackException e) {
			// This would be extremely strange if this ever got called.
			logger.error(e);
		}
	}

	/**
	 * 
	 * Print the current transaction ID for the context.
	 * 
	 */
	private void logTxID() {
		if (logger.isDebugEnabled()) {
			Transaction t;
			t = getTransaction();
			if (logger.isDebugEnabled())
				logger.debug("Context local TxID: " + t);
			if (logger.isTraceEnabled()) {
				try {
					throw new Exception();
				} catch (Exception e) {
					logger.trace("Call stack", e);
				}
			}
		}
	}

	/* Commit a transaction */
	public void commit() throws SystemException {
		// new Exception().printStackTrace();

		Transaction tx = getTransaction();

		if (tx == null) {
			throw new SystemException(
					"Failed to commit transaction since there is no transaction to commit!");
		}

		if (logger.isDebugEnabled())
			logger.debug("Committing tx");

		logTxID();

		if (tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
			
			if (logger.isDebugEnabled())
				logger
				.debug("Transaction marked for roll back, cannot commit, ending with rollback: "
						+ makeKey(tx));
			rollback();
			

		} else if (tx.getStatus() == Status.STATUS_ACTIVE) {
			try {
				txMgr.commit();
				if (logger.isDebugEnabled())
					logger.debug("Committed tx");
			} catch (Exception e) {
				throw new SystemException("Failed to commit tx. "+e.getMessage());
			}

		} else {
			throw new SystemException(
					"Failed to commit transaction since transaction is in state: "
							+ tx.getStatus());
		}

	}

	/* Roll-back a transaction */
	public void rollback() throws SystemException {
		// new Exception().printStackTrace();
		Transaction tx = getTransaction();
		if (tx == null) {
			throw new SystemException(
					"Failed to rollback transaction since there is no transaction to rollback!");
		}

		if (logger.isDebugEnabled())
			logger.debug("Rolling-back tx.");
		logTxID();

		if (!((tx.getStatus() != Status.STATUS_ACTIVE) || (tx.getStatus() != Status.STATUS_MARKED_ROLLBACK))) {
			throw new SystemException(
					"Failed to rollback transaction since transaction is in state: "
							+ tx.getStatus());
		}

		txMgr.rollback();

		if (logger.isDebugEnabled())
			logger.debug("Transaction rollbacked");
	}

	/*
	 * Remove the entry in the transaction table corresponding to the
	 * transaction associated with the current thread
	 */
	private TxLocalEntry removeEntry(Transaction tx) {
		return (TxLocalEntry) txDeferredTable.remove(makeKey(tx));
	}

	/* Execute all the post commit transactional actions */
	private void executeAfterCommitActions(Transaction tx) {
		Integer tid = makeKey(tx);
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.remove(tid);
		if (logger.isDebugEnabled())
			logger.debug("Executing commit actions for transaction: " + tid);

		if (entry != null) {
			entry.executeAfterCommitActions();
		} else {
			if (logger.isDebugEnabled())
				logger.debug("no commit actions to execute for the tx! " + tid);
		}
		if (logger.isDebugEnabled())
			logger.debug("Done executing afterCommitAction " + tid);
	}

	private void executePrepareActions(Transaction tx) {
		if (logger.isDebugEnabled())
			logger.debug("Executing Prepare actions for transaction : " + tx);
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(makeKey(tx));
		if (entry != null) {
			entry.executePrepareActions();
		}
	}

	/* Execute all the rollback transactional actions */
	private void executeAfterRollbackActions(Transaction tx) {
		if (logger.isDebugEnabled())
			logger.debug("Executing rollback actions for transaction:" + tx);
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.remove(makeKey(tx));
		if (entry != null) {
			entry.executeAfterRollbackActions();
		}
	}

	// Hack so the timer facility can remove any set timer actions
	public List getCommitActions() throws SystemException {
		assertIsInTx();
		Transaction tx = getTransaction();
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(makeKey(tx));
		if (entry != null) {
			return entry.getAfterCommitActions();
		} else {
			return null;
		}
	}

	public List getPrepareActions() throws SystemException {
		assertIsInTx();
		Transaction tx = getTransaction();
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(makeKey(tx));
		if (entry != null) {
			return entry.getPrepareActions();
		} else {
			return null;
		}
	}

	/**
	 * looks up the map with objects intended to be used at tx completion
	 * 
	 * @return the object from the deferred tx map
	 */
	public Object getTxLocalData(Object key) {
		assertIsInTx();
		Transaction tx = getTransaction();
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(makeKey(tx));
		if (entry != null) {
			return entry.getData(key);
		} else {
			return null;
		}
	}

	/**
	 * Adds an object to a map that will be looked up when an ongoing
	 * transaction completes
	 */
	public void putTxLocalData(Object key, Object value) {
		assertIsInTx();
		Transaction tx = getTransaction();
		Integer tid = makeKey(tx);
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(tid);

		if (entry == null) {
			entry = new TxLocalEntry(tid.toString());
			txDeferredTable.put(tid, entry);
		}

		entry.putData(key, value);
	}

	/**
	 * removes an entry from the deffered map
	 * 
	 * @param key
	 *            the lookup key for the object in the map
	 * @see TransactionManagerImpl#getTxLocalData(Object)
	 * @see TransactionManagerImpl#putDeferredData(Object)
	 */
	public void removeTxLocalData(Object key) throws SystemException {
		assertIsInTx();
		Transaction tx = getTransaction();
		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(makeKey(tx));

		if (entry == null)
			return;

		entry.removeData(key);

	}

	/**
	 * Add an after commit action. These are executed if and after the
	 * transaction commits
	 */
	public void addAfterCommitAction(TransactionalAction action) {
		assertIsInTx();
		if (logger.isDebugEnabled())
			logger.debug("Adding commit action: " + action);
		TxLocalEntry entry = getEntry();
		entry.addAfterCommitAction(action);
	}

	/**
	 * Add a prepare commit action. These are executed if the transaction begins
	 * the commit phase (prepare) and before it completes.
	 */
	public void addPrepareCommitAction(TransactionalAction action) {
		assertIsInTx();
		if (logger.isDebugEnabled())
			logger.debug("Adding prepare action: " + action);
		TxLocalEntry entry = getEntry();
		entry.addPrepareAction(action);
	}

	private TxLocalEntry getEntry() {
		Transaction tx = getTransaction();
		Integer tid = makeKey(tx);
		if (logger.isDebugEnabled())
			logger.debug("TX is " + tid);

		TxLocalEntry entry = (TxLocalEntry) txDeferredTable.get(tid);
		if (entry == null) {
			if (logger.isDebugEnabled())
				logger.debug("Entry not already there");
			entry = new TxLocalEntry(tid.toString());
			txDeferredTable.put(tid, entry);
		}
		return entry;
	}

	/**
	 * Add a rollback action to the transaction - these will be executed if the
	 * transaction rolls back
	 */
	public void addAfterRollbackAction(TransactionalAction action) {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			logger.debug("Adding rollback action: " + action);
		}
		TxLocalEntry entry = getEntry();
		entry.addAfterRollbackAction(action);
	}

	/* The remaining methods in this class are related to the TreeCache */
	private TreeCache getTreeCache(String treeCacheName) {
		return (TreeCache) this.treeCaches.get(treeCacheName);
	}

	/**
	 * Put an object in the persistant store.
	 * 
	 * @param fqn --
	 *            the fully qualified name under which put the key-value pair
	 * @param key --
	 *            the key name under which put the object for this FQN
	 * @param object --
	 *            the object to store
	 * 
	 * @throws TransactionRequiredLocalException
	 *             if not called in the context of a current transaction.
	 * @throws SystemException
	 *             if failure occurs for any other reason.
	 */

	public void putObject(String cacheName, String fqn, Object key, Object object) {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			((List) sleeTransactions.get(this.getTransaction()))
					.add("putObject(" + cacheName + "," + fqn + "," + key + ","
							+ object + ")" + getFileAndLine());

		}
		try {

			getTreeCache(cacheName).put(getRootFqn(cacheName) + "/" + fqn, key,
					object);
		} catch (UpgradeException ex) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			try {
				getTreeCache(cacheName).put(getRootFqn(cacheName) + "/" + fqn,
						key, object);
			} catch (CacheException e1) {
				log.error(this.displayOngoingSleeTransactions());
				throw new RuntimeException(ex.getMessage(), e1);
			}

		} catch (Exception ex) {
			log.error(this.displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * Put a set of objects in the persistant store.
	 * 
	 * @param fqn --
	 *            the fully qualified name under which put the set of key-value
	 *            pairs
	 * @param data --
	 *            the set of key-value pair. can be null
	 * 
	 * @throws TransactionRequiredLocalException
	 *             if not called in the context of a current transaction.
	 * @throws SystemException
	 *             if failure occurs for any other reason.
	 */

	public void createNode(String cacheName, String fqn, Map data)
			{
		mandateTransaction();
		
		if (data == null) { 
			logger.warn("createNode(String cacheName, String fqn, Map data): Storing null in distributed cache. Potentially wasteful operation.");
		}
		
		try {
			getTreeCache(cacheName)
					.put(getRootFqn(cacheName) + "/" + fqn, data);
		} catch (UpgradeException ex) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.warn(this.displayOngoingSleeTransactions());
			}
			try {
				getTreeCache(cacheName).put(getRootFqn(cacheName) + "/" + fqn,
						data);
			} catch (CacheException e1) {
				log.error(this.displayOngoingSleeTransactions());
				throw new RuntimeException(ex.getMessage(), e1);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * remove data or a set of objects in the persistent store.
	 * 
	 * @param fqn --
	 *            the fully qualified name under which data is being cleared
	 * 
	 * @throws TransactionRequiredLocalException
	 *             if not called in the context of a current transaction.
	 * @throws SystemException
	 *             if failure occurs for any other reason.
	 */
	public void clearNode(String cacheName, String fqn) {
		mandateTransaction();
		try {
			getTreeCache(cacheName).removeData(
					getRootFqn(cacheName) + "/" + fqn);
		} catch (UpgradeException ex) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				getTreeCache(cacheName).removeData(
						getRootFqn(cacheName) + "/" + fqn);
			} catch (CacheException e1) {
				log.error(this.displayOngoingSleeTransactions());
				// ex.printStackTrace();
				throw new RuntimeException(ex.getMessage(), e1);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * Get an object from the persistant store.
	 * 
	 * @param tcache --
	 *            transactional cache to store to.
	 * 
	 * @param fqn --
	 *            the fully qualified name under which to retrieve the object
	 * @param key --
	 *            a key to retrieve the object.
	 * @return the object pointed to by the fqn and the key or null if nothing
	 *         available
	 */
	public Object getObject(String tcache, String fqn, Object key) {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			if (sleeTransactions.get(this.getTransaction()) != null)
				((List) sleeTransactions.get(this.getTransaction()))
						.add("getObject(" + tcache + "," + fqn + "," + key
								+ ")" + getFileAndLine());

		}
		try {
			if (logger.isDebugEnabled())
				logger.debug("getObject ( " + tcache + "," + fqn + "," + key
						+ ")");
			String localFqn = getRootFqn(tcache) + "/" + fqn;

			Object object = getTreeCache(tcache).get(localFqn, key);
			// this.owns(localFqn);
			if (logger.isDebugEnabled())
				logger.debug(">>> getObject ( " + tcache + "," + fqn + ","
						+ key + ") returning 	" + object);
			return object;
		} catch (TimeoutException ex) {
			logger.error(displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error("Failed getObject(" + getRootFqn(tcache) + "/" + fqn
					+ "/" + key + ")", ex);
			logger.error(displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}


	/**
	 * Get the keys of the attributes assigned to a given cache node
	 * 
	 * @param fqn --
	 *            the fully qualified name of the node to retrieve
	 * @return the key set
	 */
	public Set getKeys(String tcache, String fqn) {
		assertIsInTx();
		String localFqn = getRootFqn(tcache) + "/" + fqn;
		try {
			if (logger.isDebugEnabled()) {
				if (sleeTransactions.get(this.getTransaction()) != null) {
					((List) sleeTransactions.get(this.getTransaction()))
							.add("getKeys (" + tcache + " , " + fqn + ")"
									+ getFileAndLine());
				}

			}
			Set keys = getTreeCache(tcache).getKeys(localFqn);
			return keys;
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());
			throw new RuntimeException("Timeout occured", ex);
		} catch (Exception ex) {
			logger.error("Failed getKeys(" + localFqn
					+ ")", ex);
			logger.error(displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	
	
	/**
	 * Get a node from the persistent store.
	 * 
	 * @param fqn --
	 *            the fully qualified name of the node to retrieve
	 * @return the jboss cache node for the fqn
	 * @deprecated exposes underlying cache implementation API. Use putObject(),getObject() and getNodeKeys() 
	 */
	public Node getNode(String tcache, String fqn) {
		assertIsInTx();
		String localFqn = getRootFqn(tcache) + "/" + fqn;
		try {
			if (logger.isDebugEnabled()) {
				if (sleeTransactions.get(this.getTransaction()) != null) {
					((List) sleeTransactions.get(this.getTransaction()))
							.add("getNode (" + tcache + " , " + fqn + ")"
									+ getFileAndLine());
				}

			}
			if (getTreeCache(tcache).exists(localFqn)) {
				if (logger.isDebugEnabled())
					logger.debug("getNode " + tcache + " fqn = " + fqn);
				Node retval = getTreeCache(tcache).get(
						localFqn);
				// this.owns(localFqn);
				if (logger.isDebugEnabled())
					logger.debug(">>> getNode " + tcache + " fqn = " + fqn);
				return retval;
			} else {
				return null;
			}
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());

			// SleeContainer.lookupFromJndi().getEventRouter().reRouteEvent();;
			throw new RuntimeException("Timeout occured", ex);
		} catch (Exception ex) {

			logger.error("Failed getNode(" + localFqn
					+ ")", ex);
			logger.error(displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * @return String - a list of ongoing SLEE transactions
	 */
	// public String displayOngoingSleeTransactions() { return "" ; }
	public String displayOngoingSleeTransactions() {
		if (logger.isDebugEnabled()) {
			// cann't iterate the Set while it can be modified
			synchronized (sleeTransactions) {
				Iterator iter = sleeTransactions.keySet().iterator();
				logger.error("current tx is "
						+ this.getTransaction());
				String msg = "---------+ Begin dump of SLEE TX map: +-------------------------- \n";

				while (iter.hasNext()) {
					Object tx = iter.next();
					msg += "----+ Transaction: " + tx
							+ " started by (stack trace): \n";

					Exception ex = (Exception) ((List) sleeTransactions.get(tx))
							.get(0);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(os);
					ex.printStackTrace(ps);
					ps.close();
					msg += os.toString() + "\n";
					List ll = (List) sleeTransactions.get(tx);
					synchronized (ll) {
						Iterator it = ll.iterator();

						it.next();
						while (it.hasNext()) {
							msg += it.next() + "\n";
						}
					}
					msg += "-------------------------------------------";

				}
				msg += "\nEnd dump of SLEE TX map: -------------------------- \n";
				return msg;
			}
		} else {
			return "";
		}
	}

	public Set getChildrenNames(String tcache, String fqn)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			if (sleeTransactions.get(this.getTransaction()) != null)
				((List) sleeTransactions.get(this
						.getTransaction()))
						.add("getChildrenNames (" + tcache + " , " + fqn + ")"
								+ getFileAndLine());

		}
		try {
			return getTreeCache(tcache).getChildrenNames(
					getRootFqn(tcache) + "/" + fqn);
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());
			// SleeContainer.lookupFromJndi().getEventRouter().reRouteEvent();;
			throw new SystemException(ex.getMessage());

		} catch (Exception ex) {
			log.error("failed getChildrenNames ( " + fqn + ")");
			log.error(displayOngoingSleeTransactions());
			throw new SystemException(ex.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.TransactionManager#removeObject(java.lang.String)
	 */
	public void removeObject(String tcache, String nodeName, Object attributeKey) {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			if (sleeTransactions.get(this.getTransaction()) != null)
				((List) sleeTransactions.get(this.getTransaction()))
						.add("removeObject (" + tcache + " , " + nodeName + "," + attributeKey + ") "
								+ getFileAndLine());

		}
		try {
			getTreeCache(tcache).remove(getRootFqn(tcache) + "/" + nodeName, attributeKey);
		} catch (UpgradeException ex) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				logger.warn("Failed to remove object with key '" + nodeName + "/" + attributeKey + "' from cache '" + tcache + "'. Will retry.", e);
			}
			try {
				getTreeCache(tcache).remove(getRootFqn(tcache) + "/" + nodeName, attributeKey);
			} catch (CacheException e1) {
				// TODO Auto-generated catch block
				log.error(this.displayOngoingSleeTransactions(), e1);
				// ex.printStackTrace();
				throw new RuntimeException(ex.getMessage(), e1);
			}
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());
			// SleeContainer.lookupFromJndi().getEventRouter().reRouteEvent();;
			throw new RuntimeException(ex.getMessage(), ex);

		} catch (Exception ex) {
			logger.error(this.displayOngoingSleeTransactions());
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * Remove a node and all its sub node and its hashmap
	 * 
	 * @param fqn --
	 *            the fully qualified name of the noe to retrieve
	 */

	public void removeNode(String tcache, String fqn) {
		assertIsInTx();
		try {
			if (fqn.startsWith("/")) {
				log.warn("Illegal start of name " + fqn);
			}

			if (logger.isDebugEnabled()) {
				log.debug("removeNode(): Removing node " + getRootFqn(tcache)
						+ "/" + fqn + " from Cache!");
				((List) sleeTransactions.get(this.getTransaction()))
						.add("removeNode (" + tcache + " , " + fqn + ")"
								+ getFileAndLine());

			}
			if (nodeExists(tcache, fqn)) {
				getTreeCache(tcache).remove(getRootFqn(tcache) + "/" + fqn);
			} else {
				if (logger.isDebugEnabled())
					log
							.debug("removeNode(): node not found - nothing to remove");
			}
		} catch (UpgradeException ex) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				getTreeCache(tcache).remove(getRootFqn(tcache) + "/" + fqn);
			} catch (CacheException e1) {
				// TODO Auto-generated catch block
				log.error(this.displayOngoingSleeTransactions());
				// ex.printStackTrace();
				throw new RuntimeException(ex.getMessage(), e1);
			}
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions(), ex);
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(this.displayOngoingSleeTransactions(), ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * Retrieve the children of a node
	 * 
	 * @param the
	 *            fqn of the node
	 * @return a map with all the children or null if no children for this node
	 *         exists
	 * @throws SystemException
	 *             if a low level exception occurs
	 */

	public Map getChildren(String tcache, String fqn) throws SystemException {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			if (sleeTransactions.get(this.getTransaction()) != null)	
			((List) sleeTransactions.get(this.getTransaction()))
					.add("getChildren (" + tcache + " , " + fqn + ")"
							+ getFileAndLine());

		}
		try {
			String localFqn = getRootFqn(tcache) + "/" + fqn;
			if (!getTreeCache(tcache).exists(localFqn))
				return null;

			if (logger.isDebugEnabled())
				logger.debug("getChildren (" + tcache + "," + fqn + ")"
						+ getFileAndLine());
			Node node = getTreeCache(tcache).get(localFqn);
			// this.owns(localFqn);
			if (logger.isDebugEnabled())
				logger.debug("getChildren (" + tcache + "," + fqn + ")"
						+ getFileAndLine());
			return node.getChildren();
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());
			// SleeContainer.lookupFromJndi().getEventRouter().reRouteEvent();;
			throw new SystemException(ex.getMessage());

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SystemException("trouble getting children");
		}
	}

	/**
	 * Remove all objects from the Cache. Used on SLEE exit.
	 * 
	 * @throws SystemException
	 * 
	 */

	public void cleanUpCache(String tcache) throws SystemException {
		try {
			if (!getTreeCache(tcache).exists(getRootFqn(tcache)))
				return;

			// TODO: test to make sure it does what it suppose to do
			getTreeCache(tcache).remove(getRootFqn(tcache));

			/*
			 * Node node = getTreeCache(tcache).get(getRootFqn(tcache));
			 * node.removeAllChildren();
			 */
		} catch (CacheException ex) {
			logger
					.error(
							"Failed to clean SLEE cache. This could lead to unexpected behaviour! Recommendation: Restart SLEE VM!",
							ex);
			throw new SystemException("Failed to clean SLEE cache. "
					+ ex.getMessage());
		}
	}

	/**
	 * Remove the children of a node
	 * 
	 * @param tcache -
	 *            the transactional cache to remove from
	 * @param the
	 *            fqn of the node
	 * @throws SystemException
	 *             if a low level exception occurs
	 */

	public void removeChildren(String tcache, String fqn)
			throws SystemException {
		assertIsInTx();
		if (logger.isDebugEnabled()) {
			if (sleeTransactions.get(this.getTransaction()) != null)
			((List) sleeTransactions.get(this.getTransaction()))
					.add("removeChildren (" + tcache + " , " + fqn + ")"
							+ getFileAndLine());

		}
		try {
			String localFqn = getRootFqn(tcache) + "/" + fqn;
			if (!getTreeCache(tcache).exists(localFqn))
				return;
			if (logger.isDebugEnabled())
				logger.debug("removeChildren (" + tcache + "," + fqn + ")"
						+ getFileAndLine());

			getTreeCache(tcache).remove(localFqn);
			// Node node = getTreeCache(tcache).get(localFqn);

			if (logger.isDebugEnabled())
				logger.debug("removeChildren (" + tcache + "," + fqn + ")"
						+ getFileAndLine());
			// node.removeAllChildren();
		} catch (TimeoutException ex) {
			logger.error(this.displayOngoingSleeTransactions());
			// SleeContainer.lookupFromJndi().getEventRouter().reRouteEvent();;
			throw new SystemException(ex.getMessage());

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SystemException(ex.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.SleeTransactionManager#removeChild(java.lang.String,
	 *      java.lang.String)
	 */
	public void removeChild(String tcache, String fqnRoot, String key) {
		assertIsInTx();
		try {
			if (logger.isDebugEnabled()) {
				if (sleeTransactions.get(this.getTransaction()) != null)
				((List) sleeTransactions.get(this.getTransaction()))
						.add("removeChild (" + tcache + " , " + fqnRoot + " , "
								+ key + ")" + getFileAndLine());

			}
			

			getTreeCache(tcache)
					.remove(getRootFqn(tcache) + "/" + fqnRoot, key);

			

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return;

	}

	/**
	 * 
	 * start MBean service lifecycle method
	 * 
	 */
	public void startService() throws Exception {
		
		// Force this property to allow invocation of getters.
		// http://code.google.com/p/mobicents/issues/detail?id=63
		System.setProperty( "jmx.invoke.getters", "true" );
		
		initTreeCache();

		// We are going to use the JBoss transaction manager
		txMgr = (TransactionManager) new InitialContext()
				.lookup("java:/TransactionManager");

		// register service in JNDI
		SleeContainer.registerWithJndi("slee", JNDI_NAME, this);
	}

	/**
	 * 
	 * stop MBean service lifecycle method
	 * 
	 */
	public void stopService() throws Exception {
		// unregister service in JNDI
		SleeContainer.unregisterWithJndi(JNDI_NAME);
	}

	// for debugging only
	// (Ivelin) FIXME: this is identical to mandateTx. One of them should be
	// removed.
	public void assertIsInTx() {
		mandateTransaction();
	}

	public void assertIsNotInTx() {
		Transaction t;
		t = getTransaction();
		if ((t) != null) {
			throw new IllegalStateException(
					"Should NOT be in an tx!! TxID: " + t);
		}
	}

	/**
	 * 
	 * Set the TreeCache instance to be used by the SLEE
	 * 
	 * @param newTreeCache
	 */
	public void setTreeCacheName(ObjectName newTreeCacheName) {
		tcacheName = newTreeCacheName;
	}

	public void setDeploymentTreeCacheName(ObjectName newTreeCacheName) {
		this.deploymentTreeCacheName = newTreeCacheName;
	}

	public void setProfileTreeCacheName(ObjectName newTreeCacheName) {
		this.profileTreeCacheName = newTreeCacheName;
	}

	public void setRuntimeTreeCacheName(ObjectName runtimeCacheName) {
		this.runtimeTreeCacheName = runtimeCacheName;
	}

	/**
	 * 
	 * Set the TreeCache instance to be used by the SLEE
	 * 
	 * @param newTreeCache
	 */
	public ObjectName getTreeCacheName() {
		return tcacheName;
	}

	/**
	 * 
	 * Set the TreeCache instance to be used by the SLEE
	 * 
	 * @param newTreeCache
	 * @throws MBeanProxyCreationException
	 */
	private synchronized void initTreeCache()
			throws MBeanProxyCreationException {

		if (getTreeCache(RUNTIME_CACHE) == null) {
			TreeCacheMBean tcm = (TreeCacheMBean) MBeanProxy.get(
					TreeCacheMBean.class, this.runtimeTreeCacheName, server);
			TreeCache runtimeTreeCache = tcm.getInstance();
			this.treeCaches.put(RUNTIME_CACHE, runtimeTreeCache);
			if (logger.isDebugEnabled())
				logger.debug("runtimeTreeCache = "
						+ this.getTreeCache(RUNTIME_CACHE));

		}

		if (getTreeCache(TCACHE) == null) {
			TreeCacheMBean tcm = (TreeCacheMBean) MBeanProxy.get(
					TreeCacheMBean.class, tcacheName, server);
			TreeCache tcache = tcm.getInstance();
			// tcache.setLockAcquisitionTimeout(3);
			// DEBUG tcache.addTreeCacheListener(new TreeCacheListenerImpl(this));
			this.treeCaches.put(TCACHE, tcache);
			if (logger.isDebugEnabled())
				logger.debug("treeCache = " + this.getTreeCache(TCACHE));
		}

		if (getTreeCache(DEPLOYMENT_CACHE) == null) {
			TreeCacheMBean tcm = (TreeCacheMBean) MBeanProxy.get(
					TreeCacheMBean.class, this.deploymentTreeCacheName, server);
			TreeCache deploymentTreeCache = tcm.getInstance();
			this.treeCaches.put(DEPLOYMENT_CACHE, deploymentTreeCache);
			if (logger.isDebugEnabled())
				logger.debug("deploymentTreeCache = "
						+ this.getTreeCache(DEPLOYMENT_CACHE));

		}
		if (getTreeCache(PROFILE_CACHE) == null) {
			TreeCacheMBean tcm = (TreeCacheMBean) MBeanProxy.get(
					TreeCacheMBean.class, this.profileTreeCacheName, server);
			TreeCache profileTreeCache = tcm.getInstance();
			this.treeCaches.put(PROFILE_CACHE, profileTreeCache);
			if (logger.isDebugEnabled())
				logger.debug("profileTreeCache = "
						+ this.getTreeCache(PROFILE_CACHE));

		}
		if (logger.isDebugEnabled())
			logger.debug("comparing caches profileCache / tcache "
					+ (getTreeCache(PROFILE_CACHE) == getTreeCache(TCACHE)));
		if (logger.isDebugEnabled())
			logger
					.debug("comparing caches pofileCache / deploymentCache "
							+ (getTreeCache(PROFILE_CACHE) == getTreeCache(DEPLOYMENT_CACHE)));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#nodeExists(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean nodeExists(String tcache, String fqn) {
		return getTreeCache(tcache).exists(getRootFqn(tcache) + "/" + fqn);
	}

	/**
	 * @deprecated use {@link #getObject(String, String, Object)}
	 */
	public Object getObject(String cacheName, String fqn, String key) throws SystemException {
		return getObject(cacheName, fqn, (Object)key);
	}

	/**
	 * @deprecated use {@link #putObject(String, String, Object, Object)}
	 */
	public void putObject(String cacheName, String fqn, String key, Object object) {
		putObject(cacheName, fqn, (Object)key, object);
	}

	
//	FIXME - methods from javax.transaction.TransactionManager
	//NO DESC ON DOCJAR.COM OR SUN javadocs for those methods ...
	public int getStatus() throws SystemException {
		int status=0;
		
		Transaction tx=null;
		
			tx = getTransaction();

			if (tx == null)
				throw new SystemException(
						"No Tx associated with current thread!!!");

			status=tx.getStatus();
		
		
		return status;
	}

	public void setTransactionTimeout(int timeout) throws SystemException {

		txMgr.setTransactionTimeout(timeout);
		
	}

	public Transaction suspend() throws SystemException {
		
		return txMgr.suspend();
	}

	public void resume(Transaction txToResume) throws InvalidTransactionException, IllegalStateException, SystemException {
		txMgr.resume(txToResume);
		
	}

}
