package org.mobicents.mgcp;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;
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

import java.util.Random;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.mgcp.ra.MgcpConnectionActivityHandle;
import org.mobicents.slee.resource.mgcp.ra.MgcpConnectionActivityImpl;
import org.mobicents.slee.resource.mgcp.ra.MgcpEndpointActivityHandle;
import org.mobicents.slee.resource.mgcp.ra.MgcpEndpointActivityImpl;
import org.mobicents.slee.resource.mgcp.ra.MgcpResourceAdaptor;

public class JainMgcpProviderImpl implements JainMgcpProvider {

	private static Logger logger = Logger.getLogger(JainMgcpProviderImpl.class);
	 
	private final MgcpResourceAdaptor ra;
	
	// a tx handle id must be between 1 and 999999999
	private static int MIN_TRANSACTION_HANDLE_ID = 1;
	private static int MAX_TRANSACTION_HANDLE_ID = Integer.MAX_VALUE < 999999999 ? Integer.MAX_VALUE : 999999999; 
	private static AtomicInteger transactionHandleCounter = new AtomicInteger(MIN_TRANSACTION_HANDLE_ID);
	private static AtomicLong callIdentifierCounter = new AtomicLong(1);
	private static AtomicLong requestIdentifierCounter = new AtomicLong(1);
	
	public JainMgcpProviderImpl(MgcpResourceAdaptor ra) {
		this.ra = ra;
	}
	
	public MgcpConnectionActivity getConnectionActivity(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager().getMgcpConnectionActivityHandle(connectionIdentifier,endpointIdentifier,-1);
		if (handle != null) {
			return ra.getMgcpActivityManager().getMgcpConnectionActivity(handle);
		}
		else {
			try {
				MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(connectionIdentifier,endpointIdentifier,ra);
				handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(activity);
				ra.getSleeEndpoint().activityStarted(handle);
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity",e);
				if (handle != null) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}
	}
	
	public MgcpConnectionActivity getConnectionActivity(int transactionHandle,EndpointIdentifier endpointIdentifier) {
		MgcpConnectionActivityHandle handle = ra.getMgcpActivityManager().getMgcpConnectionActivityHandle(null,endpointIdentifier,transactionHandle);
		if (handle != null) {
			return ra.getMgcpActivityManager().getMgcpConnectionActivity(handle);
		}
		else {
			try {
				MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(transactionHandle,endpointIdentifier,ra);
				handle = ra.getMgcpActivityManager().putMgcpConnectionActivity(activity);
				ra.getSleeEndpoint().activityStarted(handle);				
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity",e);
				if (handle != null) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}
		
	}

	public MgcpEndpointActivity getEndpointActivity(EndpointIdentifier endpointIdentifier) {
		MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(endpointIdentifier.toString());
		MgcpEndpointActivityImpl activity = ra.getMgcpActivityManager().getMgcpEndpointActivity(handle);
		if (activity != null) {
			return activity;
		}
		else {
			boolean insertedActivity = false;
			activity = new MgcpEndpointActivityImpl(ra,endpointIdentifier);
			try {
				ra.getMgcpActivityManager().putMgcpEndpointActivity(handle, activity);
				insertedActivity = true;
				ra.getSleeEndpoint().activityStarted(new MgcpEndpointActivityHandle(activity.getEndpointIdentifier().toString()));
				return activity;
			} catch (Exception e) {
				logger.error("Failed to start activity",e);
				if (insertedActivity) {
					ra.getMgcpActivityManager().removeMgcpActivity(handle);
				}
				return null;
			}
		}	
	}

	public void addJainMgcpListener(JainMgcpListener arg0)
			throws TooManyListenersException {
		throw new TooManyListenersException("this provider does not support listeners");		
	}

	public JainMgcpStack getJainMgcpStack() {
		return null;
	}

	public void removeJainMgcpListener(JainMgcpListener arg0) {
		// do nothing		
	}

	public void sendMgcpEvents(JainMgcpEvent[] events)
			throws IllegalArgumentException {
		
		for (JainMgcpEvent event:events) {
			
            if (event instanceof JainMgcpCommandEvent) {
            	
            	// SENDING REQUEST
                JainMgcpCommandEvent commandEvent = (JainMgcpCommandEvent) event;
         
                TransactionHandler handle = null;
                switch (commandEvent.getObjectIdentifier()) {
                
                case Constants.CMD_AUDIT_CONNECTION:
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending EndpointConfiguration object to "
                				+ commandEvent.getEndpointIdentifier());
                	}
                	handle = new AuditConnectionHandler(ra.getStack());
                	break;
                
                case Constants.CMD_AUDIT_ENDPOINT:
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending EndpointConfiguration object to "
                				+ commandEvent.getEndpointIdentifier());
                	}
                	handle = new AuditEndpointHandler(ra.getStack());
                	break;
              	
                case Constants.CMD_CREATE_CONNECTION:
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending CreateConnection object to "
                				+ commandEvent.getEndpointIdentifier());
                	}
                	handle = new CreateConnectionHandler(ra.getStack());
                	break;

                case Constants.CMD_DELETE_CONNECTION :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending DeleteConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new DeleteConnectionHandler(ra.getStack());
                	break;
                	
                case Constants.CMD_ENDPOINT_CONFIGURATION :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending EndpointConfiguration object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new EndpointConfigurationHandler(ra.getStack());
                	break;
                	
                case Constants.CMD_MODIFY_CONNECTION :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending ModifyConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new ModifyConnectionHandler(ra.getStack());
                	break;
                
