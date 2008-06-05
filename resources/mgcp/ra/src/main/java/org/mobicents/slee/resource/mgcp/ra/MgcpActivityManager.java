package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.resource.ActivityHandle;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import org.apache.log4j.Logger;

public class MgcpActivityManager {

	private static Logger logger = Logger.getLogger(MgcpActivityManager.class);

	private ConcurrentHashMap<MgcpEndpointActivityHandle, MgcpEndpointActivityImpl> endpointActivities = new ConcurrentHashMap<MgcpEndpointActivityHandle, MgcpEndpointActivityImpl>();

	private ConcurrentHashMap<MgcpConnectionActivityHandle, MgcpConnectionActivityImpl> connectionActivities = new ConcurrentHashMap<MgcpConnectionActivityHandle, MgcpConnectionActivityImpl>();
	private ConcurrentHashMap<Integer, MgcpConnectionActivityHandle> transactionHandle2ActivityHandleMap = new ConcurrentHashMap<Integer, MgcpConnectionActivityHandle>();
	private ConcurrentHashMap<String, MgcpConnectionActivityHandle> connectionIdentifier2ActivityHandleMap = new ConcurrentHashMap<String, MgcpConnectionActivityHandle>();

	protected MgcpActivityManager() {
	};

	// --------- CONNECTION ACTIVITY MANAGEMENT

	public MgcpConnectionActivityHandle putMgcpConnectionActivity(MgcpConnectionActivityImpl activity) {

		if (logger.isDebugEnabled()) {
			logger.debug("putMgcpConnectionActivity(activity=" + activity + ")");
		}

		MgcpConnectionActivityHandle handle = new MgcpConnectionActivityHandle(activity.getId());
		if (activity.getConnectionIdentifier() != null) {
			connectionIdentifier2ActivityHandleMap.put(activity.getConnectionIdentifier(), handle);
			if (logger.isDebugEnabled()) {
				logger.debug("created mapping between connection identifier " + activity.getConnectionIdentifier()
						+ " and activity handle " + handle + ")");
			}
		} else if (activity.getTransactionHandle() != null) {
			transactionHandle2ActivityHandleMap.put(activity.getTransactionHandle(), handle);
			if (logger.isDebugEnabled()) {
				logger.debug("created temp mapping between transaction handle " + activity.getTransactionHandle()
						+ " and activity handle " + handle + ")");
			}
		}
		connectionActivities.put(handle, activity);
		return handle;
	}

	public MgcpConnectionActivityImpl getMgcpConnectionActivity(MgcpConnectionActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMgcpConnectionActivity(handle=" + handle + ")");
		}

