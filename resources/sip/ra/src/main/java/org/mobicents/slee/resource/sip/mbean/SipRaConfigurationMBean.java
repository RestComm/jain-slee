package org.mobicents.slee.resource.sip.mbean;

public interface SipRaConfigurationMBean {

	public boolean getAutomaticDialogSupport();
	
	public String getStackAddress();
	
	public int  getStackPort();
	
	public String[] getTransport();
	
	
}
