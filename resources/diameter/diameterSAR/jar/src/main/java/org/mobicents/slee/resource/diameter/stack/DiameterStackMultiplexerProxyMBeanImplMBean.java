package org.mobicents.slee.resource.diameter.stack;

import org.jboss.system.ServiceMBean;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Stack;

public interface DiameterStackMultiplexerProxyMBeanImplMBean extends ServiceMBean {
	//public static final String MBEAN_NAME_PREFIX="slee:Service=DiameterProxy,Name=";
	public void registerRa(RADiameterListener raListener, Object appIds,Object commandCodes) throws IllegalStateException;
	public void deregisterRa(RADiameterListener raListener);
	//For sake of simplicity in the pre Gamma :)
	public Stack getStack();
	public Object getMultiplexerMBean();
	
}
