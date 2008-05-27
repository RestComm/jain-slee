package org.mobicents.slee.services.sip.balancer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.mobicents.slee.services.sip.balancer.mbean.SIPNode;

public class NodeRegisterImpl  implements NodeRegister, NodeRegisterImplMBean {

	private static Logger logger = Logger.getLogger(NodeRegisterImpl.class
			.getCanonicalName());

	private long nodeInfoExpirationTaskInterval = 5000;
	private long infoFetchInterval = 800;
	private long nodeExpiration = 5100;

	// is one timer enough for both task types?
	private Timer taskTimer = new Timer();
	// Is one task enough? for those actions?
	// private TimerTask balancerMessagesFetchTask = null;

	private TimerTask nodeExpirationTask = null;

	private int pointer = 0;

	private List<SIPNode> nodes = new ArrayList<SIPNode>();
	private Map<String, SIPNode> gluedSessions = new HashMap<String, SIPNode>();
	//private Map<Socket, TimerTask> fetchTasks = new HashMap<Socket, TimerTask>();
	//private List<Socket> connections = new ArrayList<Socket>();

	// List<Socket> connections=Collections.synchronizedList(new
	// ArrayList<Socket>());
	private InetAddress serverAddress = null;
	//private int port = 0;
	//private ServerSocket ss = null;

	// private Thread serverThread = null;

	//public NodeRegisterImpl(InetAddress serverAddress, int port) {
	//	super();
	//	this.serverAddress = serverAddress;
	//	this.port = port;
	//}

	public NodeRegisterImpl(InetAddress serverAddress) throws RemoteException {
		super();
		this.serverAddress = serverAddress;
		
	}
	
	
	public List<SIPNode> getGatheredInfo() {

		return new ArrayList<SIPNode>(this.nodes);
	}

