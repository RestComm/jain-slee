package org.mobicents.slee.services.sip.balancer;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class BalancerRunner {

	private static Logger logger = Logger.getLogger(BalancerRunner.class
			.getCanonicalName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args=new String[]{"127.0.0.1","5070","5080"};
		if (args.length < 3) {
			logger.fine("Insufficient args");
			throw new IllegalArgumentException(
					"Bad args: supply ip address and internal port and external port ");
		}
		
		// This is my IP Address
		String ipAddress = args[0];
		int port;
		int externalPort;
		//int serverPort;
		try {
			// This is the proxy port at which I am listening.
			port = Integer.parseInt(args[1]);
			externalPort = Integer.parseInt(args[2]);
			//serverPort = Integer.parseInt(args[3]);

		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			logger.fine("arguments are IPAddress port");
			throw nfe;
		}

		NodeRegisterImpl reg = null;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e1) {

			e1.printStackTrace();
			return;
		}
		try {
			//reg = new NodeRegisterImpl(addr, serverPort);

			reg = new NodeRegisterImpl(addr);
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			ObjectName on = new ObjectName("slee:name=Balancer,type=sip");

			if (server.isRegistered(on)) {
				server.unregisterMBean(on);
			}

			server.registerMBean(reg, on);

		} catch (MalformedObjectNameException e) {

			e.printStackTrace();
			return;
		} catch (NullPointerException e) {

			e.printStackTrace();
			return;
		} catch (InstanceNotFoundException e) {
			// Shouldnt happen

			e.printStackTrace();
			return;
		} catch (MBeanRegistrationException e) {
			// Shouldnt happen
			e.printStackTrace();
			return;
		} catch (InstanceAlreadyExistsException e) {
			// Shouldnt happen
			e.printStackTrace();
			return;
		} catch (NotCompliantMBeanException e) {
			// Shouldnt happen
			e.printStackTrace();
			return;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			RouterImpl.setRegister(reg);
			SIPBalancerForwarder fwd = new SIPBalancerForwarder(addr
					.getHostAddress(), port, externalPort, reg);
			reg.startServer();
		} catch (Exception e) {
			reg.stopServer();
			e.printStackTrace();
		} finally
		{
			try {

				System.in.read();
				reg.stopServer();
			} catch (Exception e) {
			}
		}

	}

}
