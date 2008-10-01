package org.mobicents.slee.services.sip.registrar.mbean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;

public class RegistrarConfigurator implements RegistrarConfiguratorMBean {

	private String name="v1RegistrarConf";
	
	private long minExpires=120;
	
	private long maxExpires=3600;
	
	public long getSipRegistrationMaxExpires() {
		
		return maxExpires;
	}

	public long getSipRegistrationMinExpires() {
		
		return minExpires;
	}

	public void setSipRegistrationMaxExpires(long maxExpires) {
		if(maxExpires<=0)
		{
			this.maxExpires=3600;

			//throw new IllegalArgumentException("maxExpires time cant be equal or less than zero!!!");
		}
		else
		{
			this.maxExpires=maxExpires;
		}
		
		if(this.maxExpires<=minExpires)
			this.minExpires=this.maxExpires-10;
	}

	public void setSipRegistrationMinExpires(long minExpires) {
		if(minExpires<=0)
		{
			this.minExpires=60;
			
			//throw new IllegalArgumentException("minExpires time cant be equal or less than zero!!!");
		}else
		{
			this.minExpires=minExpires;
		}
		if(this.maxExpires<=this.minExpires)
			this.maxExpires=this.minExpires+10;
		
		return;
		
	}

	
	public Object clone()
	{
		
		
		return new RegistrarConfigurator(this.minExpires,this.maxExpires);
	}

	public RegistrarConfigurator(long minExpires, long maxExpires) {
		super();
		this.minExpires = minExpires;
		this.maxExpires = maxExpires;
	}

	public RegistrarConfigurator() {
		super();
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	
	public  boolean startService()
	{
		
		
		MBeanServer mbs=SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on=null;
		try {
			on=new ObjectName(MBEAN_NAME_PREFIX+name);
			
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (mbs.getObjectInstance(on) != null) {
				mbs.unregisterMBean(on);
			}
		} catch (InstanceNotFoundException e) {
			// ignore
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mbs.registerMBean(this, on);
		} catch (InstanceAlreadyExistsException e) {
			
			e.printStackTrace();
			return false;
		} catch (MBeanRegistrationException e) {
			
			e.printStackTrace();
			return false;
		} catch (NotCompliantMBeanException e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public void stopService() {
		try {
			SleeContainer.lookupFromJndi().getMBeanServer().unregisterMBean(new ObjectName(MBEAN_NAME_PREFIX+name));
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