		return connectionActivities.get(handle);
	}

	public List<MgcpConnectionActivity> getMgcpConnectionActivities(EndpointIdentifier endpointIdentifier) {
		List<MgcpConnectionActivity> listOfmgcpConnectionActivities = Collections
				.synchronizedList(new ArrayList<MgcpConnectionActivity>());

		Collection<MgcpConnectionActivityImpl> connectionActivitiesColl = connectionActivities.values();

		for (MgcpConnectionActivityImpl mgcpConnectionActivityImpl : connectionActivitiesColl) {
			EndpointIdentifier endpointIdentifierLocal = mgcpConnectionActivityImpl.getEndpointIdentifier();
			
			if (endpointIdentifierLocal != null && endpointIdentifierLocal.toString().equals(endpointIdentifier.toString())) {
				listOfmgcpConnectionActivities.add(mgcpConnectionActivityImpl);
			}
		}
		return listOfmgcpConnectionActivities;

	}

	public MgcpConnectionActivityHandle updateMgcpConnectionActivity(int transactionHandle,
			ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier) {

		if (logger.isDebugEnabled()) {
			logger.debug("updateMgcpConnectionActivity(transactionHandle=" + transactionHandle
					+ ",connectionIdentifier=" + connectionIdentifier + ")");
		}

		if (connectionIdentifier != null) {
			ActivityHandle handle = transactionHandle2ActivityHandleMap.remove(Integer.valueOf(transactionHandle));
			if (handle != null) {
				// confirm activity exists
				MgcpConnectionActivityImpl activity = connectionActivities.get(handle);
				if (activity == null) {
					logger.warn("update of MgcpConnectionActivity failed, activity for connectionIdentifier "
							+ connectionIdentifier + " not found");
				} else {
					MgcpConnectionActivityHandle connectionHandle = (MgcpConnectionActivityHandle) handle;
					// move handler from tx handler map to connection id map
					connectionIdentifier2ActivityHandleMap.put(connectionIdentifier.toString(), connectionHandle);
					// update activity
					activity.setConnectionIdentifier(connectionIdentifier);
					if (endpointIdentifier != null) {
						activity.setEndpointIdentifier(endpointIdentifier);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("activity for connectionIdentifier " + connectionIdentifier + " updated");
					}
					return connectionHandle;
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("update of MgcpConnectionActivity failed, transactionHandle " + transactionHandle
							+ " not found");
				}
			}
		} else {
			logger.warn("update of MgcpConnectionActivity failed, connectionIdentifier is null");
		}
		return null;
	}

	public MgcpConnectionActivityHandle getMgcpConnectionActivityHandle(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier, int transactionHandle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMgcpConnectionActivityHandle(transactionHandle=" + transactionHandle
					+ ",connectionIdentifier=" + connectionIdentifier + ")");
		}

		if (connectionIdentifier != null) {
			// get handle from connection id map
			MgcpConnectionActivityHandle handle = connectionIdentifier2ActivityHandleMap.get(connectionIdentifier
					.toString());
			// if handle does not exist try update first the activity
			return (handle != null ? handle : updateMgcpConnectionActivity(transactionHandle, connectionIdentifier,
					endpointIdentifier));
		} else {
			return transactionHandle2ActivityHandleMap.get(Integer.valueOf(transactionHandle));
		}
	}

	// --------- ENDPOINT ACTIVITY MANAGEMENT

	public void putMgcpEndpointActivity(MgcpEndpointActivityHandle handle, MgcpEndpointActivityImpl activity) {

		if (logger.isDebugEnabled()) {
			logger.debug("putMgcpEndpointActivity(activity=" + activity + ")");
		}

		endpointActivities.put(handle, activity);
	}

	public MgcpEndpointActivityImpl getMgcpEndpointActivity(MgcpEndpointActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMgcpEndpointActivity(handle=" + handle + ")");
		}

		return endpointActivities.get(handle);
	}

	public boolean containsMgcpEndpointActivityHandle(MgcpEndpointActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("containsMgcpEndpointActivityHandle(handle=" + handle + ")");
		}

		return endpointActivities.containsKey(handle);
	}

	// --- COMMON

	public void removeMgcpActivity(ActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("removeMgcpActivity(handle=" + handle + ")");
		}

		if (handle instanceof MgcpConnectionActivityHandle) {
			MgcpConnectionActivityImpl activity = connectionActivities.remove((MgcpConnectionActivityHandle) handle);
			if (activity != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed connection activity for handle " + handle);
				}
				if (activity.getConnectionIdentifier() != null) {
					if (connectionIdentifier2ActivityHandleMap.remove(activity.getConnectionIdentifier()) != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("removed connection identifier mapping for handle " + handle);
						}
					}
				} else {
					if (transactionHandle2ActivityHandleMap.remove(activity.getTransactionHandle()) != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("removed tx handle mapping for activity handle " + handle);
						}
					}
				}
			} else {
				logger.warn("connection activity for handle " + handle + " not found");
			}

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			MgcpEndpointActivityImpl activity = endpointActivities.remove((MgcpEndpointActivityHandle) handle);
			if (activity != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed endpoint activity for handle " + handle);
				}
			} else {
				logger.warn("endpoint activity for handle " + handle + " not found");
			}
		}
	}

	public boolean containsActivityHandle(ActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("containsActivityHandle(handle=" + handle + ")");
		}

		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.containsKey((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.containsKey((MgcpEndpointActivityHandle) handle);
		} else {
			return false;
		}
	}

	public Object getActivity(ActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getActivity(handle=" + handle + ")");
		}

		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.get((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.get((MgcpEndpointActivityHandle) handle);
		} else {
			return null;
		}
	}

	public ActivityHandle getActivityHandle(Object activity) {

		if (logger.isDebugEnabled()) {
			logger.debug("getActivityHandle(activity=" + activity + ")");
		}

		if (activity instanceof MgcpConnectionActivityImpl) {
			MgcpConnectionActivityImpl castedActivity = (MgcpConnectionActivityImpl) activity;
			if (castedActivity.getConnectionIdentifier() != null) {
				return connectionIdentifier2ActivityHandleMap.get(castedActivity.getConnectionIdentifier());
			} else {
				return transactionHandle2ActivityHandleMap.get(castedActivity.getTransactionHandle());
			}
		} else if (activity instanceof MgcpEndpointActivityImpl) {
			MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(((MgcpEndpointActivityImpl) activity)
					.getEndpointIdentifier().toString());
			if (endpointActivities.containsKey(handle)) {
				return handle;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
