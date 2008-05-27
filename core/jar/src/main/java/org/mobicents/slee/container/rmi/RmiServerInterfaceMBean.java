package org.mobicents.slee.container.rmi;

import javax.management.ObjectName;

import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.SleeInternalEndpoint;
import org.mobicents.slee.runtime.facilities.NullActivityFactoryImpl;

public interface RmiServerInterfaceMBean {

	public void startRMIServer(NullActivityFactoryImpl naf,
			SleeInternalEndpoint endpoint, EventLookup eventLookup,
			ActivityContextFactoryImpl activityContextFactory);

	public void stopRMIServer();
	
	//public ObjectName getRmiServerInterfaceImplMBean();

}
