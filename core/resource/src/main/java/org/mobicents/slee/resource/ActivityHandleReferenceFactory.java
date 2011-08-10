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

package org.mobicents.slee.resource;

import java.rmi.dgc.VMID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jgroups.Address;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.cluster.election.ClientLocalListenerElector;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * Manages the creation and removal of {@link ActivityHandleReference}s, which
 * are used in cluster environments to work around the fact that an activity
 * handle may not be replicated. In case of cluster node fail, another node
 * factory will end the lost activities.
 * 
 * @author martins
 * 
 */
public class ActivityHandleReferenceFactory {

	/**
	 * 
	 */
	private final ResourceManagementImpl resourceManagement;

	/**
	 * 
	 */
	private final FailOverListener failOverListener = new FailOverListener();
	
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private final Fqn baseFqn;
	
	private final static Logger logger = Logger.getLogger(ActivityHandleReferenceFactory.class);
	private static boolean doTraceLogs = logger.isTraceEnabled();
	
	private ClusteredCacheData clusteredCacheData;
	
	private SleeTransactionManager _txManager;
	private Address _localAddress;
	
	/**
	 * 
	 * @param raEntity
	 */
	public ActivityHandleReferenceFactory(ResourceManagementImpl resourceManagement) {
		this.resourceManagement = resourceManagement;
		this.baseFqn = Fqn.fromElements("ra-entity-handle-refs");
	}

	public void init() {
		if (doTraceLogs) {
			logger.trace("init()");
		}
		// lets create this cluster member cache data object with failover
		final MobicentsCluster cluster = resourceManagement.getSleeContainer().getCluster();
		clusteredCacheData = new ClusteredCacheData(baseFqn,cluster);
		clusteredCacheData.create();
		cluster.addFailOverListener(failOverListener);
	}
	
	public void remove() {
		if (doTraceLogs) {
			logger.trace("remove()");
		}
		clusteredCacheData.remove();
		clusteredCacheData = null;
		resourceManagement.getSleeContainer().getCluster().removeFailOverListener(failOverListener);
	}
	
	public Address getLocalAddress() {
		if (_localAddress == null) {
			_localAddress = resourceManagement.getSleeContainer().getCluster().getLocalAddress();
		}
		return _localAddress;
	}
	
	public SleeTransactionManager getTxManager() {
		if (_txManager == null) {
			_txManager = resourceManagement.getSleeContainer().getTransactionManager();
		}
		return _txManager;
	}
	
	private final ConcurrentHashMap<ActivityHandle, PendingId> pendingIds = new ConcurrentHashMap<ActivityHandle, PendingId>();
	private final ConcurrentHashMap<ActivityHandle, ActivityHandleReference> handle2ref = new ConcurrentHashMap<ActivityHandle, ActivityHandleReference>();
	private final ConcurrentHashMap<ActivityHandleReference, ActivityHandle> ref2handle = new ConcurrentHashMap<ActivityHandleReference, ActivityHandle>();
	
	private static class PendingId {
		
		private final String id;
		private final AtomicInteger txs = new AtomicInteger(1);
		
		public PendingId(String id) {
			this.id = id;
		}
		
	}
	
	private static class Handle2RefTxData {
		
		private final ActivityHandle handle;
		private final ActivityHandleReference ref;
		
		public Handle2RefTxData(ActivityHandle handle,
				ActivityHandleReference ref) {
			this.handle = handle;
			this.ref = ref;
		}
		
		@Override
		public int hashCode() {
			return handle.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Handle2RefTxData other = (Handle2RefTxData) obj;
			return handle.equals(other.handle);
		}		
		
	}