	public boolean startServer() {

		try {

			logger.finer("[NodeRegisterImpl] Starting socket");
			//this.ss = new ServerSocket(port, 0, serverAddress);
			// this.serverThread = new Thread(new ServerRunClass());
			// this.serverThread.start();
			register(serverAddress);
			System.out
					.println("[NodeRegisterImpl] Server thread is running, creating tasks");

			this.nodeExpirationTask = new NodeExpirationTimerTask();
			this.taskTimer.scheduleAtFixedRate(this.nodeExpirationTask,
					this.nodeInfoExpirationTaskInterval,
					this.nodeInfoExpirationTaskInterval);

			// this.balancerMessagesFetchTask = new BalancerMessagesFetchTask();
			// this.taskTimer.scheduleAtFixedRate(this.balancerMessagesFetchTask,
			// this.infoFetchInterval, this.infoFetchInterval);
			logger.finer("[NodeRegisterImpl] Done with starting");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	
	
	
	
	public boolean stopServer() {

		deregister(serverAddress);
		System.exit(0);
		return false;
	}

	
	// ********* CLASS TO BE EXPOSED VIA RMI
	private class RegisterRMIStub extends
	UnicastRemoteObject implements NodeRegisterRMIStub
	{

		protected RegisterRMIStub() throws RemoteException {
			super();
			
		}
		
		public void handlePing(ArrayList<SIPNode> ping) throws RemoteException
		{
			//CALL METHOD IN REGISTRY
			handlePingInRegister(ping);
		}
		
	}
	// ***** SOME PRIVATE HELPERS

	private void register(InetAddress serverAddress) {

		try {
			LocateRegistry.createRegistry(2000);
			Registry registry = LocateRegistry.getRegistry(serverAddress
					.getHostAddress(), 2000);
			registry.rebind("SIPBalancer", new RegisterRMIStub());
		} catch (RemoteException e) {
			throw new RuntimeException("Failed to bind due to:", e);
		}

	}

	private void deregister(InetAddress serverAddress) {
		try {
			LocateRegistry.createRegistry(2000);
			Registry registry = LocateRegistry.getRegistry(serverAddress
					.getHostAddress(), 2000);
			registry.unbind("SIPBalancer");
		} catch (RemoteException e) {

			throw new RuntimeException("Failed to unbind due to", e);
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ***** NODE MGMT METHODS

	public void unStickSessionFromNode(String callID) {
		this.gluedSessions.remove(callID);
	}

	public void reset() {

	}

	public SIPNode getNextNode() throws IndexOutOfBoundsException {

		int oldPtr = pointer++;
		if (pointer >= nodes.size())
			pointer = 0;
		SIPNode chosen = this.nodes.get(oldPtr);

		return chosen;
	}

	public SIPNode stickSessionToNode(String callID) {

		if (this.gluedSessions.containsKey(callID))
			return this.gluedSessions.get(callID);

		SIPNode node = null;
		for (int i = 0; i < 5 && node == null; i++)
			try {
				node = this.getNextNode();
			} catch (IndexOutOfBoundsException ioobe) {

			}
		if (node != null)
			this.gluedSessions.put(callID, node);

		return node;
	}

	public SIPNode getGluedNode(String callID) {

		return this.gluedSessions.get(callID);
	}

	class NodeExpirationTimerTask extends TimerTask {

		public void run() {

			logger.finer("[NodeRegisterImpl][NodeExpirationTimerTask] Run");
			// for (SIPNode node : nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				SIPNode node = nodes.get(i);
				logger
						.finer("[NodeRegisterImpl][NodeExpirationTimerTask] Run Sync["
								+ node + "]");
			
				if (node.getTimeStamp() + nodeExpiration < System
						.currentTimeMillis()) {
					nodes.remove(node);
				
				}
		
				logger
						.finer("[NodeRegisterImpl][NodeExpirationTimerTask] Run NSync["
								+ node + "]");
			}
			logger.finer("[NodeRegisterImpl][NodeExpirationTimerTask] Run End");
		}

	}

	public void handlePingInRegister(ArrayList<SIPNode> ping) {

		for (SIPNode node : ping) {

			if (nodes.contains(node)) {
				nodes.get(nodes.indexOf(node)).updateTimerStamp();
			} else {

				nodes.add(node);

			}
		}
	}

	public InetAddress getAddress() {

		return this.serverAddress;
	}

	public long getNodeExpiration() {

		return this.nodeExpiration;
	}

	public long getNodeExpirationTaskInterval() {

		return this.nodeInfoExpirationTaskInterval;
	}

	//public int getPort() {
//
	//	return this.port;
	//}

	public void setNodeExpiration(long value) throws IllegalArgumentException {
		if (value < 150)
			throw new IllegalArgumentException("Value cant be less than 150");
		this.nodeExpiration = value;

	}

	public void setNodeExpirationTaskInterval(long value) {
		if (value < 150)
			throw new IllegalArgumentException("Value cant be less than 150");
		this.nodeInfoExpirationTaskInterval = value;
	}

	// ******* CODE THAT USES SOCKETS INSTEAD OF RMI

	// public long getPingFetchInterval() {
	//
	// return this.infoFetchInterval;
	// }

	// public void setPingFetchInterval(long value) {
	// if (value < 5)
	// throw new IllegalArgumentException("Value cant be less than 5");
	// this.infoFetchInterval = value;
	// }

	// class ServerRunClass implements Runnable {

	// public void run() {
	// Socket s;
	// while (true) {
	// s = null;
	// try {
	// logger
	// .info("[NodeRegisterImpl][ServerRunClass] Accepting");
	// s = ss.accept();
	// connections.add(s);
	// scheduleFetchTask(s, 100);
	// // toWrite.put(s, new
	// // ObjectOutputStream(s.getOutputStream()));
	// // toRead.put(s, new ObjectInputStream(s.getInputStream()));
	// logger.info("[NodeRegisterImpl][ServerRunClass] Accepted["
	// + s + "]");
	// } catch (IOException e) {
	// logger
	// .warning("[NodeRegisterImpl][ServerThreadClass] Some failure occured
	// while estabilishing connection ["
	// + e.getMessage() + "]");
	// if (s != null) {
	// // toWrite.remove(s);
	// // toRead.remove(s);
	// connections.remove(s);
	// if (fetchTasks.containsKey(s))
	// try {
	// fetchTasks.remove(s).cancel();
	// } catch (Exception ee) {
	// }
	// }
	// e.printStackTrace();
	// }
	// }
	//
	// }
	//
	// }

	// --- Private message handlers

	// private void handleDisconnectReqeust(BalancerMessage msg, Socket s) {
	// Other end snends politely "I will terminate", so we dont get
	// exceptions and have to make distinction between error situations and
	// intent disconnect

	// connections.remove(s);
	// try {
	// fetchTasks.remove(s).cancel();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }

	// fetchThreads.remove(s).stop();
	// }
	// logger.finer("[NodeRegisterImpl] DR NSync[" + s + "]");

	// }

	// private void handlePing(BalancerMessage msg, Socket s) {
	//
	// try {
	// logger.finer("[NodeRegisterImpl] P");
	// ArrayList<SIPNode> info = (ArrayList<SIPNode>) msg.getContent();
	// for (SIPNode node : info) {
	// // logger.finer("[NodeRegisterImpl] P Sync[" + s + "]");
	// // synchronized (s) {
	// if (nodes.contains(node)) {
	// nodes.get(nodes.indexOf(node)).updateTimerStamp();
	// } else {
	// node.setSocket(s);
	// nodes.add(node);
	// // if (!toWrite.containsKey(s))
	// // toWrite.put(s, new ObjectOutputStream(s
	// // .getOutputStream()));
	// // if (!toRead.containsKey(s))
	// // toRead.put(s, new ObjectInputStream(s
	// // .getInputStream()));
	// // node.setSocket(s);
	// if (!connections.contains(s))
	// connections.add(s);
	//
	// }
	// }
	// // logger.finer("[NodeRegisterImpl] P NSync[" + s + "]");
	// }
	// } catch (Exception e) {
	// System.out
	// .println("[NodeRegisterImpl] Failed to handle ping due to["
	// + e.getMessage() + "]. Removing connections");
	// e.printStackTrace();

	// try {
	// this.fetchTasks.remove(s).cancel();
	// this.connections.remove(s);
	// s.close();
	// } catch (Exception ee) {
	// }
	// }
	//
	// }

	// private void scheduleFetchTask(Socket s, long firstRunIn) {
	// if (fetchTasks.containsKey(s))
	// try {
	// fetchTasks.remove(s).cancel();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// TimerTask tt = new MessageFetchTask(s);
	// fetchTasks.put(s, tt);
	// this.taskTimer.scheduleAtFixedRate(tt, 1000, this.infoFetchInterval);

	// }

	// --------- HELPER CLASSES

	// class MessageFetchTask extends TimerTask {

	// private Socket s = null;

	// @Override
	// public void run() {

	// logger.finer("[NodeRegisterImpl][MessageFetchTask] Run Sync[" + s
	// + "]");
	// synchronized (s) {

	// if (!connections.contains(s)) {
	// this.cancel();

	// return;
	// }
	// ObjectInputStream ois = toRead.get(s);
	// ObjectInputStream ois = null;
	// try {
	// logger.finer("[NodeRegisterImpl][MessageFetchTask] available:"
	// + s.getInputStream().available());
	// if (s.getInputStream().available() == 0) {
	// logger.finer("[NodeRegisterImpl][MessageFetchTask] [" + ois
	// + "]available2:" + s.getInputStream().available());
	// return;
	// }
	// ois = new ObjectInputStream(s.getInputStream());
	// if (ois == null) {

	// return;
	// }
	// } catch (java.io.StreamCorruptedException sce) {
	// This can happen if we dont read fast enough??
	// try {
	// / s.getInputStream().skip(s.getInputStream().available());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// return;
	// }
	//
	// try {
	// logger.finer("[NodeRegisterImpl][MessageFetchTask] some null:"
	// + ois);
	//
	// BalancerMessageImpl msg = (BalancerMessageImpl) ois
	// .readObject();
	//
	// For some twisted reason I cant use switch with enum
	// here.... ;/
	// logger
	// .finer("[NodeRegisterImpl][MessageFetchTask] Run - Got message:\n"
	// + msg);
	// if (msg.getType() == BalancerMessageType._Ping) {
	// handlePing(msg, s);
	// } else if (msg.getType() == BalancerMessageType._DisconnectReqeust) {
	// handleDisconnectReqeust(msg, s);
	// } else {
	//
	// logger
	// .severe("[BalancerMessagesFetchTask] got weird message type["
	// + msg.getType() + "], skipping message.");
	//
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// logger.finer("[NodeRegisterImpl][MessageFetchTask] Run End");
	// // }
	// logger.finer("[NodeRegisterImpl][MessageFetchTask] Run NSync[" + s
	// + "]");
	// }

	// public MessageFetchTask(Socket s) {
	// super();
	// this.s = s;
	// }

	// }

}
