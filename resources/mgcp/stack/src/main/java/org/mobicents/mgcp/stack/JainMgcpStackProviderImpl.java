package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;
import jain.protocol.ip.mgcp.JainMgcpProvider;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.JainMgcpStack;
import jain.protocol.ip.mgcp.message.AuditConnectionResponse;
import jain.protocol.ip.mgcp.message.AuditEndpointResponse;
import jain.protocol.ip.mgcp.message.Constants;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.ModifyConnectionResponse;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.NotifyResponse;
import jain.protocol.ip.mgcp.message.RestartInProgressResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


import org.apache.log4j.Logger;



public class JainMgcpStackProviderImpl implements JainMgcpProvider {

	private static Logger logger = Logger.getLogger(JainMgcpStackProviderImpl.class);

	private final JainMgcpStackImpl runningStack;

	// a tx handle id must be between 1 and 999999999
	private static int MIN_TRANSACTION_HANDLE_ID = 1;
	private static int MAX_TRANSACTION_HANDLE_ID = Integer.MAX_VALUE < 999999999 ? Integer.MAX_VALUE : 999999999;
	private static AtomicInteger transactionHandleCounter = new AtomicInteger(MIN_TRANSACTION_HANDLE_ID);
	private static AtomicLong callIdentifierCounter = new AtomicLong(1);
	private static AtomicLong requestIdentifierCounter = new AtomicLong(1);
	//For now provider is only holder of listeners
    protected Set<JainMgcpListener> jainListeners=new HashSet<JainMgcpListener>();
    //This set contains upgraded listeners - one that allow to notify when tx ends
    protected Set<JainMgcpExtendedListener> jainMobicentsListeners=new HashSet<JainMgcpExtendedListener>();
	

	public JainMgcpStackProviderImpl(JainMgcpStackImpl runningStack) {
		super();
		this.runningStack = runningStack;
	}

	public void addJainMgcpListener(JainMgcpListener listener) throws TooManyListenersException {
		if(listener instanceof JainMgcpExtendedListener)
		{
			synchronized(this.jainMobicentsListeners)
			{
				this.jainMobicentsListeners.add((JainMgcpExtendedListener) listener);
			}
		}else
		{
			synchronized(this.jainListeners)
			{
				this.jainListeners.add(listener);
			}
		}
	}

	public JainMgcpStack getJainMgcpStack() {
		return this.runningStack;
	}

	public void removeJainMgcpListener(JainMgcpListener listener) {
		if(listener instanceof JainMgcpExtendedListener)
		{
			synchronized(this.jainMobicentsListeners)
			{
				this.jainMobicentsListeners.remove((JainMgcpExtendedListener) listener);
			}
		}else
		{
			synchronized(this.jainListeners)
			{
				this.jainListeners.remove(listener);
			}
		}
	}

	public void sendMgcpEvents(JainMgcpEvent[] events) throws IllegalArgumentException {

		for (JainMgcpEvent event : events) {

			if (event instanceof JainMgcpCommandEvent) {

				// SENDING REQUEST
				JainMgcpCommandEvent commandEvent = (JainMgcpCommandEvent) event;

				TransactionHandler handle = null;
				switch (commandEvent.getObjectIdentifier()) {

				case Constants.CMD_AUDIT_CONNECTION:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending EndpointConfiguration object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new AuditConnectionHandler(this.runningStack);
					break;

				case Constants.CMD_AUDIT_ENDPOINT:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending EndpointConfiguration object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new AuditEndpointHandler(this.runningStack);
					break;

				case Constants.CMD_CREATE_CONNECTION:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending CreateConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new CreateConnectionHandler(this.runningStack);
					break;

				case Constants.CMD_DELETE_CONNECTION:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending DeleteConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new DeleteConnectionHandler(this.runningStack);
					break;

				case Constants.CMD_ENDPOINT_CONFIGURATION:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending EndpointConfiguration object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new EndpointConfigurationHandler(this.runningStack);
					break;

				case Constants.CMD_MODIFY_CONNECTION:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending ModifyConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new ModifyConnectionHandler(this.runningStack);
					break;

				case Constants.CMD_NOTIFICATION_REQUEST:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending ModifyConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new NotificationRequestHandler(this.runningStack);
					break;

				case Constants.CMD_NOTIFY:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending ModifyConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new NotifyHandler(this.runningStack);
					break;

				case Constants.CMD_RESP_UNKNOWN:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending ModifyConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new RespUnknownHandler(this.runningStack);
					break;

				case Constants.CMD_RESTART_IN_PROGRESS:
					if (logger.isDebugEnabled()) {
						logger.debug("Sending ModifyConnection object to " + commandEvent.getEndpointIdentifier());
					}
					handle = new RestartInProgressHandler(this.runningStack);
					break;

				default:
					throw new IllegalArgumentException("Could not send type of the message yet");
				}

				handle.send(commandEvent);

			} else {

				// SENDING RESPONSE
				ReceivedTransactionID receivedTransactionID = null;
				try {
					receivedTransactionID = (ReceivedTransactionID) event.getSource();
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Illegal event source. Msg when retrieving: " + e.getMessage());
				}
				if (receivedTransactionID == null) {
					throw new IllegalArgumentException("Event source is null");
				}
				TransactionHandler handler = this.runningStack.rTransactions.get(receivedTransactionID);
				if (handler == null) {
					throw new IllegalArgumentException("Unknown transaction handle: " + event.getTransactionHandle());
				}
				// callback ra if it's a create connection response
				//if (event instanceof CreateConnectionResponse) {
					//ra.sendingCreateConnectionResponse((CreateConnectionResponse) event);
				//}
				// send event
				handler.send((JainMgcpResponseEvent) event);

			}
		}

	}

