package org.mobicents.slee.runtime.facilities;

import javax.slee.management.NotificationSource;

import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;

/**
 * Default impl of the alarm facility
 * @author martins
 *
 */
public class DefaultAlarmFacilityImpl extends AbstractAlarmFacilityImpl {

	private final MNotificationSource mNotificationSource;
	
	public DefaultAlarmFacilityImpl(NotificationSource notificationSource, AlarmMBeanImpl alarmMBeanImpl) {
		super(alarmMBeanImpl);
		this.mNotificationSource = new MNotificationSource(notificationSource);
	}

	@Override
	public MNotificationSource getNotificationSource() {
		return mNotificationSource;
	}

	
}
