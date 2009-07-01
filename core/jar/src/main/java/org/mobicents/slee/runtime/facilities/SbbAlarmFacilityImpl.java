package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.SLEEException;
import javax.slee.SbbID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;

/**
 * Tricky extension of the alarm facility, needs to get the service id from the
 * thread
 * 
 * @author martins
 * 
 */
public class SbbAlarmFacilityImpl extends AbstractAlarmFacilityImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SbbID sbbID;
	
	public SbbAlarmFacilityImpl(SbbID sbbID, AlarmMBeanImpl alarmMBeanImpl) {
		super(alarmMBeanImpl);
		this.sbbID = sbbID;
	}

	@Override
	public MNotificationSource getNotificationSource() {
		
		final ServiceComponent serviceComponent = SleeContainer.lookupFromJndi().getComponentRepositoryImpl().getComponentByID(EventRouterThreadLocals.getInvokingService());
		if (serviceComponent != null) {
			return (MNotificationSource) serviceComponent.getAlarmNotificationSources().get(sbbID);
		}
		else {
			throw new SLEEException("unable to retrieve notification source for sbb, the service was not found");
		}		
	}

}
