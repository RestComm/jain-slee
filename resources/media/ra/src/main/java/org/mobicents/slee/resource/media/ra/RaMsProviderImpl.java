package org.mobicents.slee.resource.media.ra;

import java.util.ArrayList;
import java.util.List;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import org.apache.log4j.Logger;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionListener;
import org.mobicents.mscontrol.MsLinkListener;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsResourceListener;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSessionListener;
import org.mobicents.mscontrol.MsSignalDetector;
import org.mobicents.mscontrol.MsSignalGenerator;
import org.mobicents.mscontrol.impl.MsSignalDetectorImpl;
import org.mobicents.mscontrol.impl.MsSignalGeneratorImpl;

/**
 * 
 * @author amit.bhayani
 * 
 */
public class RaMsProviderImpl implements MsProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6526827341543630317L;

	private static Logger logger = Logger.getLogger(RaMsProviderImpl.class);
	protected ArrayList<MsSessionListener> sessionListeners = new ArrayList<MsSessionListener>();
	protected ArrayList<MsConnectionListener> connectionListeners = new ArrayList<MsConnectionListener>();
	protected ArrayList<MsResourceListener> resourceListeners = new ArrayList<MsResourceListener>();

	protected ArrayList<MsLinkListener> linkListeners = new ArrayList<MsLinkListener>();

	private ArrayList<MsSession> calls = new ArrayList<MsSession>();

	private MediaResourceAdaptor ra;

	public RaMsProviderImpl(MediaResourceAdaptor ra) {
		this.ra = ra;
	}

	public void addConnectionListener(MsConnectionListener listener) {
		connectionListeners.add(listener);

	}

	public void addLinkListener(MsLinkListener listener) {
		linkListeners.add(listener);
	}

	public void addResourceListener(MsResourceListener listener) {
		resourceListeners.add(listener);

	}

	public void addSessionListener(MsSessionListener listener) {
		sessionListeners.add(listener);
	}

	public MsSession createSession() {

		MsSession raMsSessionImpl = new RaMsSessionImpl(this, ra);
		calls.add(raMsSessionImpl);
		MsSessionActivityHandle msSessionActivityHandle = ra.getMediaActivityManager().putMsSessionActivity(
				raMsSessionImpl);
		try {
			ra.getSleeEndpoint().activityStarted(msSessionActivityHandle);
		} catch (NullPointerException e) {
			logger.error("NullPointerException while trying to start the MsSession Activity for MsSession id = "
					+ raMsSessionImpl.getId());
		} catch (IllegalStateException e) {
			logger.error("IllegalStateException while trying to start the MsSession Activity for MsSession id = "
					+ raMsSessionImpl.getId());
		} catch (ActivityAlreadyExistsException e) {
			logger
					.error("ActivityAlreadyExistsException while trying to start the MsSession Activity for MsSession id = "
							+ raMsSessionImpl.getId());
		} catch (CouldNotStartActivityException e) {
			logger
					.error("CouldNotStartActivityException while trying to start the MsSession Activity for MsSession id = "
							+ raMsSessionImpl.getId());
		}
		raMsSessionImpl.setSessionStateIdle();
		return raMsSessionImpl;
	}

	public List<MsConnectionListener> getConnectionListeners() {
		return this.connectionListeners;
	}

	public List<MsLinkListener> getLinkListeners() {
		return this.linkListeners;
	}

	public List<MsResourceListener> getResourceListeners() {
		return this.resourceListeners;
	}

	public List<MsSessionListener> getSessionListeners() {
		return this.sessionListeners;
	}

	public MsSignalDetector getSignalDetector(String endpointName) {
		MsSignalDetector msSignalDetector = new MsSignalDetectorImpl(this, endpointName);

		MsResourceActivityHandle msResourceActivityHandle = ra.getMediaActivityManager().putMsResourceActivity(
				msSignalDetector);
		try {
			ra.getSleeEndpoint().activityStarted(msResourceActivityHandle);
		} catch (NullPointerException e) {
			logger.error("NullPointerException while trying to start the msSignalDetector Activity for Endpoint id = "
					+ endpointName);
		} catch (IllegalStateException e) {
			logger.error("IllegalStateException while trying to start the msSignalDetector Activity for Endpoint id = "
					+ endpointName);
		} catch (ActivityAlreadyExistsException e) {
			logger
					.error("ActivityAlreadyExistsException while trying to start the msSignalDetector Activity for Endpoint id = "
							+ endpointName);
		} catch (CouldNotStartActivityException e) {
			logger
					.error("CouldNotStartActivityException while trying to start the msSignalDetector Activity for Endpoint id = "
							+ endpointName);
		}

		return msSignalDetector;
	}

	public MsSignalGenerator getSignalGenerator(String endpointName) {
		MsSignalGenerator msSignalGenerator = new MsSignalGeneratorImpl(this, endpointName);

		MsResourceActivityHandle msResourceActivityHandle = ra.getMediaActivityManager().putMsResourceActivity(
				msSignalGenerator);
		try {
			ra.getSleeEndpoint().activityStarted(msResourceActivityHandle);
		} catch (NullPointerException e) {
			logger.error("NullPointerException while trying to start the MsSignalGenerator Activity for Endpoint id = "
					+ endpointName);
		} catch (IllegalStateException e) {
			logger
					.error("IllegalStateException while trying to start the MsSignalGenerator Activity for Endpoint id = "
							+ endpointName);
		} catch (ActivityAlreadyExistsException e) {
			logger
					.error("ActivityAlreadyExistsException while trying to start the MsSignalGenerator Activity for Endpoint id = "
							+ endpointName);
		} catch (CouldNotStartActivityException e) {
			logger
					.error("CouldNotStartActivityException while trying to start the MsSignalGenerator Activity for Endpoint id = "
							+ endpointName);
		}

		return msSignalGenerator;
	}

	public void removeLinkListener(MsLinkListener listener) {
		linkListeners.remove(listener);
	}

	public void removeSessionListener(MsSessionListener listener) {
		sessionListeners.remove(listener);
	}
	
	public MsConnection getMsConnection(String msConnectionId) {

		for (MsSession e : calls) {
			for (MsConnection c : e.getConnections()) {
				if (c.getId().equals(msConnectionId)) {
					return c;
				}
			}
		}
		return null;
	}

	public List<MsConnection> getMsConnections(String endpointName) {
		List<MsConnection> msConnectionList = new ArrayList<MsConnection>();
		for (MsSession e : calls) {
			for (MsConnection c : e.getConnections()) {
				if (c.getEndpoint().equals(endpointName)) {
					msConnectionList.add(c);
				}
			}
		}
		return msConnectionList;
	}	

}
