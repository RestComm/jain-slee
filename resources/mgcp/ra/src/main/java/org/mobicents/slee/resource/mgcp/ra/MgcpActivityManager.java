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

	protected MgcpConnectionActivityHandle putMgcpConnectionActivity(MgcpConnectionActivityImpl activity) {

		MgcpConnectionActivityHandle handle = activity.getActivityHandle();
		if (activity.getConnectionIdentifier() != null) {
			connectionIdentifier2ActivityHandleMap.put(activity.getConnectionIdentifier(), handle);
		} else if (activity.getTransactionHandle() != null) {
			transactionHandle2ActivityHandleMap.put(activity.getTransactionHandle(), handle);
		}
		connectionActivities.put(handle, activity);
		return handle;
	}

	protected MgcpConnectionActivityImpl getMgcpConnectionActivity(MgcpConnectionActivityHandle handle) {
		return connectionActivities.get(handle);
	}

	protected List<MgcpConnectionActivity> getMgcpConnectionActivities(EndpointIdentifier endpointIdentifier) {
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

	protected MgcpConnectionActivityHandle updateMgcpConnectionActivity(int transactionHandle,
			ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier) {
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

	protected MgcpConnectionActivityHandle getMgcpConnectionActivityHandle(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier, int transactionHandle) {
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

	protected MgcpEndpointActivityHandle getMgcpEndpointActivityHandle(EndpointIdentifier endpointIdentifier) {
		for (MgcpEndpointActivityHandle handle : endpointActivities.keySet()) {
			if (handle.getId().equals(endpointIdentifier.toString())) {
				return handle;
			}
		}
		return null;
	}

	protected void putMgcpEndpointActivity(MgcpEndpointActivityImpl activity) {
		endpointActivities.put(activity.getActivityHandle(), activity);
	}

	protected MgcpEndpointActivityImpl getMgcpEndpointActivity(MgcpEndpointActivityHandle handle) {
		return endpointActivities.get(handle);
	}

	protected boolean containsMgcpEndpointActivityHandle(MgcpEndpointActivityHandle handle) {
		return endpointActivities.containsKey(handle);
	}

	// --- COMMON

	protected void removeMgcpActivity(ActivityHandle handle) {
		if (handle instanceof MgcpConnectionActivityHandle) {
			MgcpConnectionActivityImpl activity = connectionActivities.remove((MgcpConnectionActivityHandle) handle);
			if (activity != null) {
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

	protected boolean containsActivityHandle(ActivityHandle handle) {

		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.containsKey((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.containsKey((MgcpEndpointActivityHandle) handle);
		} else {
			return false;
		}
	}

	protected Object getActivity(ActivityHandle handle) {
		if (handle instanceof MgcpConnectionActivityHandle) {
			return connectionActivities.get((MgcpConnectionActivityHandle) handle);

		} else if (handle instanceof MgcpEndpointActivityHandle) {
			return endpointActivities.get((MgcpEndpointActivityHandle) handle);
		} else {
			return null;
		}
	}

	protected ActivityHandle getActivityHandle(Object activity) {

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

	protected String getMgcpRaEntityName() {
		return mgcpRaEntityName;
	}

	protected void setMgcpRaEntityName(String mgcpRaEntityName) {
		this.mgcpRaEntityName = mgcpRaEntityName;
	}

}
