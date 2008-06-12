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

import java.util.List;
import java.util.TooManyListenersException;
import java.util.WeakHashMap;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.stack.JainMgcpExtendedListener;
import org.mobicents.mgcp.stack.JainMgcpStackProviderImpl;

public class JainMgcpProviderImpl implements JainMgcpProvider {

	private static Logger logger = Logger.getLogger(JainMgcpProviderImpl.class);

	private final MgcpResourceAdaptor ra;
	private final JainMgcpStackProviderImpl provider;
	private MgcpStackListener listener=new MgcpStackListener();
	private WeakHashMap<Integer, JainMgcpCommandEvent> commandsEventMap=new WeakHashMap<Integer, JainMgcpCommandEvent>();
	
	
	public JainMgcpProviderImpl(MgcpResourceAdaptor ra,
			jain.protocol.ip.mgcp.JainMgcpProvider jainMgcpProvider) {
		this.ra = ra;
		this.provider = (JainMgcpStackProviderImpl) jainMgcpProvider;
		try {
			this.provider.addJainMgcpListener(listener);
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MgcpConnectionActivity getConnectionActivity(
			ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager()
				.getMgcpConnectionActivityHandle(connectionIdentifier,
						endpointIdentifier, -1);
		if (handle != null) {
			return ra.getMgcpActivityManager()
					.getMgcpConnectionActivity(handle);
		} else {
			try {
				MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(
						connectionIdentifier, endpointIdentifier, ra);
				handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(
						activity);
				ra.getSleeEndpoint().activityStarted(handle);
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity", e);
				if (handle != null) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}
	}

	public MgcpConnectionActivity getConnectionActivity(int transactionHandle,
			EndpointIdentifier endpointIdentifier) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager()
				.getMgcpConnectionActivityHandle(null, endpointIdentifier,
						transactionHandle);
		if (handle != null) {
			return ra.getMgcpActivityManager()
					.getMgcpConnectionActivity(handle);
		} else {
			try {
				MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(
						transactionHandle, endpointIdentifier, ra);
				handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(
						activity);
				ra.getSleeEndpoint().activityStarted(handle);
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity", e);
				if (handle != null) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}

	}

	public List<MgcpConnectionActivity> getConnectionActivities(
			EndpointIdentifier endpointIdentifier) {
		return ra.getMgcpActivityManager().getMgcpConnectionActivities(
				endpointIdentifier);
	}

	public MgcpEndpointActivity getEndpointActivity(
			EndpointIdentifier endpointIdentifier) {
		MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(
				endpointIdentifier.toString());
		MgcpEndpointActivityImpl activity = ra.getMgcpActivityManager()
				.getMgcpEndpointActivity(handle);
		if (activity != null) {
			return activity;
		} else {
			boolean insertedActivity = false;
			activity = new MgcpEndpointActivityImpl(ra, endpointIdentifier);
			try {
				ra.getMgcpActivityManager().putMgcpEndpointActivity(handle,
						activity);
				insertedActivity = true;
				ra.getSleeEndpoint().activityStarted(
						new MgcpEndpointActivityHandle(activity
								.getEndpointIdentifier().toString()));
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity", e);
				if (insertedActivity) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}
	}

	public void addJainMgcpListener(JainMgcpListener arg0)
			throws TooManyListenersException {
		throw new TooManyListenersException(
				"this provider does not support listeners");
	}

	public JainMgcpStack getJainMgcpStack() {
		return null;
	}

	public void removeJainMgcpListener(JainMgcpListener arg0) {
		// do nothing
	}

	public void sendMgcpEvents(JainMgcpEvent[] events)
			throws IllegalArgumentException {

		provider.sendMgcpEvents(events);
		for(JainMgcpEvent event: events)
		{
			if (event instanceof CreateConnectionResponse) {
				ra.sendingCreateConnectionResponse((CreateConnectionResponse) event);
			}
			if(event instanceof JainMgcpCommandEvent)
			{
				this.commandsEventMap.put(event.getTransactionHandle(), (JainMgcpCommandEvent) event);
			}
		}

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
	
	private class MgcpStackListener implements JainMgcpExtendedListener
	{



		public void processMgcpCommandEvent(JainMgcpCommandEvent event) {
			ra.processMgcpCommandEvent(event);
			
		}

		public void processMgcpResponseEvent(JainMgcpResponseEvent response) {
			ra.processMgcpResponseEvent(response, commandsEventMap.get(response.getTransactionHandle()));
			
		}

		public void transactionEnded(int handle) {
			//TODO: ????
			
		}

		public void transactionRxTimedOut(JainMgcpCommandEvent command) {
			ra.processRxTimeout(command);
			commandsEventMap.remove(command.getTransactionHandle());
			
		}

		public void transactionTxTimedOut(JainMgcpCommandEvent command) {
			ra.processTxTimeout(command);
			commandsEventMap.remove(command.getTransactionHandle());
			
		}}

}
