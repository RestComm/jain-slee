/**
 * Start time:10:51:03 2009-03-26<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import org.mobicents.slee.container.facilities.NotificationSourceWrapper;
import org.mobicents.slee.container.management.AlarmManagement;
import org.mobicents.slee.container.profile.ProfileCallRecorderTransactionData;

/**
 * Start time:10:51:03 2009-03-26<br>
 * Project: mobicents-jainslee-server-core<br>
 * This is profile impl of alarm facility. it looks up transaction context for
 * profile call recorder. It rettrieves it to fetch notification source. NOTE:
 * this class wont work without valid transaction that has passed profile
 * concrete object method call.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class ProfileAlarmFacilityImpl extends AbstractAlarmFacilityImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProfileAlarmFacilityImpl(AlarmManagement bean) {
		super(bean);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.runtime.facilities.AbstractAlarmFacilityImpl#
	 * getNotificationSource()
	 */
	@Override
	public NotificationSourceWrapper getNotificationSource() {
		return ProfileCallRecorderTransactionData.getCurrentNotificationSource();
	}

}