	/**
	 * 
	 * @param handle
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActivityHandleReference createActivityHandleReference(
			final ActivityHandle handle) {
		
		if (doTraceLogs) {
			logger.trace("createActivityHandleReference( handle = "+handle+" )");
		}
		
		ActivityHandleReference reference = handle2ref.get(handle);
		if (reference != null) {
			throw new ActivityAlreadyExistsException(handle.toString());
		}
		
		String id = null;
		final TransactionContext txContext = getTxManager().getTransactionContext();
		if (txContext != null) {
			// check 1st in the tx
			final Map txData = txContext.getData();
			Handle2RefTxData handle2RefTxData = (Handle2RefTxData) txData.get(handle);
			if (handle2RefTxData != null) {
				// already exists in tx
				throw new ActivityAlreadyExistsException(handle.toString());
			}
			else {
				id = new VMID().toString();
				// a ref to generated ids are kept while tx exists, so concurrent invocations use the same id
				final PendingId otherPendingId = pendingIds.putIfAbsent(handle, new PendingId(id));
				if (otherPendingId != null) {
					// concurrent tx creating, must allow, it may rollback, reuse id 
					otherPendingId.txs.incrementAndGet();
					id = otherPendingId.id;
				}
				// create ref and add to tx
				reference = new ActivityHandleReference(handle, getLocalAddress(), id);
				final Handle2RefTxData newHandle2RefTxData = new Handle2RefTxData(handle, reference);
				txData.put(handle, newHandle2RefTxData);
				txData.put(reference, newHandle2RefTxData);
				// add tx actions
				final TransactionalAction rollbackAction = new TransactionalAction() {
					public void execute() {
						if (logger.isDebugEnabled()) {
							logger.debug("Rollback of activity handle reference creation, for activity handle "+handle);
						}
						PendingId pendingId = pendingIds.get(handle);
						if (pendingId.txs.decrementAndGet() < 1) {
							pendingIds.remove(handle);
						}
					}
				};
				final TransactionalAction commitAction = new TransactionalAction() {
					public void execute() {
						PendingId pendingId = pendingIds.get(handle);
						if (pendingId.txs.decrementAndGet() < 1) {
							pendingIds.remove(handle);
						}
						handle2ref.put(handle, newHandle2RefTxData.ref);
						ref2handle.put(newHandle2RefTxData.ref, handle);
					}
				};
				txContext.getAfterCommitActions().add(commitAction);
				txContext.getAfterRollbackActions().add(rollbackAction);
			}
		}
		else {
			reference = new ActivityHandleReference(handle, getLocalAddress(), new VMID().toString());
			handle2ref.put(handle, reference);
			ref2handle.put(reference, handle);
		}
				
		if (logger.isDebugEnabled()) {
			logger.debug("Created activity handle reference "+reference+" for activity handle "+reference.getReference());
		}
		
		return reference;
	}
	
	
	/**
	 * 
	 * @param reference
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ActivityHandle getActivityHandle(ActivityHandleReference reference) {
		
		if (doTraceLogs) {
			logger.trace("getActivityHandle( reference = "+reference+" )");
		}
		
		ActivityHandle ah = reference.getReference();
		if (ah == null) {
			ah = ref2handle.get(reference);
			if (ah != null) {
				reference.setReference(ah);
			}
			else {
				// if in tx context it may be in tx data only, due to creation
				final TransactionContext txContext = getTxManager().getTransactionContext();
				if (txContext != null) {
					final Map txData = txContext.getData();
					Handle2RefTxData handle2RefTxData = (Handle2RefTxData) txData.get(reference);
					if (handle2RefTxData != null) {
						ah = handle2RefTxData.handle;
						reference.setReference(ah);						
					}
				}
			}
		}
		return ah;
	}
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ActivityHandle getReferenceTransacted(ActivityHandle handle) {
		
		if (doTraceLogs) {
			logger.trace("getReferenceTransacted( handle = "+handle+" )");
		}
		
		ActivityHandle reference = handle2ref.get(handle);
		if (reference == null) {
			// if in tx context it may be in tx data only, due to creation
			final TransactionContext txContext = getTxManager().getTransactionContext();
			if (txContext != null) {
				final Map txData = txContext.getData();
				Handle2RefTxData handle2RefTxData = (Handle2RefTxData) txData.get(handle);
				if (handle2RefTxData != null) {
					reference = handle2RefTxData.ref;
				}
			}
		}
		
		if (reference == null) {
			reference = handle;
		}
		return reference;
	}
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public ActivityHandle getReference(ActivityHandle handle) {
		
		if (doTraceLogs) {
			logger.trace("getReference( handle = "+handle+" )");
		}
		
		ActivityHandle reference = handle2ref.get(handle);
		if (reference == null) {
			reference = handle;
		}
		return reference;
	}
	
	/**
	 * 
	 * @param reference
	 */
	public ActivityHandle removeActivityHandleReference(final ActivityHandleReference reference) {
		
		if (doTraceLogs) {
			logger.trace("removeActivityHandleReference( reference = "+reference+" )");
		}
		
		ActivityHandle handle = getActivityHandle(reference);
		if (handle != null) {
			// if in tx we need to remove only on commit
			final TransactionContext txContext = getTxManager().getTransactionContext();
			if (txContext != null) {
				TransactionalAction action = new TransactionalAction() {
					@Override
					public void execute() {
						final ActivityHandle handle = ref2handle.remove(reference);
						if (handle != null) {
							handle2ref.remove(handle);
						}						
					}
				};
				txContext.getAfterCommitActions().add(action);
			}
			else {
				ref2handle.remove(reference);
				handle2ref.remove(handle);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Removed activity handle reference "+reference);
			}
		}
		return handle;
	}

