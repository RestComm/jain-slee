package org.mobicents.slee.resource.sip11.mbean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.slee.resource.ResourceException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;



public class SipRaConfiguration implements SipRaConfigurationMBean {

	
	public static final String MBEAN_NAME_PREFIX="slee:sippraconfiguration=";
	private boolean automaticDialogSupport=false;
	
	private String stackAddress=null;
	
	private String[] transports=null;
	
	private int port=-1;
	
	private static Logger logger=Logger.getLogger(SipRaConfiguration.class);

	
	
	public SipRaConfiguration(boolean automaticDialogSupport, String stackAddress, String[] transports, int port) {
		super();
		this.automaticDialogSupport = automaticDialogSupport;
		this.stackAddress = stackAddress;
		this.transports = transports;
		this.port = port;
	}

	public boolean getAutomaticDialogSupport() {
	
		return automaticDialogSupport;
	}

	public String getStackAddress() {
	
		return stackAddress;
	}

	public int getStackPort() {
		
		return port;
	}

	public String[] getTransport() {
	
		return transports;
	}

	
	public void startService(String name) 
	{
		
		logger.info("Exposing RA configuration as MBean");
		MBeanServer mbs=SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on=null;
		try {
			on=new ObjectName(MBEAN_NAME_PREFIX+name);
			
		
			
			mbs.registerMBean(this, on);
			
			logger.info("RA Configuration exposed");
		}  catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void stopService(String name)
	{
		logger.info("Removing RA configuration MBean");
		MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on;
		try {
			on = new ObjectName(MBEAN_NAME_PREFIX
					+ name);
			mbs.unregisterMBean(on);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
}
