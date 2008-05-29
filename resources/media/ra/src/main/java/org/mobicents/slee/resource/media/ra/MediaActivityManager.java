package org.mobicents.slee.resource.media.ra;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsResource;
import org.mobicents.mscontrol.MsSession;

public class MediaActivityManager {

	private static Logger logger = Logger.getLogger(MediaActivityManager.class);

	private ConcurrentHashMap<MsConnectionActivityHandle, MsConnection> msConnectionActivities = new ConcurrentHashMap<MsConnectionActivityHandle, MsConnection>();

	private ConcurrentHashMap<MsLinkActivityHandle, MsLink> msLinkActivities = new ConcurrentHashMap<MsLinkActivityHandle, MsLink>();

	private ConcurrentHashMap<MsSessionActivityHandle, MsSession> msSessionActivities = new ConcurrentHashMap<MsSessionActivityHandle, MsSession>();

	private ConcurrentHashMap<MsResourceActivityHandle, MsResource> msResourceActivities = new ConcurrentHashMap<MsResourceActivityHandle, MsResource>();

	private ConcurrentHashMap<String, MsConnectionActivityHandle> msConnectionId2ActivityHandleMap = new ConcurrentHashMap<String, MsConnectionActivityHandle>();

	private ConcurrentHashMap<String, MsLinkActivityHandle> msLinkId2ActivityHandleMap = new ConcurrentHashMap<String, MsLinkActivityHandle>();

	private ConcurrentHashMap<String, MsSessionActivityHandle> msSessionId2ActivityHandleMap = new ConcurrentHashMap<String, MsSessionActivityHandle>();

	private ConcurrentHashMap<String, MsResourceActivityHandle> msResourceId2ActivityHandleMap = new ConcurrentHashMap<String, MsResourceActivityHandle>();

	public MsConnectionActivityHandle putMsConnectionActivity(MsConnection activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("putMsConnectionActivity(activity=" + activity + ")");
		}

		MsConnectionActivityHandle handle = new MsConnectionActivityHandle(activity.getId());

		msConnectionId2ActivityHandleMap.put(activity.getId(), handle);
		msConnectionActivities.put(handle, activity);

