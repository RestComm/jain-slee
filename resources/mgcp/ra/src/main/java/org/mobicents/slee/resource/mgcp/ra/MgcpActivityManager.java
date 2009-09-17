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

/**
 * 
 * @author amit bhayani
 * 
 */
public class MgcpActivityManager {

	private static Logger logger = Logger.getLogger(MgcpActivityManager.class);

	private String mgcpRaEntityName = null;


	private ConcurrentHashMap<MgcpEndpointActivityHandle, MgcpEndpointActivityImpl> endpointActivities = new ConcurrentHashMap<MgcpEndpointActivityHandle, MgcpEndpointActivityImpl>();

	private ConcurrentHashMap<MgcpConnectionActivityHandle, MgcpConnectionActivityImpl> connectionActivities = new ConcurrentHashMap<MgcpConnectionActivityHandle, MgcpConnectionActivityImpl>();
	private ConcurrentHashMap<Integer, MgcpConnectionActivityHandle> transactionHandle2ActivityHandleMap = new ConcurrentHashMap<Integer, MgcpConnectionActivityHandle>();
	private ConcurrentHashMap<String, MgcpConnectionActivityHandle> connectionIdentifier2ActivityHandleMap = new ConcurrentHashMap<String, MgcpConnectionActivityHandle>();

	protected MgcpActivityManager() {
	}

	// --------- CONNECTION ACTIVITY MANAGEMENT

	public MgcpConnectionActivityHandle putMgcpConnectionActivity(MgcpConnectionActivityImpl activity) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// putMgcpConnectionActivity(activity=" + activity + ")");
		// }

