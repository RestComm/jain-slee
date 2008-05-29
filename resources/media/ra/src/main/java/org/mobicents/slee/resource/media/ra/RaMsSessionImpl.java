package org.mobicents.slee.resource.media.ra;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import org.apache.log4j.Logger;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.media.msc.common.MsSessionState;
import org.mobicents.media.msc.common.events.MsSessionEventCause;
import org.mobicents.media.msc.common.events.MsSessionEventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSessionListener;
import org.mobicents.mscontrol.impl.MsConnectionImpl;
import org.mobicents.mscontrol.impl.MsLinkImpl;
import org.mobicents.mscontrol.impl.MsSessionEventImpl;

/**
 * 
 * @author amit.bhayani
 * 
 */
public class RaMsSessionImpl implements MsSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8812912256314276363L;

	private static Logger logger = Logger.getLogger(RaMsSessionImpl.class);

	private String id = (new UID()).toString();

	private MsSessionState state;

	private MsProvider msProvider;
	private MediaResourceAdaptor ra;

	protected ArrayList<MsSessionListener> sessionListeners = new ArrayList<MsSessionListener>();

	// connections holders
	private ArrayList<MsLink> links = new ArrayList<MsLink>();
	private ArrayList<MsConnection> connections = new ArrayList<MsConnection>();

	public RaMsSessionImpl(MsProvider msProvider, MediaResourceAdaptor ra) {
		this.msProvider = msProvider;
		this.ra = ra;
	}


	public MsLink createLink(MsLinkMode mode) {
		MsLink msLink = new MsLinkImpl(this, mode);
		links.add(msLink);

		MsLinkActivityHandle msLinkActivityHandle = ra.getMediaActivityManager().putMsLinkActivity(msLink);
		try {
			ra.getSleeEndpoint().activityStarted(msLinkActivityHandle);
		} catch (NullPointerException e) {
			logger.error("NullPointerException while trying to start the MsLink Activity for MsLink id = "
					+ msLink.getId());
		} catch (IllegalStateException e) {
			logger.error("IllegalStateException while trying to start the MsLink Activity for MsLink id = "
					+ msLink.getId());
		} catch (ActivityAlreadyExistsException e) {
			logger.error("ActivityAlreadyExistsException while trying to start the MsLink Activity for MsLink id = "
					+ msLink.getId());
		} catch (CouldNotStartActivityException e) {
			logger.error("CouldNotStartActivityException while trying to start the MsLink Activity for MsLink id = "
					+ msLink.getId());
		}

		setState(MsSessionState.ACTIVE, MsSessionEventCause.LINK_CREATED, msLink);
		msLink.fireMsLinkCreated();

		return msLink;
	}

	public MsConnection createNetworkConnection(String endpointName) {

		MsConnection connection = new MsConnectionImpl(this, endpointName);
		connections.add(connection);

		MsConnectionActivityHandle msConnectionActivityHandle = ra.getMediaActivityManager().putMsConnectionActivity(
				connection);
		try {
			ra.getSleeEndpoint().activityStarted(msConnectionActivityHandle);
		} catch (NullPointerException e) {
			logger.error("NullPointerException while trying to start the MsConnection Activity for MsConnection id = "
					+ connection.getId());
		} catch (IllegalStateException e) {
			logger.error("IllegalStateException while trying to start the MsConnection Activity for MsConnection id = "
					+ connection.getId());
		} catch (ActivityAlreadyExistsException e) {
			logger
					.error("ActivityAlreadyExistsException while trying to start the MsConnection Activity for MsConnection id = "
							+ connection.getId());
		} catch (CouldNotStartActivityException e) {
			logger
					.error("CouldNotStartActivityException while trying to start the MsConnection Activity for MsConnection id = "
							+ connection.getId());
		}
		setState(MsSessionState.ACTIVE, MsSessionEventCause.CONNECTION_CREATED, connection);
		connection.fireConnectionInitialized();
		return connection;
	}

	public String getId() {
		return this.id;
	}

	public MsProvider getProvider() {
		return this.msProvider;
	}

	public MsSessionState getState() {
		return this.state;
	}

	public void addSessionListener(MsSessionListener listener) {
		sessionListeners.add(listener);
	}
	
	public void removeSessionListener(MsSessionListener listener) {
		sessionListeners.remove(listener);
	}

	public void setSessionStateIdle() {
		setState(MsSessionState.IDLE, MsSessionEventCause.SESSION_CREATED, this);
	}

	/**
	 * Sends events related to this session.
	 * 
	 * @param eventID
	 *            the id of the event to be sent.
	 */
	private void sendEvent(MsSessionEventID eventID, MsSessionEventCause eventCause, Object causeObject) {
		MsSessionEventImpl evt = new MsSessionEventImpl(this, eventID, eventCause, causeObject);
		new Thread(evt).start();
	}

	/**
	 * Modify state of the session.
	 * 
	 * @param state
	 *            the new value of the state.
	 */
	private void setState(MsSessionState state, MsSessionEventCause eventCause, Object causeObject) {
		if (this.state != state) {
			this.state = state;
			switch (state) {
			case IDLE:
				sendEvent(MsSessionEventID.SESSION_CREATED, eventCause, causeObject);
				break;
			case ACTIVE:
				sendEvent(MsSessionEventID.SESSION_ACTIVE, eventCause, causeObject);
				break;
			case INVALID:
				sendEvent(MsSessionEventID.SESSION_INVALID, eventCause, causeObject);
				break;
			}
		}
	}
	
	public String toString() {
		return "RaMsSessionImpl[" + id + "]";
	}	

	public List<MsSessionListener> getSessionListeners() {
		return this.sessionListeners;
	}
	
	public List<MsConnection> getConnections() {
		return this.connections;
	}	

}
