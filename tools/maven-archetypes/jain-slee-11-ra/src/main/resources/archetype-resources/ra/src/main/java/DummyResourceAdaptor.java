package ${package};

import javax.slee.resource.ActivityHandle;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.ConfigProperties.Property;
import javax.transaction.SystemException;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//ft import
import org.mobicents.slee.resource.cluster.*;

//comment for nonFT/clustered RA
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;

/**
 * 
 * dummy Ra. it reads 192B from socket, converts to string and uses it as
 * activig handle. Fires event based on '1' count in data
 * 
 */
public class DummyResourceAdaptor implements
		FaultTolerantResourceAdaptor<DummyActivityHandle, DummyActivity> {
	// change to ResourceAdaptor if FT is not requried
	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private final EventIDFilter eventIDFilter = new EventIDFilter();
	// some static used to fire event; -----------------------
	private final static String _EVENT_NAME_PREFIX = "${package}.Event_";
	private final static String _EVENT_NAME_VERSION = "${version}";
	private final static String _EVENT_NAME_VENDOR = "${groupId}";

	// flags ----------------------------------------
	/**
	 * for all events we are interested in knowing when the event failed to be
	 * processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	// standard vars --------------------------------
	private ResourceAdaptorContext raContext;
	private SleeEndpoint sleeEndpoint;
	private EventLookupFacility eventLookupFacility;

	// Replicated DS ------------------------------
	// not efficient, but it shows how to use it.
	private ReplicatedData<DummyActivityHandle, DummyActivity> activities;

	/**
	 * tracer
	 */
	private Tracer tracer;

	private DummyProviderImpl provider;

	// Config Properties Names -------------------------------------------

	private static final String _BIND_ADDRESS = "address";

	private static final String _BIND_PORT = "port";

	// Config Properties Values -------------------------------------------

	private String localAddress;
	private int localPort;
	private InetAddress bindAddress;

	// Connection ------------------------------------------------------

	// we need this since lower layer is very time sensitivie, lets deliver on
	// different thread.
	// private ExecutorService executor = Executors.newFixedThreadPool(5);
	// client part for streaming
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel channel;

	private Selector readSelector;
	private Selector connectSelector;
	private ByteBuffer readBuff = ByteBuffer.allocate(192);
	private ExecutorService streamExecutor = Executors
			.newSingleThreadExecutor();
	private Future streamFuture;
	private Runner runner;
	private boolean connected = false;

	// Connection operations --------------------------------------------

	private void performKeyOperations(Iterator selectedKeys) throws IOException {

		while (selectedKeys.hasNext()) {

			SelectionKey key = (SelectionKey) selectedKeys.next();
			selectedKeys.remove();

			if (!key.isValid()) {
				// handle disconnect here?
				continue;
			}

			// Check what event is available and deal with it
			if (key.isAcceptable()) {

				this.accept(key);
			} else if (key.isReadable()) {

				this.read(key);
			} // else if (key.isWritable()) {
			//	
			// this.write(key);
			// }
		}

	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Attempt to read off the channel
		int numRead = -1;
		try {
			numRead = socketChannel.read(this.readBuff);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			handleClose(key);
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			handleClose(key);
			return;
		}
		// pass it on.
		if (this.readBuff.remaining() == 0) {
			this.readBuff.flip();
			byte[] b = new byte[this.readBuff.limit()];
			this.readBuff.get(b);
			this.readBuff.clear();
			String handleId = new String(b);

			DummyActivity da = this.provider.getOrCreate(handleId);

			fire(da);
		} else {
			// read more?
		}

		this.readBuff.clear();
		// this.layer3.send(si, ssf, this.readBuff.array());

	}

	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		if (connected) {
			serverSocketChannel.close();
			return;
		}

		channel = serverSocketChannel.accept();
		Socket socket = channel.socket();
		channel.configureBlocking(false);

		channel.register(this.readSelector, SelectionKey.OP_READ);

		connected = true;
		if (tracer.isInfoEnabled()) {
			tracer.info("Estabilished connection with: "
					+ socket.getInetAddress() + ":" + socket.getPort());

		}
	}

	private void handleClose(SelectionKey key) throws IOException {
		try {
			SocketChannel socketChannel = (SocketChannel) key.channel();
			key.cancel();
			socketChannel.close();

		} finally {
			connected = false;
			// synchronized (this.txBuffer) {

		}
		return;
	}

	// Fire method ------------------------

	private void fire(DummyActivity da) {

		byte[] bits = da.getHandle().getHandleId().getBytes();
		int bCount = 0;
		for (byte b : bits) {
			for (int i = 0; i < 8; i++) {
				int x = (b >> i) & 0x01;
				if (x == 1) {
					bCount++;
				}
			}

		}

		// NOTE 6 is number of events we define, so we get num here.
		bCount = bCount % 6;
		String name = _EVENT_NAME_PREFIX + bCount;
		EventTypeID eventId = new EventTypeID(name, _EVENT_NAME_VENDOR,
				_EVENT_NAME_VERSION);
		try {
			FireableEventType fet = getEventLookupFacility()
					.getFireableEventType(eventId);

			if (eventIDFilter.filterEvent(fet)) {
				if (tracer.isFineEnabled()) {
					tracer.fine("Event "
							+ (fet == null ? "null" : fet.getEventType())
							+ " filtered.");
				}
				return;
			}
			try {
				getSleeEndpoint().fireEvent(da.getHandle(), fet,
						new DummyEvent(), null, null, DEFAULT_EVENT_FLAGS);
			} catch (Throwable e) {
				tracer.severe("Failed to fire event", e);
			}
		} catch (Throwable e) {
			tracer.severe("Failed to fire event", e);
		}
	}

	// LIFECYLE

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		try {
			this.provider = new DummyProviderImpl();
			this.readSelector = SelectorProvider.provider().openSelector();

			this.connectSelector = SelectorProvider.provider().openSelector();
			// Create a new non-blocking server socket channel
			this.serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);

			// Bind the server socket to the specified address and port
			InetSocketAddress isa = new InetSocketAddress(this.bindAddress,
					this.localPort);
			serverSocketChannel.socket().bind(isa);

			// Register the server socket channel, indicating an interest in
			// accepting new connections
			serverSocketChannel.register(this.connectSelector,
					SelectionKey.OP_ACCEPT);
			this.runner = new Runner();
			this.streamFuture = this.streamExecutor.submit(this.runner);
			tracer.info("Initiaited server on: " + this.bindAddress + ":"
					+ this.localAddress);
		} catch (Exception ex) {
			String msg = "error in initializing resource adaptor";
			tracer.severe(msg, ex);
			throw new RuntimeException(msg, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		if (this.streamFuture != null) {
			try {
				this.streamFuture.cancel(false);
				this.streamFuture = null;
				this.serverSocketChannel.close();
				this.serverSocketChannel = null;
				this.readSelector.close();
				this.readSelector = null;
				this.connectSelector.close();
				this.connectSelector = null;
				this.channel.close();
				this.channel = null;
				this.runner = null;
			} catch (Exception ex) {
				String msg = "error in initializing resource adaptor";
				tracer.severe(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		} else {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {

	}

	// EVENT PROCESSING CALLBACKS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.
	 * resource.ActivityHandle, javax.slee.resource.FireableEventType,
	 * java.lang.Object, javax.slee.Address,
	 * javax.slee.resource.ReceivableService, int,
	 * javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle ah,
			FireableEventType arg1, Object event, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee
	 * .resource.ActivityHandle, javax.slee.resource.FireableEventType,
	 * java.lang.Object, javax.slee.Address,
	 * javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource
	 * .ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle handle,
			FireableEventType eventType, Object event, Address arg3,
			ReceivableService arg4, int arg5) {

	}

	// RA CONFIG

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.
	 * resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties properties) {
		//this should stop and rebind sockets!
		raConfigure(properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.
	 * ConfigProperties)
	 */
	public void raConfigure(ConfigProperties properties) {
		tracer.config("RA entity named " + raContext.getEntityName()
				+ " is beeing configured");
		Integer v = (Integer) properties.getProperty(_BIND_PORT).getValue();
		if (v == null) {
			localPort = 10240;
		} else {
			localPort =v;
		}

		String s = (String) properties.getProperty(_BIND_ADDRESS).getValue();
		if (s == null) {
			this.localAddress = "127.0.0.1";
		} else {
			this.localAddress = s;
		}
		tracer.config("RA entity named " + raContext.getEntityName()
				+ " configured");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.
	 * resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		//called before raConfigure
		try {
			int localPort;
			Integer v = (Integer) properties.getProperty(_BIND_PORT).getValue();
			if (v == null) {
				localPort = 10240;
			} else {
				localPort =v;
			}

			String s = (String) properties.getProperty(_BIND_ADDRESS).getValue();
			String localAddress;
			if (s == null) {
				this.localAddress = "127.0.0.1";
			} else {
				this.localAddress = s;
			}
			this.bindAddress = InetAddress.getByName(this.localAddress);
			// try to open socket
			InetSocketAddress sockAddress = new InetSocketAddress(localAddress,
					localPort);
			new DatagramSocket(sockAddress).close();
			// check transports
			tracer.config("RA entity named " + raContext.getEntityName()
					+ " verivied availability of net resources");
		} catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(), e);
		}
	}

	// EVENT FILTERING

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);

	}

	// RA CONTEXT

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee
	 * .resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.raContext = raContext;
		this.tracer = raContext.getTracer("DummyresourceAdaptor");
		this.sleeEndpoint = raContext.getSleeEndpoint();
		this.eventLookupFacility = raContext.getEventLookupFacility();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
	}

	/**
	 * 
	 * @return
	 */
	public EventLookupFacility getEventLookupFacility() {
		return eventLookupFacility;
	}

	/**
	 * 
	 * @return
	 */
	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	/**
	 * 
	 * @param tracerName
	 * @return
	 */
	public Tracer getTracer(String tracerName) {
		return raContext.getTracer(tracerName);
	}

	// ACTIVITY MANAGEMENT

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void activityEnded(ActivityHandle activityHandle) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle activityHandle) {
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.
	 * ActivityHandle)
	 */
	public Object getActivity(ActivityHandle handle) {
		if (handle instanceof DummyActivityHandle) {
			final DummyActivityHandle h = (DummyActivityHandle) handle;
			return this.activities.get(h);
		}
		return null;
	}

	private void endActivity(final DummyActivity da) {

		try {
			this.sleeEndpoint.endActivity(da.getHandle());
			this.activities.remove(da.getHandle());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return;
	}

	private void startActivity(final DummyActivity da) {
		try {
			this.sleeEndpoint.startActivity(da.getHandle(), da);
			this.activities.put(da.getHandle(), da);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object activity) {
		if (activity instanceof DummyActivity) {
			return ((DummyActivity) activity).getHandle();
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {

	}

	// OTHER GETTERS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.
	 * lang.String)
	 */
	public Object getResourceAdaptorInterface(String raTypeSbbInterfaceclassName) {
		// this ra implements a single ra type
		return this.provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	// CLUSTERING

	/**
	 * Indicates if the RA is running in local mode or in a clustered
	 * environment
	 * 
	 * @return true if the RA is running in local mode, false if is running in a
	 *         clustered environment
	 */
	public boolean inLocalMode() {
		if (this.ftRaContext != null) {
			return this.ftRaContext.isLocal();
		}
		return true;
	}

	private FaultTolerantResourceAdaptorContext<DummyActivityHandle, DummyActivity> ftRaContext;

	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<DummyActivityHandle, DummyActivity> context) {
		this.ftRaContext = context;
		this.activities = ftRaContext.getReplicateData();
	}

	public void unsetFaultTolerantResourceAdaptorContext() {
		this.ftRaContext = null;
	}

	public void failOver(DummyActivityHandle handle) {
		DummyActivity da = this.activities.get(handle);
		// for demonstration we set this stuff.
		da.setLocalAddress(localAddress);
		da.setLocalPort(localPort + "");

	}

	// inner class:

	private class DummyProviderImpl implements DummyProvider {

		DummyActivity getOrCreate(String dhh) {
			DummyActivityHandle _dhh = new DummyActivityHandle(dhh);
			DummyActivity da = activities.get(_dhh);
			if (da == null) {
				da = this.createDummyActivity(dhh);
				da.setLocalAddress(localAddress);
				da.setLocalPort(localPort + "");
			}
			return da;
		}

		private DummyActivity createDummyActivity(String dhh) {
			DummyActivity da = new DummyActivity(dhh, this);

			startActivity(da);
			return da;
		}

		public DummyActivity createDummyActivity() {
			byte[] b = new byte[readBuff.limit()];
			// fill
			return this.createDummyActivity(new String(b));
		}

		public void terminateActivity(DummyActivity dummyActivity) {
			endActivity(dummyActivity);

		}
	}

	private class Runner implements Runnable {
		public void run() {
			while (true) {
				try {

					Iterator selectedKeys = null;

					// Wait for an event one of the registered channels
					if (!connected) {

						// block till we have someone subscribing for data.
						connectSelector.select();

						selectedKeys = connectSelector.selectedKeys()
								.iterator();
						// operate on keys set
						performKeyOperations(selectedKeys);

					} else {
						// else we try I/O ops.

						if (readSelector.selectNow() > 0) {
							selectedKeys = readSelector.selectedKeys()
									.iterator();
							// operate on keys set

							performKeyOperations(selectedKeys);

						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
