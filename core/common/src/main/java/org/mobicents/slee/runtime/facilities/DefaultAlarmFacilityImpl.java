package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.management.NotificationSource;

import org.mobicents.slee.container.management.AlarmManagement;

/**
 * Default impl of the alarm facility
 * @author martins
 *
 */
public class DefaultAlarmFacilityImpl extends AbstractAlarmFacilityImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final NotificationSourceWrapperImpl mNotificationSource;
	
	public DefaultAlarmFacilityImpl(NotificationSource notificationSource, AlarmManagement alarmMBeanImpl) {
		super(alarmMBeanImpl);
		this.mNotificationSource = new NotificationSourceWrapperImpl(notificationSource);
	}

	@Override
	public NotificationSourceWrapperImpl getNotificationSource() {
		return mNotificationSource;
	}

	
}