	public int getUniqueTransactionHandler() {
		// retreives current counter value and sets next one
		int current;
		int next;
		do {
			current = transactionHandleCounter.get();
			next = (current == MAX_TRANSACTION_HANDLE_ID ? MIN_TRANSACTION_HANDLE_ID : current + 1);
		} while (!transactionHandleCounter.compareAndSet(current, next));

		return current;
	}

	public void processMgcpResponseEvent(JainMgcpResponseEvent response, JainMgcpEvent command) {
		//ra.processMgcpResponseEvent(response, command);
		synchronized(this.jainListeners)
		{
			for(JainMgcpListener listener:this.jainListeners)
			{
				listener.processMgcpResponseEvent(response);
			}
		}
		
		
		synchronized(this.jainMobicentsListeners)
		{
			for(JainMgcpListener listener:this.jainMobicentsListeners)
			{
				listener.processMgcpResponseEvent(response);
			}
		}
		
	}

	public void processMgcpCommandEvent(JainMgcpCommandEvent command) {
		//ra.processMgcpCommandEvent(command);
		synchronized(this.jainListeners)
		{
			for(JainMgcpListener listener:this.jainListeners)
			{
				listener.processMgcpCommandEvent(command);
			}
		}
		
		
		synchronized(this.jainMobicentsListeners)
		{
			for(JainMgcpListener listener:this.jainMobicentsListeners)
			{
				listener.processMgcpCommandEvent(command);
			}
		}
	}

	protected void processTxTimeout(JainMgcpCommandEvent command) {
		// notify RA
		//ra.processTxTimeout(command);
		synchronized(this.jainMobicentsListeners)
		{
			for(JainMgcpExtendedListener listener:this.jainMobicentsListeners)
			{
				listener.transactionTxTimedOut(command);
			}
		}
	}

	protected void processRxTimeout(JainMgcpCommandEvent command) {
		// notify RA
		//ra.processRxTimeout(command);
		synchronized(this.jainMobicentsListeners)
		{
			for(JainMgcpExtendedListener listener:this.jainMobicentsListeners)
			{
				listener.transactionRxTimedOut(command);
			}
		}
		// reply to server
		JainMgcpResponseEvent response = null;
		// FIXME - how to change o return code of transaction timeout?!?
		switch (command.getObjectIdentifier()) {
		case Constants.CMD_AUDIT_CONNECTION:
			response = new AuditConnectionResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_AUDIT_ENDPOINT:
			response = new AuditEndpointResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_CREATE_CONNECTION:
			response = new CreateConnectionResponse(this, ReturnCode.Transient_Error, new ConnectionIdentifier(Long
					.toHexString(new Random(System.currentTimeMillis()).nextLong())));
			break;
		case Constants.CMD_DELETE_CONNECTION:
			response = new DeleteConnectionResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_ENDPOINT_CONFIGURATION:
			response = new DeleteConnectionResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_MODIFY_CONNECTION:
			response = new ModifyConnectionResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_NOTIFICATION_REQUEST:
			response = new NotificationRequestResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_NOTIFY:
			response = new NotifyResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_RESP_UNKNOWN:
			// FIXME - what response?!?
			response = new NotifyResponse(this, ReturnCode.Transient_Error);
			break;
		case Constants.CMD_RESTART_IN_PROGRESS:
			response = new RestartInProgressResponse(this, ReturnCode.Transient_Error);
			break;
		default:
			throw new IllegalArgumentException("Could not send type of the message yet");
		}
		response.setTransactionHandle(command.getTransactionHandle());
		JainMgcpEvent[] events = { response };
		sendMgcpEvents(events);
	}

	public CallIdentifier getUniqueCallIdentifier() {
		long current = -1;
		boolean b = true;
		while (b) {
			current = callIdentifierCounter.get();
			if (current == Long.MAX_VALUE) {
				b = !callIdentifierCounter.compareAndSet(current, 1);
			} else {
				b = !callIdentifierCounter.compareAndSet(current, current + 1);
			}
		}
		return new CallIdentifier(Long.toHexString(current));
	}

	public RequestIdentifier getUniqueRequestIdentifier() {
		long current = -1;
		boolean b = true;
		while (b) {
			current = requestIdentifierCounter.get();
			if (current == Long.MAX_VALUE) {
				b = !requestIdentifierCounter.compareAndSet(current, 1);
			} else {
				b = !requestIdentifierCounter.compareAndSet(current, current + 1);
			}
		}
		return new RequestIdentifier(Long.toHexString(current));
	}
}
