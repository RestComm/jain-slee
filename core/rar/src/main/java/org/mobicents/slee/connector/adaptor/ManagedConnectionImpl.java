package org.mobicents.slee.connector.adaptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.connector.server.EventInvocation;
import org.mobicents.slee.connector.server.RemoteSleeService;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.transaction.xa.XAResource;

/**
 * Implementation of the ManagedConnection interface according to the JCA1.0
 * contract
 * 
 * @author Tim
 */
public class ManagedConnectionImpl implements ManagedConnection,
		LocalTransaction {

	private static Logger log = Logger.getLogger(ManagedConnectionImpl.class);
	private static ConnectionMetaDataImpl metaData = new ConnectionMetaDataImpl();
	private RemoteSleeService rmiStub;
	private LinkedList<ConnectionEventListener> listeners = new LinkedList<ConnectionEventListener>();
	private LinkedList<SleeConnectionImpl> connectionHandles = new LinkedList<SleeConnectionImpl>();
	private ArrayList<EventInvocation> eventQueue = new ArrayList<EventInvocation>();
	private boolean destroyed;
	private PrintWriter printWriter;
	private boolean inTransaction;

	ManagedConnectionImpl(RemoteSleeService rmiStub) {
		if (log.isDebugEnabled()) {
			log.debug("Creating ManagedConnectionImpl");
		}
		this.rmiStub = rmiStub;
	}

	/*
	 * Called by the connection manager to register interest in a connection
	 * 
	 * @see javax.resource.spi.ManagedConnection#addConnectionEventListener(javax.resource.spi.ConnectionEventListener)
	 */
	public void addConnectionEventListener(ConnectionEventListener listener) {
		if (log.isDebugEnabled()) {
			log.debug("addConnectionEventListener() called");
		}
		listeners.add(listener);
	}

	/*
	 * Remove a listener
	 * 
	 * @see javax.resource.spi.ManagedConnection#removeConnectionEventListener(javax.resource.spi.ConnectionEventListener)
	 */
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		if (log.isDebugEnabled()) {
			log.debug("removeConnectionEventListener() called");
		}
		listeners.remove(listener);
	}

	/*
	 * Move a handle from one physical connection to another
	 * 
	 * @see javax.resource.spi.ManagedConnection#associateConnection(java.lang.Object)
	 */
	public void associateConnection(Object connection) throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("associateConnection() called");
		}
		SleeConnectionImpl conn = (SleeConnectionImpl) connection;
		conn.getManagedConnection().removeConnectionHandle(conn);
		connectionHandles.add(conn);
		conn.setManagedConnection(this);
	}

	/*
	 * Client up client side state associated with the physical connection
	 * 
	 * @see javax.resource.spi.ManagedConnection#cleanup()
	 */
	public void cleanup() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("cleanUp() called on " + this);
		}
		// This is called to clean up any client-specific state associated with
		// this
		// physical connection. It should call invalidate on all the associated
		// connection handles
		Iterator iter = connectionHandles.iterator();
		while (iter.hasNext()) {
			SleeConnectionImpl handle = (SleeConnectionImpl) iter.next();
			handle.invalidate();
		}
		connectionHandles.clear();
	}

	/*
	 * Destroy the physical connection
	 * 
	 * @see javax.resource.spi.ManagedConnection#destroy()
	 */
	public void destroy() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("destroy() called on " + this);
		}
		if (destroyed)
			return;
		cleanup();
		destroyed = true;
	}

	/*
	 * Get a handle
	 * 
	 * @see javax.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject,
	 *      javax.resource.spi.ConnectionRequestInfo)
	 */
	public Object getConnection(Subject subject, ConnectionRequestInfo info)
			throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("getConnection() called on " + this);
		}
		if (destroyed)
			throw new IllegalStateException(
					"This ManagedConnection has already been destroyed!");
		// Create a new connection handle
		SleeConnectionImpl conn = new SleeConnectionImpl(this);
		connectionHandles.add(conn);
		return conn;
	}

	/*
	 * Get the meta-data
	 * 
	 * @see javax.resource.spi.ManagedConnection#getMetaData()
	 */
	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		return metaData;
	}

	/*
	 * We don't support XA transactions
	 * 
	 * @see javax.resource.spi.ManagedConnection#getXAResource()
	 */
	public XAResource getXAResource() throws ResourceException {
		// We don't support XA transactions
		return null;
	}

	/*
	 * Get the local transaction
	 * 
	 * @see javax.resource.spi.ManagedConnection#getLocalTransaction()
	 */
	public LocalTransaction getLocalTransaction() throws ResourceException {
		log.debug("getLocalTransaction() called");
		return this;
	}

	/*
	 * Not used
	 * 
	 * @see javax.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter pw) throws ResourceException {
		printWriter = pw;
	}

	/*
	 * Not used
	 * 
	 * @see javax.resource.spi.ManagedConnection#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws ResourceException {
		return printWriter;
	}

	/*
	 * Called by the transaction manager to start a local transaction
	 * 
	 * @see javax.resource.spi.LocalTransaction#begin()
	 */
	public void begin() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("begin() called on local transaction");
		}
		if (inTransaction) {
			throw new ResourceException(
					"begin() called on transaction already in progress");
		}
		if (this.eventQueue.size() != 0) {
			throw new IllegalStateException(
					"begin() called on transaction but events already in queue!");
		}
		// do stuff
		inTransaction = true;
		// sendNotification(new ConnectionEvent(this,
		// ConnectionEvent.LOCAL_TRANSACTION_STARTED));
	}

	/*
	 * Called by the transaction manager to commit a local tranaction
	 * 
	 * @see javax.resource.spi.LocalTransaction#commit()
	 */
	public void commit() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("commit() called on local transaction");
		}
		if (!inTransaction) {
			throw new ResourceException(
					"commit() called on transaction but transaction not started");
		}
		// Send any queued events

		sendQueuedEvents();

		eventQueue.clear();

		inTransaction = false;

		// sendNotification(new ConnectionEvent(this,
		// ConnectionEvent.LOCAL_TRANSACTION_COMMITTED));
	}

	/*
	 * Called by the transaction manager to rollback a local transaction
	 * 
	 * @see javax.resource.spi.LocalTransaction#rollback()
	 */
	public void rollback() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("rollback() called on local transaction");
		}
		if (!inTransaction) {
			throw new ResourceException(
					"rollback() called on transaction but transaction not started");
		}
		// Clear the event queue
		this.eventQueue.clear();
		inTransaction = false;
	}

	private void removeConnectionHandle(SleeConnectionImpl connectionHandle) {
		if (log.isDebugEnabled()) {
			log.debug("removeConnectionHandle() called");
		}
		connectionHandles.remove(connectionHandle);
	}

	private void sendNotification(ConnectionEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("sendNotification() called with " + event + " eventID = "
					+ event.getId());
		}
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ConnectionEventListener listener = (ConnectionEventListener) iter
					.next();
			switch (event.getId()) {

			case ConnectionEvent.CONNECTION_CLOSED:
				listener.connectionClosed(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
				listener.localTransactionStarted(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
				listener.localTransactionCommitted(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
				listener.localTransactionRolledback(event);
				break;
			case ConnectionEvent.CONNECTION_ERROR_OCCURRED:
				listener.connectionErrorOccurred(event);
				break;
			default:
				throw new IllegalStateException("Invalid event id:"
						+ event.getId());
			}
		}
	}

	void handleClosed(SleeConnectionImpl handle) {
		if (log.isDebugEnabled()) {
			log.debug("handleClosed() called");
		}
		if (destroyed)
			throw new IllegalStateException(
					"Attempt to close a handle on a destroyed connection!");
		ConnectionEvent event = new ConnectionEvent(this,
				ConnectionEvent.CONNECTION_CLOSED);
		event.setConnectionHandle(handle);
		sendNotification(event);
		connectionHandles.remove(handle);
	}

	void connectionError(SleeConnectionImpl handle, Exception e) {
		if (log.isDebugEnabled()) {
			log.debug("connectionError() called");
		}
		if (destroyed)
			throw new IllegalStateException(
					"Attempt to signal a conection error on a destroyed connection!");
		ConnectionEvent event = new ConnectionEvent(this,
				ConnectionEvent.CONNECTION_ERROR_OCCURRED);
		event.setConnectionHandle(handle);
		sendNotification(event);
	}

	/* This method is non-transactional */
	ExternalActivityHandle createActivityHandle() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("createActivityHandle() called");
		}
		if (destroyed)
			throw new IllegalStateException("Connection is destroyed!");
		try {
			return rmiStub.createActivityHandle();
		} catch (RemoteException e) {
			String s = "Failed to invoke createActivityHandle";
			log.error(s, e);
			ResourceException ex = new ResourceException(s);
			ex.setLinkedException(e);
			throw ex;
		}
	}

	/* This method is non-transactional */
	EventTypeID getEventTypeID(String name, String vendor, String version)
			throws ResourceException, UnrecognizedEventException {
		if (log.isDebugEnabled()) {
			log.debug("getEventTypeID called:" + name + "," + vendor + ","
					+ version);
		}
		if (destroyed)
			throw new IllegalStateException("Connection is destroyed!");
		try {
			return rmiStub.getEventTypeID(name, vendor, version);
		} catch (RemoteException e) {
			String s = "Failed to invoke getEventTypeID";
			log.error(s, e);
			ResourceException ex = new ResourceException(s);
			ex.setLinkedException(e);
			throw ex;
		}
	}

	/*
	 * This method is transactional. If this method is invoked while a
	 * transaction is in progress, then the events aren't actually fired on the
	 * SLEE until the transaction commits. See JAIN SLEE 1.0 spec. section F.2.
	 * Therefore we need to queue the events internally and fire them all on the
	 * commit, if we're in a transaction. We don't queue them on the server side
	 * due to difficulty in clustering (we would have to cluster the queued
	 * events)
	 */
	void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address)
			throws ResourceException, NullPointerException,
			UnrecognizedEventException {
		if (log.isDebugEnabled()) {
			log.debug("fireEvent() called:" + event + "," + eventType + ","
					+ activityHandle + "," + address);
		}
		if (destroyed)
			throw new IllegalStateException("Connection is destroyed!");
		if (!this.inTransaction) {
			fireEventNow(event, eventType, activityHandle, address);
		} else {
			fireEventLater(event, eventType, activityHandle, address);
		}
	}

	private void fireEventNow(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address)
			throws ResourceException, NullPointerException,
			UnrecognizedEventException {
		if (log.isDebugEnabled()) {
			log.debug("Firing single event on SLEE:" + event);
		}
		try {
			this.rmiStub.fireEvent(event, eventType, activityHandle, address);
		} catch (RemoteException e) {
			String s = "Failed to invoke fireEvent";
			log.error(s, e);
			ResourceException ex = new ResourceException(s);
			ex.setLinkedException(e);
			throw ex;
		}
	}

	private void fireEventLater(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address) {
		// Just stick the invocation on the queue
		if (log.isDebugEnabled()) {
			log.debug("Adding event to event queue");
		}
		EventInvocation ei = new EventInvocation(event, eventType,
				activityHandle, address);
		this.eventQueue.add(ei);
	}

	/*
	 * Actually send any queued events to the SLEE.
	 */
	private void sendQueuedEvents() throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("Firing queue of events on SLEE: "
					+ this.eventQueue.size());
		}
		try {
			for (EventInvocation ei : eventQueue) {
				fireEventNow(ei.event, ei.eventTypeId,
						ei.externalActivityHandle, ei.address);
			}
		} catch (NullPointerException e) {
			throw new ResourceException(e.getMessage(), e);
		} catch (UnrecognizedEventException e) {
			throw new ResourceException(e.getMessage(), e);
		}
	}
}