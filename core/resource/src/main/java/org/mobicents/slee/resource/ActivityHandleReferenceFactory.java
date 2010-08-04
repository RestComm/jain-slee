package org.mobicents.slee.resource;

import java.rmi.dgc.VMID;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jgroups.Address;
import org.mobicents.cache.MobicentsCache;
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
	@SuppressWarnings("unchecked")
	private final Fqn baseFqn;
	
	private final static Logger logger = Logger.getLogger(ActivityHandleReferenceFactory.class);
	private static boolean doTraceLogs = logger.isTraceEnabled();
	
	private ClusteredCacheData clusteredCacheData;
	private CacheData localCacheData;
	
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
		localCacheData = new CacheData(baseFqn, resourceManagement.getSleeContainer().getLocalCache());
		localCacheData.create();
	}
	
	public void remove() {
		if (doTraceLogs) {
			logger.trace("remove()");
		}
		clusteredCacheData.remove();
		clusteredCacheData = null;
		resourceManagement.getSleeContainer().getCluster().removeFailOverListener(failOverListener);
		localCacheData.remove();
		localCacheData = null;
	}
	
	private final ConcurrentHashMap<ActivityHandle, String> pendingIds = new ConcurrentHashMap<ActivityHandle, String>();
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public ActivityHandleReference createActivityHandleReference(
			final ActivityHandle handle) {
		
		if (doTraceLogs) {
			logger.trace("createActivityHandleReference( handle = "+handle+" )");
		}
		
		ActivityHandleReference reference = localCacheData.getReference(handle);
		if (reference != null) {
			throw new ActivityAlreadyExistsException(handle.toString());
		}
		
		// a ref to generated ids are kept while tx exists, so concurrent invocations use the same id
		String id = pendingIds.get(handle);
		if (id == null) {
			id = new VMID().toString();
			final TransactionContext txContext = resourceManagement.getSleeContainer().getTransactionManager().getTransactionContext();
			if (txContext != null) {
				final String otherId = pendingIds.putIfAbsent(handle, id);
				if (otherId != null) {
					id = otherId;
				}
				else {
					// put suceed, add tx actions to clean pending id mapping
					final TransactionalAction action = new TransactionalAction() {
						public void execute() {
							pendingIds.remove(handle);
						}
					};
					txContext.getAfterCommitActions().add(action);
					txContext.getAfterRollbackActions().add(action);
				}				
			}
		}
		
		reference = new ActivityHandleReference(handle, resourceManagement.getSleeContainer().getCluster().getLocalAddress(),id);

		localCacheData.link(handle, reference);
			
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
	public ActivityHandle getActivityHandle(ActivityHandleReference reference) {
		
		if (doTraceLogs) {
			logger.trace("getActivityHandle( reference = "+reference+" )");
		}
		
		ActivityHandle ah = null;//reference.getReference();
		if (ah == null) {
			ah = localCacheData.getActivityHandle(reference);
		}
		return ah;
	}
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public ActivityHandle getReferenceTransacted(ActivityHandle handle) {
		
		if (doTraceLogs) {
			logger.trace("getReferenceTransacted( handle = "+handle+" )");
		}
		
		ActivityHandle ah = localCacheData.getReference(handle);
		if (ah == null) {
			ah = handle;
		}
		return ah;
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
		
		final SleeTransactionManager txManager = resourceManagement.getSleeContainer().getTransactionManager();
		Transaction tx = null;
		try {
			tx = txManager.getTransaction();
			if (tx != null) {
				txManager.suspend();
			}
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		
		ActivityHandle ah = localCacheData.getReference(handle);
		
		if (tx != null) {
			try {
				txManager.resume(tx);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		
		if (ah == null) {
			ah = handle;
		}
		return ah;
	}
	
	/**
	 * 
	 * @param reference
	 */
	public ActivityHandle removeActivityHandleReference(ActivityHandleReference reference) {
		
		if (doTraceLogs) {
			logger.trace("removeActivityHandleReference( reference = "+reference+" )");
		}
		
		final ActivityHandle handle = getActivityHandle(reference);
		if (handle != null) {
			localCacheData.unlink(handle, reference);
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
		
		@SuppressWarnings("unchecked")
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
	private static class CacheData extends
			org.mobicents.cache.CacheData {

		public CacheData(Fqn nodeFqn, MobicentsCache localCache) {
			super(nodeFqn, localCache);
		}

		private static final String DATA_KEY = "link";
		
		public void link(ActivityHandle handle,ActivityHandleReference reference) {
			Node node = getNode();
			node.addChild(Fqn.fromElements(handle)).put(DATA_KEY, reference);
			node.addChild(Fqn.fromElements(reference.getId())).put(DATA_KEY, handle);
		}
		
		public ActivityHandle getActivityHandle(ActivityHandleReference reference) {
			Node child = getNode().getChild(reference.getId());
			return child == null ? null : (ActivityHandle) child.get(DATA_KEY);
		}
		
		public ActivityHandleReference getReference(ActivityHandle handle) {
			Node child = getNode().getChild(handle);
			return child == null ? null : (ActivityHandleReference) child.get(DATA_KEY);
		}
		
		public void unlink(ActivityHandle handle,ActivityHandleReference reference) {
			Node node = getNode();
			node.removeChild(reference.getId());
			node.removeChild(handle);
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
		@SuppressWarnings("unchecked")
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
		return "ActivityHandleReferenceFactory[pendingIds = "+pendingIds.keySet()+"]";
	}
}
