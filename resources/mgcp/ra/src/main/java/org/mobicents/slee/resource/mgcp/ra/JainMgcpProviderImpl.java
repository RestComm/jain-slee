package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.JainMgcpStack;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;

import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;

import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;

import org.mobicents.mgcp.stack.JainMgcpExtendedListener;
import org.mobicents.mgcp.stack.JainMgcpStackProviderImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class JainMgcpProviderImpl implements JainMgcpProvider {

	private Tracer tracer = null;

	private final MgcpResourceAdaptor ra;
	private final JainMgcpStackProviderImpl provider;
	private MgcpStackListener listener = new MgcpStackListener();

	private HashMap<Integer, JainMgcpCommandEvent> commandsEventMap = new HashMap<Integer, JainMgcpCommandEvent>();

	public JainMgcpProviderImpl(MgcpResourceAdaptor ra, jain.protocol.ip.mgcp.JainMgcpProvider jainMgcpProvider,
			Tracer tracer) {
		this.ra = ra;
		this.provider = (JainMgcpStackProviderImpl) jainMgcpProvider;
		this.tracer = tracer;
		try {
			this.provider.addJainMgcpListener(listener);
		} catch (Throwable e) {
			String msg = "Couldn't register MgcpStackListener to JainMgcpStackProviderImpl";
			tracer.severe(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	public MgcpConnectionActivity getConnectionActivity(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier) {
		return getConnectionActivity(connectionIdentifier, endpointIdentifier, true);
	}

	protected MgcpConnectionActivity getConnectionActivity(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier, boolean startActivity) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager().getMgcpConnectionActivityHandle(
				connectionIdentifier, endpointIdentifier, -1);
		if (handle != null) {
			return ra.getMgcpActivityManager().getMgcpConnectionActivity(handle);
		} else {
			MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(connectionIdentifier,
					endpointIdentifier, ra);
			handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(activity);
			if (startActivity) {
				try {
					ra.getSleeEndpoint().startActivity(handle, activity);
				} catch (Exception e) {
					String msg = "Failed to start activity";
					tracer.severe(msg, e);
					if (handle != null) {
						ra.getMgcpActivityManager().removeMgcpActivity(handle);
					}
					activity = null;
				}
			}
			return activity;
		}
	}

	public MgcpConnectionActivity getConnectionActivity(int transactionHandle, EndpointIdentifier endpointIdentifier) {
		return getConnectionActivity(transactionHandle, endpointIdentifier, true);
	}

	protected MgcpConnectionActivity getConnectionActivity(int transactionHandle,
			EndpointIdentifier endpointIdentifier, boolean startActivity) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager().getMgcpConnectionActivityHandle(null,
				endpointIdentifier, transactionHandle);
		if (handle != null) {
			return ra.getMgcpActivityManager().getMgcpConnectionActivity(handle);
		} else {
			MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(transactionHandle, endpointIdentifier,
					ra);
			handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(activity);
			if (startActivity) {
				try {
					ra.getSleeEndpoint().startActivity(handle, activity);
				} catch (Exception e) {
					String msg = "Failed to start activity";
					tracer.severe(msg, e);
					if (handle != null) {
						ra.getMgcpActivityManager().removeMgcpActivity(handle);
					}
					activity = null;
				}
			}
			return activity;
		}

	}

	public List<MgcpConnectionActivity> getConnectionActivities(EndpointIdentifier endpointIdentifier) {
		return ra.getMgcpActivityManager().getMgcpConnectionActivities(endpointIdentifier);
	}

	public MgcpEndpointActivity getEndpointActivity(EndpointIdentifier endpointIdentifier) {
		return getEndpointActivity(endpointIdentifier, true);
	}

	protected MgcpEndpointActivity getEndpointActivity(EndpointIdentifier endpointIdentifier, boolean startActivity) {
		MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(endpointIdentifier.toString());
		MgcpEndpointActivityImpl activity = ra.getMgcpActivityManager().getMgcpEndpointActivity(handle);
		if (activity != null) {
			return activity;
		} else {
			activity = new MgcpEndpointActivityImpl(ra, endpointIdentifier);
			ra.getMgcpActivityManager().putMgcpEndpointActivity(activity);
			if (startActivity) {
				try {
					ra.getSleeEndpoint().startActivity(handle, activity);
				} catch (Exception e) {
					String msg = "Failed to start activity";
					tracer.severe(msg, e);
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
					activity = null;
				}
			}
			return activity;
		}
	}

	public void addJainMgcpListener(JainMgcpListener arg0) throws TooManyListenersException {
		throw new TooManyListenersException("this provider does not support listeners");
	}

	public JainMgcpStack getJainMgcpStack() {
		return this.provider.getJainMgcpStack();
	}

	public void removeJainMgcpListener(JainMgcpListener arg0) {
		// do nothing
	}

	public void sendMgcpEvents(JainMgcpEvent[] events) throws IllegalArgumentException {

		// provider.sendMgcpEvents(events);
		for (JainMgcpEvent event : events) {
			if (event instanceof CreateConnectionResponse) {
				ra.sendingCreateConnectionResponse((CreateConnectionResponse) event);
			}
			if (event instanceof JainMgcpCommandEvent) {
				commandsEventMap.put(event.getTransactionHandle(), (JainMgcpCommandEvent) event);
			}
		}
		provider.sendMgcpEvents(events);

	}

	public int getUniqueTransactionHandler() {
		// retreives current counter value and sets next one
		return provider.getUniqueTransactionHandler();
	}

	public CallIdentifier getUniqueCallIdentifier() {
		return provider.getUniqueCallIdentifier();
	}

	public RequestIdentifier getUniqueRequestIdentifier() {
		return provider.getUniqueRequestIdentifier();
	}

	private class MgcpStackListener implements JainMgcpExtendedListener {

		public void processMgcpCommandEvent(JainMgcpCommandEvent event) {
			ra.processMgcpCommandEvent(event);

		}

		public void processMgcpResponseEvent(JainMgcpResponseEvent response) {
			ra.processMgcpResponseEvent(response, commandsEventMap.remove(response.getTransactionHandle()));

		}

		public void transactionEnded(int handle) {
			// TODO: ????

		}

		public void transactionRxTimedOut(JainMgcpCommandEvent command) {
			ra.processRxTimeout(command);
			commandsEventMap.remove(command.getTransactionHandle());

		}

		public void transactionTxTimedOut(JainMgcpCommandEvent command) {
			ra.processTxTimeout(command);
			commandsEventMap.remove(command.getTransactionHandle());

		}
	}

}
