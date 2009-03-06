package org.mobicents.slee.container.rmi;

import org.mobicents.slee.resource.EventLookupFacilityImpl;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;

public interface RmiServerInterfaceMBean {

	public void startRMIServer(NullActivityFactoryImpl naf,
			EventLookupFacilityImpl eventLookupFacilityImpl,
			ActivityContextFactoryImpl activityContextFactory);

	public void stopRMIServer();

}
