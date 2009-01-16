package org.mobicents.slee.container.rmi;

import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;

public interface RmiServerInterfaceMBean {

	public void startRMIServer(NullActivityFactoryImpl naf,
			EventLookup eventLookup,
			ActivityContextFactoryImpl activityContextFactory);

	public void stopRMIServer();

}
