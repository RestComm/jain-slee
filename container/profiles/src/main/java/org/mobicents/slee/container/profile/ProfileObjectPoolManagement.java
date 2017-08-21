/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.profile;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.SLEEException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * 
 * Manages profile object pools in the SLEE container.
 * 
 * @author martins
 * 
 */
public class ProfileObjectPoolManagement implements ProfileObjectPoolManagementMBean {

	private final static Logger logger = Logger
			.getLogger(ProfileObjectPoolManagement.class);

	private final ConcurrentHashMap<String, ProfileObjectPool> pools;
	private final SleeContainer sleeContainer;

	private GenericObjectPool.Config config;

	public ProfileObjectPoolManagement(SleeContainer sleeContainer) {
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
		pools = new ConcurrentHashMap<String, ProfileObjectPool>();
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
	 * Retrieves the object pool for the specified profile table.
	 * @param profileTable
	 * @return
	 */
	public ProfileObjectPool getObjectPool(String profileTableName) {
		return pools.get(profileTableName);
	}

	/**
	 * Creates an object pool for the specified profile table. If a
	 * transaction manager is used then, and if the tx rollbacks, the pool will
	 * be removed.
	 * 
	 * @param 
	 * @param sleeTransactionManager
	 */
	public void createObjectPool(final ProfileTableImpl profileTable,
			final SleeTransactionManager sleeTransactionManager) {

		if (logger.isTraceEnabled()) {
            logger.trace("Creating Pool for " + profileTable);
		}

		createObjectPool(profileTable);

		if (sleeTransactionManager != null) {
			// add a rollback action to remove sbb object pool
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Due to tx rollback, removing pool for " + profileTable);
					}
					try {
						removeObjectPool(profileTable);
					} catch (Throwable e) {
						logger.error("Failed to remove table's " + profileTable + " object pool", e);
					}
				}
			};
			sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);			
		}
	}

	/**
	 * 
	 * @param profileTable
	 */
	private void createObjectPool(final ProfileTableImpl profileTable) {
		// create the pool
		GenericObjectPoolFactory poolFactory = new GenericObjectPoolFactory(
				new ProfileObjectPoolFactory(profileTable), config);
		final ObjectPool objectPool = poolFactory.createPool();
		final ProfileObjectPool oldObjectPool = pools.put(profileTable.getProfileTableName(),
				new ProfileObjectPool(objectPool));
		if (oldObjectPool != null) {
			// there was an old pool, close it
			try {
				oldObjectPool.close();
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to close old pool for " + profileTable);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Created Pool for " + profileTable);
		}
	}

	/**
	 * Removes the object pool for the specified profile table. If a
	 * transaction manager is used then the removal is only after the tx commit.
	 * 
	 * @param sleeTransactionManager
	 * @throws Exception
	 */
	public void removeObjectPool(final ProfileTableImpl profileTable,
			final SleeTransactionManager sleeTransactionManager) {

		
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				if (logger.isTraceEnabled()) {
		            logger.trace("Removing Pool for " + profileTable);
				}

				removeObjectPool(profileTable);
			}
		};
		
		if (sleeTransactionManager != null) {
			sleeTransactionManager.getTransactionContext().getAfterCommitActions().add(action);			
		}
		else {
			action.execute();
		}
	}

	/**
	 * Removes the pool for the specified ids
	 * 
	 * @param serviceID
	 * @param sbbID
	 * @throws Exception
	 */
	private void removeObjectPool(final ProfileTableImpl profileTable) {
		final ProfileObjectPool objectPool = pools.remove(profileTable.getProfileTableName());
		if (objectPool != null) {
			try {
				objectPool.close();
			} catch (Exception e) {
				logger.error("failed to close pool",e);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Removed Pool for " + profileTable);
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
		for (String key : pools.keySet()) {
			ProfileTableImpl profileTable;
			try {
				profileTable = (ProfileTableImpl) sleeContainer.getSleeProfileTableManager().getProfileTable(key);
			} catch (UnrecognizedProfileTableNameException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			createObjectPool(profileTable);
		}
	}

	@Override
	public String toString() {
		return "Profile Object Pool Management: " 
			+ "\n+-- Pools: " + pools.keySet();
	}

}
