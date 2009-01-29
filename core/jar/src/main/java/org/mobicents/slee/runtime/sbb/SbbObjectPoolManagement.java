package org.mobicents.slee.runtime.sbb;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.ComponentID;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Manages sbb object pools in the SLEE container.
 * 
 * @author martins
 * 
 */
public class SbbObjectPoolManagement implements SbbObjectPoolManagementMBean {

	private final static Logger logger = Logger
			.getLogger(SbbObjectPoolManagement.class);

	private final ConcurrentHashMap<ComponentID, SbbObjectPool> pools;
	private final SleeContainer sleeContainer;

	private GenericObjectPool.Config config;

	public SbbObjectPoolManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		// create pool config mbean with default pool configuration
		config = new GenericObjectPool.Config();
		config.maxActive = -1;
		config.maxIdle = 50;
		config.maxWait = -1;
		config.minEvictableIdleTimeMillis = 60000;
		config.minIdle = 0;
		config.numTestsPerEvictionRun = -1;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		config.testWhileIdle = false;
		config.timeBetweenEvictionRunsMillis = 300000;
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		// create pools map
		pools = new ConcurrentHashMap<ComponentID, SbbObjectPool>();
	}

	/**
	 * Retrieves the current pool configuration
	 * 
	 * @return
	 */
	public GenericObjectPool.Config getPoolConfig() {
		return config;
	}

	/**
	 * Defines the current pool configuration
	 */
	public void setPoolConfig(GenericObjectPool.Config config) {
		this.config = config;
	}

	/**
	 * get the ObjectPool for this Sbb. Each Sbb component has its own object
	 * pool. Each sbb component is identified by an SbbId.
	 * 
	 * @param sbbId -
	 *            sbb id for which object pool is required.
	 * @return the object pool for the sbb id.
	 * 
	 */
	public SbbObjectPool getObjectPool(ComponentID sbbID) {
		return pools.get(sbbID);
	}

	/**
	 * Creates an object pool for the sbb with the specified descriptor. If a
	 * transaction manager is used then, and if the tx rollbacks, the pool will
	 * be removed.
	 * 
	 * @param sbbDescriptor
	 * @param sleeTransactionManager
	 */
	public void createObjectPool(final MobicentsSbbDescriptor sbbDescriptor,
			final SleeTransactionManager sleeTransactionManager) {

		if (logger.isDebugEnabled()) {
			logger.debug("Creating Pool for SBB " + sbbDescriptor.getID());
		}

		createObjectPool(sbbDescriptor);

		if (sleeTransactionManager != null) {
			// add a rollback action to remove sbb object pool
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Due to tx rollback, removing pool for sbb "
										+ sbbDescriptor.getID());
					}
					try {
						removeObjectPool(sbbDescriptor.getID());
					} catch (Exception e) {
						logger.error("Failed to remove SBB "
								+ sbbDescriptor.getID() + " object pool", e);
					}
				}
			};
			sleeTransactionManager.addAfterRollbackAction(action);
		}
	}

	/**
	 * create the pool for the given SbbID
	 * 
	 * @param sbbDescriptor
	 */
	private void createObjectPool(final MobicentsSbbDescriptor sbbDescriptor) {
		// create the pool for the given SbbID
		GenericObjectPoolFactory poolFactory = new GenericObjectPoolFactory(
				new SbbObjectPoolFactory(sbbDescriptor), config);
		final ObjectPool objectPool = poolFactory.createPool();
		final SbbObjectPool oldObjectPool = pools.put(sbbDescriptor.getID(),
				new SbbObjectPool(objectPool));
		if (oldObjectPool != null) {
			// there was an old pool, close it
			try {
				oldObjectPool.close();
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to close old pool for SBB "
							+ sbbDescriptor.getID());
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Created Pool for SBB " + sbbDescriptor.getID());
		}
	}

	/**
	 * Removes the object pool for the sbb with the specified descriptor. If a
	 * transaction manager is used then, and if the tx rollbacks, the pool will
	 * be restored.
	 * 
	 * @param sbbDescriptor
	 * @param sleeTransactionManager
	 * @throws Exception
	 */
	public void removeObjectPool(final MobicentsSbbDescriptor sbbDescriptor,
			final SleeTransactionManager sleeTransactionManager)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing Pool for SBB " + sbbDescriptor.getID());
		}

		removeObjectPool(sbbDescriptor.getID());

		if (sleeTransactionManager != null) {
			// restore object pool if tx rollbacks
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Due to tx rollback, restoring pool for sbb "
										+ sbbDescriptor.getID());
					}
					createObjectPool(sbbDescriptor);
				}
			};
			sleeTransactionManager.addAfterRollbackAction(action);
		}
	}

	/**
	 * Removes the pool for the specified id
	 * 
	 * @param id
	 * @throws Exception
	 */
	private void removeObjectPool(ComponentID id) throws Exception {
		final SbbObjectPool objectPool = pools.remove(id);
		if (objectPool != null) {
			objectPool.close();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Removed Pool for SBB " + id);
		}
	}

	// ---- MBEAN METHODS

	public void register() {
		MBeanServer mBeanServer = sleeContainer.getMBeanServer();
		try {
			mBeanServer.registerMBean(this, new ObjectName(MBEAN_NAME));
		} catch (Exception e) {
			logger.error("Failed to register", e);
		}
	}

	public void unregister() {
		try {
			sleeContainer.getMBeanServer().unregisterMBean(
					new ObjectName(MBEAN_NAME));
		} catch (Exception e) {
			logger.error("Failed to unregister", e);
		}
	}

	public int getMaxActive() {
		return config.maxActive;
	}

	public void setMaxActive(int maxActive) {
		config.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return config.maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		config.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return config.minIdle;
	}

	public void setMinIdle(int minIdle) {
		config.minIdle = minIdle;
	}

	public long getMaxWait() {
		return config.maxWait;
	}

	public void setMaxWait(long maxWait) {
		config.maxWait = maxWait;
	}

	public long getMinEvictableIdleTimeMillis() {
		return config.minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		config.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return config.numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		config.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public boolean getTestOnBorrow() {
		return config.testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		config.testOnBorrow = testOnBorrow;
	}

	public boolean getTestOnReturn() {
		return config.testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		config.testOnReturn = testOnReturn;
	}

	public boolean getTestWhileIdle() {
		return config.testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		config.testWhileIdle = testWhileIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return config.timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		config.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public byte getWhenExhaustedAction() {
		return config.whenExhaustedAction;
	}

	public void setWhenExhaustedAction(byte whenExhaustedAction) {
		config.whenExhaustedAction = whenExhaustedAction;
	}

	public void reconfig() {
		for (Object key : pools.keySet().toArray()) {
			final MobicentsSbbDescriptor sbbDescriptor = sleeContainer
					.getSbbManagement().getSbbComponent((ComponentID) key);
			createObjectPool(sbbDescriptor);
		}
	}

	@Override
	public String toString() {
		return "SbbObject Pool Management: " + "\n+-- Pools: " + pools.keySet();
	}

}