	/**
	 * 
	 * @author martins
	 * 
	 */
	private static class ClusteredCacheData extends
			org.mobicents.cluster.cache.ClusteredCacheData {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public ClusteredCacheData(Fqn baseFqn,
				MobicentsCluster cluster) {
			super(Fqn.fromRelativeElements(baseFqn, cluster.getLocalAddress()), cluster);
		}
	}
	
	/**
	 * 
	 * @author martins
	 * 
	 */
	private class FailOverListener implements
			org.mobicents.cluster.FailOverListener {

		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.mobicents.cluster.FailOverListener#failOverClusterMember(org.
		 * jgroups.Address)
		 */
		public void failOverClusterMember(Address arg0) {
			// lets search and end all ACs that were local to the node which failed
			final ActivityContextFactory acFactory = resourceManagement.getSleeContainer()
			.getActivityContextFactory();
			// ouch, this is going to be expensive
			for (ActivityContextHandle ach : acFactory.getAllActivityContextsHandles()) {
				if (ach.getActivityType() == ActivityType.RA) {
					final ResourceAdaptorActivityContextHandleImpl raach = (ResourceAdaptorActivityContextHandleImpl) ach;
					if (raach.getActivityHandle().getClass() == ActivityHandleReference.class) {
						final ActivityHandleReference reference = (ActivityHandleReference) raach.getActivityHandle();
						if (reference.getAddress().equals(arg0)) {
							final ActivityContext ac = acFactory.getActivityContext(raach);
							if (ac != null) {
								ac.endActivity();
							}
						}
					}
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.cluster.FailOverListener#getBaseFqn()
		 */
		@SuppressWarnings("rawtypes")
		public Fqn getBaseFqn() {
			return baseFqn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.cluster.FailOverListener#getElector()
		 */
		public ClientLocalListenerElector getElector() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.cluster.FailOverListener#getPriority()
		 */
		public byte getPriority() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.mobicents.cluster.FailOverListener#lostOwnership(org.mobicents
		 * .cluster.cache.ClusteredCacheData)
		 */
		public void lostOwnership(
				org.mobicents.cluster.cache.ClusteredCacheData arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.mobicents.cluster.FailOverListener#wonOwnership(org.mobicents
		 * .cluster.cache.ClusteredCacheData)
		 */
		public void wonOwnership(
				org.mobicents.cluster.cache.ClusteredCacheData arg0) {
			
		}

	}
	
	@Override
	public String toString() {
		return "ActivityHandleReferenceFactory[ pendingIds = "+pendingIds.keySet()+", handles = "+handle2ref.keySet()+", refs = "+ref2handle.keySet()+" ]";
	}
}