		return handle;
	}

	public MsLinkActivityHandle putMsLinkActivity(MsLink activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("putMsLinkActivity(activity=" + activity + ")");
		}

		MsLinkActivityHandle handle = new MsLinkActivityHandle(activity.getId());
		msLinkId2ActivityHandleMap.put(activity.getId(), handle);
		msLinkActivities.put(handle, activity);

		return handle;
	}

	public MsSessionActivityHandle putMsSessionActivity(MsSession activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("putMsSessionActivity(activity=" + activity + ")");
		}

		MsSessionActivityHandle handle = new MsSessionActivityHandle(activity.getId());
		msSessionId2ActivityHandleMap.put(activity.getId(), handle);
		msSessionActivities.put(handle, activity);

		return handle;
	}

	public MsResourceActivityHandle putMsResourceActivity(MsResource activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("putMsResourceActivity(activity=" + activity + ")");
		}

		MsResourceActivityHandle handle = new MsResourceActivityHandle(activity.getID());
		msResourceId2ActivityHandleMap.put(activity.getID(), handle);
		msResourceActivities.put(handle, activity);

		return handle;
	}

	public MsConnection getMsConnectionActivity(MsConnectionActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMsConnectionActivity(handle=" + handle + ")");
		}
		return msConnectionActivities.get(handle);
	}

	public MsLink getMsLinkActivity(MsLinkActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMsLinkActivity(handle=" + handle + ")");
		}
		return msLinkActivities.get(handle);
	}

	public MsSession getMsSessionActivity(MsSessionActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMsSessionActivity(handle=" + handle + ")");
		}
		return msSessionActivities.get(handle);
	}

	public MsResource getMsResourceActivity(MsResourceActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getMsResourceActivity(handle=" + handle + ")");
		}
		return msResourceActivities.get(handle);
	}

	public MsConnectionActivityHandle getMsConnectionActivityHandle(String msConnectionId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMsConnectionActivityHandle(msConnectionId=" + msConnectionId + ")");
		}
		return msConnectionId2ActivityHandleMap.get(msConnectionId);
	}

	public MsLinkActivityHandle getMsLinkActivityHandle(String msLinkId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMsLinkActivityHandle(msLinkId=" + msLinkId + ")");
		}
		return msLinkId2ActivityHandleMap.get(msLinkId);
	}

	public MsSessionActivityHandle getMsSessionActivityHandle(String msSessionId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMsSessionActivityHandle(msSessionId=" + msSessionId + ")");
		}
		return msSessionId2ActivityHandleMap.get(msSessionId);
	}

	public MsResourceActivityHandle getMsResourceActivityHandle(String msResourceId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Session(msSessionId=" + msResourceId + ")");
		}
		return msResourceId2ActivityHandleMap.get(msResourceId);
	}

	public void removeMediaActivity(ActivityHandle handle) {
		if (logger.isDebugEnabled()) {
			logger.debug("removeMediaActivity(handle=" + handle + ")");
		}

		if (handle instanceof MsConnectionActivityHandle) {
			MsConnection msConnection = msConnectionActivities.remove(handle);
			if (msConnection != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed MsConnection activity for handle " + handle);
				}
				if (msConnectionId2ActivityHandleMap.remove(msConnection.getId()) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("removed MsConnectionActivityHandle for MsConnection ID " + msConnection.getId());
					}
				}
			} else {
				logger.warn("MsConnection Activity for handle " + handle + " not found");
			}
		} else if (handle instanceof MsLinkActivityHandle) {
			MsLink msLink = msLinkActivities.remove(handle);
			if (msLink != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed MsLink activity for handle " + handle);
				}
				if (msLinkId2ActivityHandleMap.remove(msLink.getId()) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("removed MsLinkActivityHandle for MsLink ID " + msLink.getId());
					}
				}
			} else {
				logger.warn("MsLink Activity for handle " + handle + " not found");
			}
		} else if (handle instanceof MsSessionActivityHandle) {
			MsSession msSession = msSessionActivities.remove(handle);
			if (msSession != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed MsSession activity for handle " + handle);
				}
				if (msSessionId2ActivityHandleMap.remove(msSession.getId()) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("removed MsSessionActivityHandle for MsSession ID " + msSession.getId());
					}
				}
			} else {
				logger.warn("MsSession Activity for handle " + handle + " not found");
			}
		} else if (handle instanceof MsResourceActivityHandle) {
			MsResource msResource = msResourceActivities.remove(handle);
			if (msResource != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("removed MsResource activity for handle " + handle);
				}
				if (msResourceId2ActivityHandleMap.remove(msResource.getID()) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("removed MsResourceActivityHandle for MsResource ID " + msResource.getID());
					}
				}
			} else {
				logger.warn("MsResource Activity for handle " + handle + " not found");
			}
		}

		else {
			logger.warn("MediaActivityManager not yet implemented for ActivityHandle " + handle);
		}
	}

	public boolean containsActivityHandle(ActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("containsActivityHandle(handle=" + handle + ")");
		}

		if (handle instanceof MsConnectionActivityHandle) {
			return msConnectionActivities.containsKey((MsConnectionActivityHandle) handle);

		} else if (handle instanceof MsLinkActivityHandle) {
			return msLinkActivities.containsKey((MsLinkActivityHandle) handle);

		} else if (handle instanceof MsSessionActivityHandle) {
			return msSessionActivities.containsKey((MsSessionActivityHandle) handle);

		} else if (handle instanceof MsResourceActivityHandle) {
			return msResourceActivities.containsKey((MsResourceActivityHandle) handle);

		} else {
			return false;
		}
	}

	public Object getActivity(ActivityHandle handle) {

		if (logger.isDebugEnabled()) {
			logger.debug("getActivity(handle=" + handle + ")");
		}

		if (handle instanceof MsConnectionActivityHandle) {
			return msConnectionActivities.get((MsConnectionActivityHandle) handle);
		} else if (handle instanceof MsLinkActivityHandle) {
			return msLinkActivities.get((MsLinkActivityHandle) handle);
		} else if (handle instanceof MsSessionActivityHandle) {
			return msSessionActivities.get((MsSessionActivityHandle) handle);
		} else if (handle instanceof MsResourceActivityHandle) {
			return msResourceActivities.get((MsResourceActivityHandle) handle);
		} else {
			return null;
		}
	}

	public ActivityHandle getActivityHandle(Object activity) {

		if (logger.isDebugEnabled()) {
			logger.debug("getActivityHandle(activity=" + activity + ")");
		}

		if (activity instanceof MsConnection) {
			MsConnection castedActivity = (MsConnection) activity;
			return msConnectionId2ActivityHandleMap.get(castedActivity.getId());
		} else if (activity instanceof MsLink) {
			MsLink castedActivity = (MsLink) activity;
			return msLinkId2ActivityHandleMap.get(castedActivity.getId());
		} else if (activity instanceof MsSession) {
			MsSession castedActivity = (MsSession) activity;
			return msSessionId2ActivityHandleMap.get(castedActivity.getId());
		} else if (activity instanceof MsResource) {
			MsResource castedActivity = (MsResource) activity;
			return msResourceId2ActivityHandleMap.get(castedActivity.getID());
		} else {
			return null;
		}
	}

}