                case Constants.CMD_NOTIFICATION_REQUEST :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending ModifyConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new NotificationRequestHandler(ra.getStack());
                	break;
                	
                case Constants.CMD_NOTIFY :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending ModifyConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new NotifyHandler(ra.getStack());
                	break;
                
                case Constants.CMD_RESP_UNKNOWN :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending ModifyConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new RespUnknownHandler(ra.getStack());
                	break;
                	
                case Constants.CMD_RESTART_IN_PROGRESS :
                	if (logger.isDebugEnabled()) {
                		logger.debug("Sending ModifyConnection object to " + 
                				commandEvent.getEndpointIdentifier());
                	}
                	handle = new RestartInProgressHandler(ra.getStack());
                	break;
                	
                default :
                	throw new IllegalArgumentException("Could not send type of the message yet");
                }

                handle.send(commandEvent);
                
            } else  {
            	
                // SENDING RESPONSE   
            	ReceivedTransactionID receivedTransactionID = null;
            	try {
            		receivedTransactionID = (ReceivedTransactionID)event.getSource();
            	}
            	catch (ClassCastException e) {
            		throw new IllegalArgumentException("Illegal event source. Msg when retrieving: "+e.getMessage());
            	}
            	if (receivedTransactionID == null) {
            		throw new IllegalArgumentException("Event source is null");
            	}
            	TransactionHandler handler = ra.getStack().rTransactions.get(receivedTransactionID);
                if (handler == null) {
                    throw new IllegalArgumentException("Unknown transaction handle: " + event.getTransactionHandle());
                }
                // callback ra if it's a create connection response
                if (event instanceof CreateConnectionResponse) {
                	ra.sendingCreateConnectionResponse((CreateConnectionResponse)event);
                }
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
			next = (current == MAX_TRANSACTION_HANDLE_ID ? MIN_TRANSACTION_HANDLE_ID : current+1);
		}
		while(!transactionHandleCounter.compareAndSet(current,next)); 
		
		return current;
	}
	
	public void processMgcpResponseEvent(JainMgcpResponseEvent response, JainMgcpEvent command) {
		ra.processMgcpResponseEvent(response,command);
	}
	
	public void processMgcpCommandEvent(JainMgcpCommandEvent command) {
		ra.processMgcpCommandEvent(command);
	}
	
	protected void processTxTimeout(JainMgcpCommandEvent command) {
		// notify RA
		ra.processTxTimeout(command);
	}
	
	protected void processRxTimeout(JainMgcpCommandEvent command) {
		// notify RA
		ra.processRxTimeout(command);
		// reply to server
		JainMgcpResponseEvent response = null;
		// FIXME - how to change o return code of transaction timeout?!?
		switch (command.getObjectIdentifier()) {        
        case Constants.CMD_AUDIT_CONNECTION:
        	response = new AuditConnectionResponse(this,ReturnCode.Transient_Error);
        	break;        
        case Constants.CMD_AUDIT_ENDPOINT:
        	response = new AuditEndpointResponse(this,ReturnCode.Transient_Error);
        	break;      	
        case Constants.CMD_CREATE_CONNECTION:
        	response = new CreateConnectionResponse(this,ReturnCode.Transient_Error,new ConnectionIdentifier(Long.toHexString(new Random(System.currentTimeMillis()).nextLong())));
        	break;
        case Constants.CMD_DELETE_CONNECTION :
        	response = new DeleteConnectionResponse(this,ReturnCode.Transient_Error);
        	break;        	
        case Constants.CMD_ENDPOINT_CONFIGURATION :
        	response = new DeleteConnectionResponse(this,ReturnCode.Transient_Error);
        	break;        	
        case Constants.CMD_MODIFY_CONNECTION :
        	response = new ModifyConnectionResponse(this,ReturnCode.Transient_Error);
        	break;        
        case Constants.CMD_NOTIFICATION_REQUEST :
        	response = new NotificationRequestResponse(this,ReturnCode.Transient_Error);
        	break;        	
        case Constants.CMD_NOTIFY :
        	response = new NotifyResponse(this,ReturnCode.Transient_Error);
        	break;        
        case Constants.CMD_RESP_UNKNOWN :
        	//FIXME - what response?!?
        	response = new NotifyResponse(this,ReturnCode.Transient_Error);
        	break;        	
        case Constants.CMD_RESTART_IN_PROGRESS :
        	response = new RestartInProgressResponse(this,ReturnCode.Transient_Error);
        	break;        	
        default :
        	throw new IllegalArgumentException("Could not send type of the message yet");
        }
		response.setTransactionHandle(command.getTransactionHandle());
        JainMgcpEvent[] events = {response};
        sendMgcpEvents(events);
	}

	public CallIdentifier getUniqueCallIdentifier() {
		long current = -1;
		boolean b = true;
		while(b) {
			current  = callIdentifierCounter.get();
			if (current == Long.MAX_VALUE) {
				b = !callIdentifierCounter.compareAndSet(current,1);
			}
			else {
				b = !callIdentifierCounter.compareAndSet(current, current+1);
			}
		}
		return new CallIdentifier(Long.toHexString(current));
	}

	public RequestIdentifier getUniqueRequestIdentifier() {
		long current = -1;
		boolean b = true;
		while(b) {
			current  = requestIdentifierCounter.get();
			if (current == Long.MAX_VALUE) {
				b = !requestIdentifierCounter.compareAndSet(current,1);
			}
			else {
				b = !requestIdentifierCounter.compareAndSet(current, current+1);
			}
		}
		return new RequestIdentifier(Long.toHexString(current));
	}
}