		MgcpConnectionActivityHandle handle = activity.getActivityHandle();
		if (activity.getConnectionIdentifier() != null) {
			connectionIdentifier2ActivityHandleMap.put(activity.getConnectionIdentifier(), handle);
			// if (logger.isDebugEnabled()) {
			// logger.debug(this.getMgcpRaEntityName()+" : created mapping
			// between connection identifier " +
			// activity.getConnectionIdentifier()
			// + " and activity handle " + handle + ")");
			// }
		} else if (activity.getTransactionHandle() != null) {
			transactionHandle2ActivityHandleMap.put(activity.getTransactionHandle(), handle);
			// if (logger.isDebugEnabled()) {
			// logger.debug(this.getMgcpRaEntityName()+" : created temp mapping
			// between transaction handle " + activity.getTransactionHandle()
			// + " and activity handle " + handle + ")");
			// }
		}
		connectionActivities.put(handle, activity);
		return handle;
	}

	public MgcpConnectionActivityImpl getMgcpConnectionActivity(MgcpConnectionActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// getMgcpConnectionActivity(handle=" + handle + ")");
		// }

		return connectionActivities.get(handle);
	}

	public List<MgcpConnectionActivity> getMgcpConnectionActivities(EndpointIdentifier endpointIdentifier) {
		List<MgcpConnectionActivity> listOfmgcpConnectionActivities = Collections
				.synchronizedList(new ArrayList<MgcpConnectionActivity>());

		Collection<MgcpConnectionActivityImpl> connectionActivitiesColl = connectionActivities.values();

		for (MgcpConnectionActivityImpl mgcpConnectionActivityImpl : connectionActivitiesColl) {
			EndpointIdentifier endpointIdentifierLocal = mgcpConnectionActivityImpl.getEndpointIdentifier();

			if (endpointIdentifierLocal != null
					&& endpointIdentifierLocal.toString().equals(endpointIdentifier.toString())) {
				listOfmgcpConnectionActivities.add(mgcpConnectionActivityImpl);
			}
		}
		return listOfmgcpConnectionActivities;

	}

	public MgcpConnectionActivityHandle updateMgcpConnectionActivity(int transactionHandle,
			ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// updateMgcpConnectionActivity(transactionHandle=" + transactionHandle
		// + ",connectionIdentifier=" + connectionIdentifier + ")");
		// }

		if (connectionIdentifier != null) {
			ActivityHandle handle = transactionHandle2ActivityHandleMap.remove(Integer.valueOf(transactionHandle));
			if (handle != null) {
				// confirm activity exists
				MgcpConnectionActivityImpl activity = connectionActivities.get(handle);
				if (activity == null) {
					logger.warn(this.getMgcpRaEntityName()
							+ " : update of MgcpConnectionActivity failed, activity for connectionIdentifier "
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
					// if (logger.isDebugEnabled()) {
					// logger.debug(this.getMgcpRaEntityName()+" : activity for
					// connectionIdentifier " + connectionIdentifier + "
					// updated");
					// }
					return connectionHandle;
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(this.getMgcpRaEntityName()
							+ " : update of MgcpConnectionActivity failed, transactionHandle " + transactionHandle
							+ " not found");
				}
			}
		} else {
			logger.warn(this.getMgcpRaEntityName()
					+ " : update of MgcpConnectionActivity failed, connectionIdentifier is null");
		}
		return null;
	}

	public MgcpConnectionActivityHandle getMgcpConnectionActivityHandle(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier, int transactionHandle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// getMgcpConnectionActivityHandle(transactionHandle=" +
		// transactionHandle
		// + ",connectionIdentifier=" + connectionIdentifier + ")");
		// }

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

	// public void putMgcpEndpointActivity(MgcpEndpointActivityHandle handle,
	// MgcpEndpointActivityImpl activity) {
	public void putMgcpEndpointActivity(MgcpEndpointActivityImpl activity) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// putMgcpEndpointActivity(activity=" + activity + ")");
		// }

		endpointActivities.put(activity.getActivityHandle(), activity);
	}

	public MgcpEndpointActivityImpl getMgcpEndpointActivity(MgcpEndpointActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// getMgcpEndpointActivity(handle=" + handle + ")");
		// }

		return endpointActivities.get(handle);
	}

	public boolean containsMgcpEndpointActivityHandle(MgcpEndpointActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// containsMgcpEndpointActivityHandle(handle=" + handle + ")");
		// }

		return endpointActivities.containsKey(handle);
	}

	// --- COMMON

	public void removeMgcpActivity(ActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// removeMgcpActivity(handle=" + handle + ")");
		// }

		if (handle instanceof MgcpConnectionActivityHandle) {
			MgcpConnectionActivityImpl activity = connectionActivities.remove((MgcpConnectionActivityHandle) handle);
			if (activity != null) {
				// if (logger.isDebugEnabled()) {
				// logger.debug(this.getMgcpRaEntityName()+" : removed
				// connection activity for handle " + handle);
				// }
				if (activity.getConnectionIdentifier() != null) {
					if (connectionIdentifier2ActivityHandleMap.remove(activity.getConnectionIdentifier()) != null) {
						if (logger.isDebugEnabled()) {
							logger.debug(this.getMgcpRaEntityName()
									+ " : removed connection identifier mapping for handle " + handle);
						}
					}
				} else {
					if (transactionHandle2ActivityHandleMap.remove(activity.getTransactionHandle()) != null) {
						if (logger.isDebugEnabled()) {
							logger.debug(this.getMgcpRaEntityName()
									+ " : removed tx handle mapping for activity handle " + handle);
						}
					}
				}
			} else {
				logger.warn(this.getMgcpRaEntityName() + " : connection activity for handle " + handle + " not found");
			}

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			MgcpEndpointActivityImpl activity = endpointActivities.remove((MgcpEndpointActivityHandle) handle);
			if (activity != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(this.getMgcpRaEntityName() + " : removed endpoint activity for handle " + handle);
				}
			} else {
				logger.warn(this.getMgcpRaEntityName() + " : endpoint activity for handle " + handle + " not found");
			}
		}
	}

	public boolean containsActivityHandle(ActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// containsActivityHandle(handle=" + handle + ")");
		// }

		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.containsKey((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.containsKey((MgcpEndpointActivityHandle) handle);
		} else {
			return false;
		}
	}

	public Object getActivity(ActivityHandle handle) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" : getActivity(handle=" +
		// handle + ")");
		// }

		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.get((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.get((MgcpEndpointActivityHandle) handle);
		} else {
			return null;
		}
	}

	public ActivityHandle getActivityHandle(Object activity) {

		// if (logger.isDebugEnabled()) {
		// logger.debug(this.getMgcpRaEntityName()+" :
		// getActivityHandle(activity=" + activity + ")");
		// }

		if (activity instanceof MgcpConnectionActivityImpl) {
			MgcpConnectionActivityImpl castedActivity = (MgcpConnectionActivityImpl) activity;
			return castedActivity.getActivityHandle();
		} else if (activity instanceof MgcpEndpointActivityImpl) {
			MgcpEndpointActivityHandle handle = ((MgcpEndpointActivityImpl) activity).getActivityHandle();
			if (endpointActivities.containsKey(handle)) {
				return handle;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String getMgcpRaEntityName() {
		return mgcpRaEntityName;
	}

	public void setMgcpRaEntityName(String mgcpRaEntityName) {
		this.mgcpRaEntityName = mgcpRaEntityName;
	}

}
