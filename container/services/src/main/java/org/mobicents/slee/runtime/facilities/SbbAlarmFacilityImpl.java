/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.SLEEException;
import javax.slee.SbbID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.AlarmManagement;

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
	
	public SbbAlarmFacilityImpl(SbbID sbbID, AlarmManagement alarmMBeanImpl) {
		super(alarmMBeanImpl);
		this.sbbID = sbbID;
	}

	@Override
	public NotificationSourceWrapperImpl getNotificationSource() {
		
		final ServiceComponent serviceComponent = SleeContainer.lookupFromJndi().getComponentRepository().getComponentByID(SleeThreadLocals.getInvokingService());
		if (serviceComponent != null) {
			return (NotificationSourceWrapperImpl) serviceComponent.getAlarmNotificationSources().get(sbbID);
		}
		else {
			throw new SLEEException("unable to retrieve notification source for sbb, the service was not found");
		}		
	}

}
