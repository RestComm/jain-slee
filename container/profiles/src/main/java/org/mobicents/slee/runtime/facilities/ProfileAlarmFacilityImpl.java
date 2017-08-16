/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:10:51:03 2009-03-26<br>
 * Project: restcomm-jainslee-server-core<br>
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
 * Project: restcomm-jainslee-server-core<br>
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
