package org.mobicents.slee.resource.diameter.base.stack;

import org.jboss.system.ServiceMBean;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.RADiameterListener;

public interface DiameterStackMultiplexerProxyMBeanImplMBean extends ServiceMBean {
	public static final String MBEAN_NAME_PREFIX="slee:Service=DiameterProxy,Name=";
	public void registerRa(RADiameterListener raListener, ApplicationId[] appIds,long[] commandCodes) throws IllegalStateException;
	public void deregisterRa(RADiameterListener raListener);
	//For sake of simplicity in the pre Gamma :)
	public Stack getStack();
	
	
}
