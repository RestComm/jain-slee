package org.mobicents.slee.services.sip.balancer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class RegisterTest {

	
	static InetAddress addr=null;
	static
	{
		try {
			addr=InetAddress.getByAddress(new byte[]{10,1,1,1});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	static int registerPort=5100;
	static int externalPort=5010;
	static int internalPort=5020;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		testBalancer();
		
	}
	
	private static void testBalancer()
	{
		try{
		NodeRegisterImpl reg=prepareRegister();
		
		reg.startServer();
		RouterImpl.setRegister(reg);
		SIPBalancerForwarder fwd=new SIPBalancerForwarder(addr.getHostAddress(),internalPort,externalPort,reg);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	private static NodeRegisterImpl prepareRegister()
	{
		
		try {
			
			
			//NodeRegisterImpl reg=new NodeRegisterImpl(addr,registerPort);
			NodeRegisterImpl reg=new NodeRegisterImpl(addr);
			
			MBeanServer server=ManagementFactory.getPlatformMBeanServer();
			ObjectName on=new ObjectName("slee:name=Balancer,type=sip");
			
			if(server.isRegistered(on))
			{
				server.unregisterMBean(on);
			}
			
			server.registerMBean(reg, on);
			
			return reg;
				
		} catch (MalformedObjectNameException e) {
			
			e.printStackTrace();
		} catch (NullPointerException e) {
			
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static void undoRegister(NodeRegisterImpl reg)
	{
		
		
	
			
			
			try {
				MBeanServer server=ManagementFactory.getPlatformMBeanServer();
				ObjectName on = new ObjectName("slee:name=Balancer,type=sip");
				server.unregisterMBean(on);
				reg.stopServer();
			} catch (MalformedObjectNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MBeanRegistrationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
		
		
		
	}

}
