package org.mobicents.slee.runtime.facilities;

import javax.slee.SbbID;
import javax.slee.management.SbbNotification;

import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;

/**
 * Tricky extension of the alarm facility, needs to get the service id from the
 * thread
 * 
 * @author martins
 * 
 */
public class SbbAlarmFacilityImpl extends AbstractAlarmFacilityImpl {

	private final SbbID sbbID;

	public SbbAlarmFacilityImpl(SbbID sbbID, AlarmMBeanImpl alarmMBeanImpl) {
		super(alarmMBeanImpl);
		this.sbbID = sbbID;
	}

	@Override
	public MNotificationSource getNotificationSource() {
		return new MNotificationSource(new SbbNotification(
				EventRouterThreadLocals.getInvokingService(), sbbID));
	}

}
