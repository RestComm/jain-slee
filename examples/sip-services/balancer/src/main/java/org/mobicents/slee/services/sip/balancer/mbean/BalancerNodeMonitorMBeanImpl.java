package org.mobicents.slee.services.sip.balancer.mbean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask; //import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.slee.management.ResourceAdaptorEntityState;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.services.sip.balancer.BalancerMessage;
import org.mobicents.slee.services.sip.balancer.BalancerMessageImpl;
import org.mobicents.slee.services.sip.balancer.BalancerMessageType;
import org.mobicents.slee.services.sip.balancer.NodeRegisterRMIStub;

public class BalancerNodeMonitorMBeanImpl extends ServiceMBeanSupport implements
		BalancerNodeMonitorMBeanImplMBean {

	private static final Logger logger = Logger
			.getLogger(BalancerNodeMonitorMBeanImpl.class.getCanonicalName());

	//private Object _BALANCER_NAMES_OP_LOCK = new Object();
	private List<String> balancerNames = new ArrayList<String>();
	private Map<String, AddressHolder> register = new HashMap<String, AddressHolder>();
	//private Map<String, Socket> connections = new HashMap<String, Socket>();

	private List<String> mbeanRegexes = Collections
			.synchronizedList(new ArrayList<String>());

	private long heartBeatInterval = 5000;

	// Some state.
	private boolean cnfRead = false;

	// ---------------- SETTERS

	public long getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(long heartBeatInterval) {
		if (heartBeatInterval < 100)
			return;
		this.heartBeatInterval = heartBeatInterval;
		this.hearBeatTaskToRun.cancel();
		this.hearBeatTaskToRun = new BalancerPingTimerTask(super.server);
		this.heartBeatTimer.scheduleAtFixedRate(this.hearBeatTaskToRun, 0,
				this.heartBeatInterval);

	}

	private InetAddress fetchHostAddress(String hostName, int index) {
		if (hostName == null)
			throw new NullPointerException("Host name cant be null!!!");

		InetAddress[] hostAddr = null;
		try {
			hostAddr = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException uhe) {
			throw new IllegalArgumentException(
					"HostName is not a valid host name or it doesnt exists in DNS",
					uhe);
		}

		if (index < 0 || index >= hostAddr.length) {
			throw new IllegalArgumentException(
					"Index in host address array is wrong, it should be [0]<x<["
							+ hostAddr.length + "] and it is [" + index + "]");
		}

		InetAddress address = hostAddr[index];
		return address;
	}

	public boolean addRAMBeanRegex(String regex)
			throws IllegalArgumentException, NullPointerException {

		if (regex == null)
			throw new NullPointerException("regex cant be null!!");

		try {
			ObjectName on = new ObjectName(regex);
			// TODO: Add some more strict checking....
			if (mbeanRegexes.contains(regex))
				return false;
			else {
				mbeanRegexes.add(regex);
				return true;
			}
		} catch (MalformedObjectNameException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("regex[" + regex
					+ "] does not follow ObjectName convention!!!!");
		}

	}

	public List<String> getBalancers() {

		return new ArrayList<String>(this.balancerNames);
	}

	public List<String> getRABeansRegex() {

		return new ArrayList<String>(this.mbeanRegexes);
	}

	public void removeBalancerAddress(int index)
			throws IllegalArgumentException {

		logger.debug("[removeBalancerAddress]");

			if (index < 0 || index >= this.balancerNames.size())
				throw new IllegalArgumentException(
						"Index is wrong, it should be [0]<x<["
								+ balancerNames.size() + "] and it is ["
								+ index + "]");

			// This code is clone, as this is ultimate cleaner method to get rid
			// of
			// errors
			String balancerName = null;

			balancerName = balancerNames.get(index);

			balancerNames.remove(balancerName);
			register.remove(balancerName);

			// balancerInfoSources.remove(balancerName);

		
		
		logger.debug("[removeBalancerAddress] END");
	}

	public boolean removeRAMBeanRegex(String regex) throws NullPointerException {

		if (regex == null)
			throw new NullPointerException("Arg can not be null!!!!");

		if (this.mbeanRegexes.contains(regex)) {
			this.mbeanRegexes.remove(regex);
			return true;
		} else {
			return false;
		}

	}

	public void removeRAMBeanRegex(int index) throws IllegalArgumentException {

		if (index < 0 || index >= this.mbeanRegexes.size())
			throw new IllegalArgumentException(
					"Index is wrong, it should be [0]<x<["
							+ mbeanRegexes.size() + "] and it is [" + index
							+ "]");

		mbeanRegexes.remove(index);

	}

	//private void sendMessage(Socket s, BalancerMessageType type, Object content)
	//		throws IOException {
	//	if (s == null)
	//		return;
	//	BalancerMessageImpl msg = new BalancerMessageImpl(content, type);
	//	ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
	//	oos.writeObject(msg);
	//	oos.flush();

	//}

	class AddressHolder {
		InetAddress address = null;
		// int port = -1;

	}

	// ***************** SERVICE PART....

	// uS between pinging balancers.
	private Timer heartBeatTimer = new Timer();
	//private Timer infoPullTimer = new Timer();
	private TimerTask hearBeatTaskToRun = null;
	//private TimerTask infoPullTaskToRun = null; // does one taks is enough?

	//private Timer generalTaskTimer = new Timer();

	class BalancerPingTimerTask extends TimerTask {

		private MBeanServer server = null;
		private ObjectName resourceMgmt = null;

		@SuppressWarnings("unchecked")
		@Override
		public void run() {

			logger.debug("[BalancerPingTimerTask] Start");
			ArrayList<SIPNode> info = new ArrayList<SIPNode>();
			logger.debug("[BalancerPingTimerTask] running for [" + mbeanRegexes
					+ "]");
			// We have to prepare list :]
			// for (String s : mbeanRegexes) {
			for (int i = 0; i < mbeanRegexes.size(); i++) {
				String s = mbeanRegexes.get(i);
				logger.debug("[BalancerPingTimerTask] Regex[" + s + "]");
				// FIXME:If there is error we dont care..?
				try {
					ObjectName on = new ObjectName(s);
					Set<ObjectInstance> mbeans = server.queryMBeans(on, null);
					logger
							.debug("[BalancerPingTimerTask] Got object instances");
					for (ObjectInstance oi : mbeans) {
						logger.debug("[BalancerPingTimerTask] ["
								+ oi.getObjectName() + "]");
						try {

							// State check, we want only active ras
							String entityName = (String) server.invoke(oi
									.getObjectName(), "getEntityName", null,
									null);

							ResourceAdaptorEntityState state = (ResourceAdaptorEntityState) server
									.invoke(this.resourceMgmt, "getState",
											new Object[] { entityName },
											new String[] { "java.lang.String" });
							// String getStackAddress();
							if (!state.equals(state.ACTIVE)) {
								logger.warn("[BalancerPingTimerTask] Entity["
										+ entityName
										+ "] is not active, skipping");
								continue;
							}
							String address = (String) server.invoke(oi
									.getObjectName(), "getStackAddress", null,
									null);

							// int getStackPort();
							int port = (Integer) server.invoke(oi
									.getObjectName(), "getStackPort", null,
									null);
							// String[] getTransport();
							String[] transports = (String[]) server.invoke(oi
									.getObjectName(), "getTransport", null,
									null);

							String hostName = null;
							try {
								InetAddress[] aArray = InetAddress
										.getAllByName(address);
								if (aArray != null && aArray.length > 0) {
									// Damn it, which one we should pick?
									hostName = aArray[0].getCanonicalHostName();
								}
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							SIPNode node = new SIPNode(hostName, address, port,
									transports);

							info.add(node);

							// FIXME: IF ANY EXC happens, we dont care?
						} catch (InstanceNotFoundException e) {

							e.printStackTrace();
						} catch (MBeanException e) {

							e.printStackTrace();
						} catch (ReflectionException e) {

							e.printStackTrace();
						}

					}

				} catch (MalformedObjectNameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			for(AddressHolder ah:new HashSet<AddressHolder>(register.values()))
			{
				try {
					Registry registry = LocateRegistry.getRegistry(ah.address.getHostAddress());
					NodeRegisterRMIStub reg=(NodeRegisterRMIStub) registry.lookup("SIPBalancer");
					reg.handlePing(info);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			logger.debug("[BalancerPingTimerTask] Finished gathering");
			// To make it quicker, we dont need convert it on each write...
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// ObjectOutputStream oos1;
			logger.debug("[BalancerPingTimerTask] Gathered info[" + info + "]");
			// BalancerMessageImpl msg = new BalancerMessageImpl(info,
			// BalancerMessageType._Ping);
			// try {

			// oos1 = new ObjectOutputStream(baos);
			// oos1.writeObject(msg);
			// oos1.flush();

			// } catch (IOException e) {
			// TODO: FIX THIS?
			// logger.error(" CRITICAL ERROR, CANT CREATE WRITE STREAM....");
			// e.printStackTrace();
			// as we cant do much here in this case ...
			// return;
			// }

			// byte[] infoAsBytes = baos.toByteArray();
			// logger.debug("[BalancerPingTimerTask] GOT BYTES["
			// + baos.toByteArray().length + "] FOR["
			// + connections.values() + "]");
			// Now, we have to send it....

			// try {

			// for (Socket s : connections.values()) {
			// if (s == null)
			// return;
			// if (s.isConnected()) {// This is BS - this value DOES NOT
			// // CHANGE!!!!!
			// logger.debug("[BalancerPingTimerTask] RUNNING FOR[" + s
			// + "]");
			// try {
			// OutputStream os = s.getOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(os);
			// oos.writeObject(msg);
			// } catch (IOException e) {

			// if other side dies isConnected and isClosed
			// will
			// show
			// true and false .... ;/
			// logger
			// .debug("[BalancerPingTimerTask] Failed to send for["
			// / + s
			// + "] reason["
			// + e.getMessage()
			// + "], removing this connection");
			// connections.values().remove(s); // This can cause
			// // ConcurrentModificationException
			//
			// }
			// }

			// }

			// } catch (ConcurrentModificationException cme) {
			// cme.printStackTrace();
			// This can happen when connection has been removed in

			// }

		}

		public BalancerPingTimerTask(MBeanServer server) {
			super();
			this.server = server;
			try {
				this.resourceMgmt = new ObjectName(
						"slee:name=ResourceManagementMBean");
			} catch (MalformedObjectNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class SocketRemoveTimerTask extends TimerTask {

		private Socket s = null;

		@Override
		public void run() {

			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public SocketRemoveTimerTask(Socket s) {
			super();
			this.s = s;
		}

	}

	@Override
	protected void startService() throws Exception {

		super.startService();

		if (!cnfRead) {

			Properties props = new Properties();

			try {
				props.load(this.getClass().getResourceAsStream(
						"bean.properties"));
			} catch (IOException ioe) {
				logger
						.error("!! Failed to load properties from file [bean.properties]");
			}

			if (props.containsKey("balancers")) {

				String[] tmp = ((String) props.get("balancers")).split(";");
				for (String addrs : tmp) {

					String[] parts = addrs.split(":");
					if (parts.length != 2) {
						logger
								.error("Wrong format of address, should be ip/name:port and is ["
										+ addrs + "]. Skipping this entry");
						continue;

					}

					int port = -1;
					try {
						port = Integer.parseInt(parts[1]);
					} catch (NumberFormatException nfe) {

						logger
								.error("Wrong format of port part, should be 0<x<65k and is ["
										+ parts[1] + "]. Skipping this entry");
						continue;
					}

					// this.addBalancerAddress(parts[0], port, 0);
				}

			}
			logger.debug("[Bean] Fetching regex");
			logger.debug("[Bean] " + props);
			if (props.containsKey("bean.regex")) {
				String[] tmp = ((String) props.get("bean.regex")).split(";");
				logger.debug("[Bean] Regex tab:" + tmp);
				for (String regex : tmp) {
					logger.debug("[Bean] Trying to add regex[" + regex + "]");
					try {
						// ObjectName on = new ObjectName(regex);
						this.addRAMBeanRegex(regex);

					} catch (Exception e) {
						logger.error("Cant add [" + regex
								+ "] as it seems to be of wrong format: "
								+ e.getMessage());
						continue;
					}

				}

			}

			cnfRead = true;
		}

		logger.debug("[Bean] creating tasks");
		this.hearBeatTaskToRun = new BalancerPingTimerTask(super.server);

		this.heartBeatTimer.scheduleAtFixedRate(this.hearBeatTaskToRun, 0,
				this.heartBeatInterval);

		logger.debug("[Bean] Created and scheduled tasks.");

	}

	@Override
	protected void stopService() throws Exception {

		this.hearBeatTaskToRun.cancel();
		this.hearBeatTaskToRun = null;

		super.stopService();
	}

	public boolean addBalancerAddress(byte[] addr)
			throws IllegalArgumentException, NullPointerException, IOException {
		if (addr == null)
			throw new NullPointerException("addr cant be null!!!");

		if (addr.length != 4)
			throw new IllegalArgumentException("addr.length!=4");

		InetAddress address = null;
		try {
			address = InetAddress.getByAddress(addr);
		} catch (UnknownHostException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Somethign wrong with host creation.", e);
		}

		String balancerName = address.getCanonicalHostName();

		if (balancerNames.contains(balancerName))
			return false;

		balancerNames.add(balancerName);
		AddressHolder ah = new AddressHolder();

		ah.address = address;
		// ah.port = port;

		register.put(balancerName, ah);

		return true;
	}

	public boolean addBalancerAddress(String hostName, int index)
			throws IllegalArgumentException, NullPointerException, IOException {
		return this.addBalancerAddress(fetchHostAddress(hostName, index)
				.getAddress());
	}

	public boolean removeBalancerAddress(byte[] addr)
			throws IllegalArgumentException, NullPointerException {
		if (addr == null)
			throw new NullPointerException("addr cant be null!!!");

		if (addr.length != 4)
			throw new IllegalArgumentException("addr.length!=4");

		InetAddress address = null;
		try {
			address = InetAddress.getByAddress(addr);
		} catch (UnknownHostException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Something wrong with host creation.", e);
		}

		String balancerName = address.getCanonicalHostName();

		if (!balancerNames.contains(balancerName))
			return false;

		this.removeBalancerAddress(this.balancerNames.indexOf(balancerName));

		return true;
	}

	public boolean removeBalancerAddress(String hostName, int index)
			throws IllegalArgumentException, NullPointerException {
		InetAddress[] hostAddr = null;
		try {
			hostAddr = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException uhe) {
			throw new IllegalArgumentException(
					"HostName is not a valid host name or it doesnt exists in DNS",
					uhe);
		}

		if (index < 0 || index >= hostAddr.length) {
			throw new IllegalArgumentException(
					"Index in host address array is wrong, it should be [0]<x<["
							+ hostAddr.length + "] and it is [" + index + "]");
		}

		InetAddress address = hostAddr[index];

		return this.removeBalancerAddress(address.getAddress());
	}

	public BalancerNodeMonitorMBeanImpl() {
		super(BalancerNodeMonitorMBeanImplMBean.class);

	}

	// *********** SOCKET VERSION METHODS

	// ---------------- OPERATIONS
	// public boolean addBalancerAddress(byte[] addr, int port)
	// throws IllegalArgumentException, NullPointerException, IOException {

	// if (addr == null)
	// throw new NullPointerException("addr cant be null!!!");

	// if (addr.length != 4)
	// throw new IllegalArgumentException("addr.length!=4");
	// if (port < 0)
	// throw new IllegalArgumentException("port value is wrong");
	// InetAddress address = null;
	// try {
	// address = InetAddress.getByAddress(addr);
	// } catch (UnknownHostException e) {

	// e.printStackTrace();
	// throw new IllegalArgumentException(
	// "Somethign wrong with host creation.", e);
	// }

	// String balancerName = address.getCanonicalHostName() + ":" + port;

	// synchronized (_BALANCER_NAMES_OP_LOCK) {
	// if (balancerNames.contains(balancerName))
	// return false;

	// balancerNames.add(balancerName);
	// AddressHolder ah = new AddressHolder();

	// ah.address = address;
	// ah.port = port;

	// register.put(balancerName, ah);

	// boolean noException = false;
	// try {
	// Socket s = new Socket(ah.address, ah.port);
	// connections.put(balancerName, s);
	// balancerInfoSources.put(balancerName, new ObjectInputStream(s
	// .getInputStream()));

	// noException = true;
	// } finally {
	// if (!noException) {
	// We need to clean...

	// balancerNames.remove(balancerName);
	// register.remove(balancerName);

	// balancerInfoSources.remove(balancerName);
	// reduntant, but for
	// sake...
	// Socket s = connections.remove(balancerName);
	// if (s != null) {
	// try {
	// s.close();
	// } catch (Exception e) {
	// }
	// }
	// }

	// }
	// }

	// return true;

	// }

	// public boolean addBalancerAddress(String hostName, int port, int index)
	// throws IllegalArgumentException, NullPointerException, IOException {

	// return this.addBalancerAddress(fetchHostAddress(hostName, index)
	// .getAddress(), port);
	// }

	// public void connectoToBalancer(int index) throws
	// IllegalArgumentException,
	// IllegalStateException, IOException {

	// logger.debug("[connectoToBalancer] Locking _BALANCER_NAMES_OP_LOCK");
	// synchronized (_BALANCER_NAMES_OP_LOCK) {
	// if (index < 0 || index >= balancerNames.size())
	// throw new IllegalArgumentException(
	// "Index out of bounds, no balancer for this index!!!");

	// if (this.isConnectedToBalancer(index))
	// throw new IllegalStateException("Already connected to "
	// + balancerNames.get(index) + "!!!");

	// boolean noException = false;

	// String balancerName = balancerNames.get(index);
	// AddressHolder ah = register.get(balancerName);
	// try {
	// Socket s = new Socket(ah.address, ah.port);
	// connections.put(balancerNames.get(index), s);
	// noException = true;
	// } finally {
	// if (!noException) {
	// // We need to clean...
	// balancerNames.remove(balancerName);
	// register.remove(balancerName);
	// connections.remove(balancerName);// reduntant, but for
	// sake...
	// }
	// }
	// }
	// logger.debug("[connectoToBalancer] UNLocking _BALANCER_NAMES_OP_LOCK");

	// }

	// public void disconnectFromBalancer(int index)
	// throws IllegalArgumentException, IllegalStateException {

	// logger.debug("[connectoToBalancer] Locking _BALANCER_NAMES_OP_LOCK");
	// synchronized (_BALANCER_NAMES_OP_LOCK) {
	// if (index < 0 || index >= balancerNames.size())
	// throw new IllegalArgumentException(
	// "Index out of bounds, no balancer for this index!!!");

	// if ((!isConnectedToBalancer(index))
	// || !connections.containsKey(balancerNames.get(index)))
	// throw new IllegalStateException("Already disconnected from "
	// + balancerNames.get(index) + "!!!");

	// try {
	// connections.get(balancerNames.get(index)).close();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// connections.remove((balancerNames.get(index)));
	// }
	// }

	// public boolean isConnectedToBalancer(int index)
	// throws IllegalArgumentException {

	// String balancerName = null;
	// logger.debug("[isConnectedToBalancer] Locking _BALANCER_NAMES_OP_LOCK");
	// synchronized (_BALANCER_NAMES_OP_LOCK) {
	// if (index < 0 || index >= balancerNames.size())
	// throw new IllegalArgumentException(
	// "Index out of bounds, no balancer for this index!!!");
	// balancerName = balancerNames.get(index);
	//
	// logger
	// .debug("[isConnectedToBalancer] UNLocking _BALANCER_NAMES_OP_LOCK");
	// if (balancerName == null)
	// return false;

	// if (connections.containsKey(balancerName))
	// return connections.get(balancerName).isConnected();
	// else
	// return false;
	// }
	// }

	// public boolean removeBalancerAddress(byte[] addr, int port)
	// throws IllegalArgumentException, NullPointerException {

	// if (addr == null)
	// throw new NullPointerException("addr cant be null!!!");

	// if (addr.length != 4)
	// throw new IllegalArgumentException("addr.length!=4");
	// if (port < 0)
	// throw new IllegalArgumentException("port value is wrong");
	// InetAddress address = null;
	// try {
	// address = InetAddress.getByAddress(addr);
	// } catch (UnknownHostException e) {

	// e.printStackTrace();
	// throw new IllegalArgumentException(
	// "Something wrong with host creation.", e);
	// }

	// String balancerName = address.getCanonicalHostName() + ":" + port;
	//
	// if (!balancerNames.contains(balancerName))
	// return false;

	// this.removeBalancerAddress(this.balancerNames.indexOf(balancerName));

	// return true;
	// }

	// public boolean removeBalancerAddress(String hostName, int port, int
	// index)
	// throws IllegalArgumentException, NullPointerException {

	// InetAddress[] hostAddr = null;
	// try {
	// hostAddr = InetAddress.getAllByName(hostName);
	// } catch (UnknownHostException uhe) {
	// throw new IllegalArgumentException(
	// "HostName is not a valid host name or it doesnt exists in DNS",
	// uhe);
	// }

	// if (index < 0 || index >= hostAddr.length) {
	// throw new IllegalArgumentException(
	// "Index in host address array is wrong, it should be [0]<x<["
	// + hostAddr.length + "] and it is [" + index + "]");
	// }

	// InetAddress address = hostAddr[index];

	// return this.removeBalancerAddress(address.getAddress(), port);
	// }

}
