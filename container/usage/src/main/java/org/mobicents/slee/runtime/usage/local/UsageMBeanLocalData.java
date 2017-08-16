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
 * 
 */
package org.mobicents.slee.runtime.usage.local;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.management.NotificationSource;

import org.mobicents.slee.runtime.usage.UsageMBeanData;
import org.mobicents.slee.runtime.usage.UsageParameter;

/**
 * Class used to store usage params locally, thus this should be used once usage
 * param replication is not desirable.
 * 
 * @author martins
 * 
 */
public class UsageMBeanLocalData implements UsageMBeanData {

	/**
	 * 
	 */
	private static final ConcurrentHashMap<NotificationSource, ConcurrentHashMap<String,UsageParamSetLocalData>> notificationSourceUsageParamSetsMap
		= new ConcurrentHashMap<NotificationSource, ConcurrentHashMap<String,UsageParamSetLocalData>>();
	
	/**
	 * 
	 */
	private final NotificationSource notificationSource;
	
	/**
	 * 
	 */
	private final String usageParameterSetName;
	
	/**
	 * @param notificationSource
	 * @param usageParameterSetName
	 */
	public UsageMBeanLocalData(NotificationSource notificationSource,
			String usageParameterSetName) {
		this.notificationSource = notificationSource;
		this.usageParameterSetName = usageParameterSetName;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.usage.UsageMBeanData#create()
	 */
	public boolean create() {
		
		ConcurrentHashMap<String, UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets == null) {
			ConcurrentHashMap<String, UsageParamSetLocalData> newNotificationSourceUsageParamSets = new ConcurrentHashMap<String, UsageParamSetLocalData>();
			notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.putIfAbsent(notificationSource, newNotificationSourceUsageParamSets);
			if (notificationSourceUsageParamSets == null) {
				notificationSourceUsageParamSets = newNotificationSourceUsageParamSets;
			}
		}
		
		if (!notificationSourceUsageParamSets.containsKey(usageParameterSetName)) {		
			return notificationSourceUsageParamSets.putIfAbsent(usageParameterSetName,new UsageParamSetLocalData(usageParameterSetName)) == null;
		}
		else {
			return false;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.usage.UsageMBeanData#getParameter(java.lang.String)
	 */
	public UsageParameter getParameter(String parameterName) {
		Map<String,UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets != null) {
			UsageParamSetLocalData data = notificationSourceUsageParamSets.get(usageParameterSetName);
			if (data != null) {
				return data.getUsageParam(parameterName);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.usage.UsageMBeanData#getParameterNames()
	 */
	public Collection<String> getParameterNames() {
		Map<String,UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets != null) {
			UsageParamSetLocalData data = notificationSourceUsageParamSets.get(usageParameterSetName);
			if (data != null) {
				return data.getParameterNames();
			}
			else {
				throw new IllegalStateException("Usage param set not found ( notification source = "+notificationSource+" , usage param set name = "+usageParameterSetName+" ) "); 
			}
		}
		else {
			throw new IllegalStateException("Notification source usage param sets not found ( notification source = "+notificationSource+" ) "); 
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.usage.UsageMBeanData#remove()
	 */
	public boolean remove() {
		final Map<String,UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets != null) {
			return notificationSourceUsageParamSets.remove(usageParameterSetName) != null;
		}	
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.usage.UsageMBeanData#setParameter(java.lang.String, org.mobicents.slee.runtime.usage.UsageParameter)
	 */
	public void setParameter(String parameterName, UsageParameter usageParameter) {
		final Map<String,UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets != null) {
			UsageParamSetLocalData data = notificationSourceUsageParamSets.get(usageParameterSetName);
			if (data != null) {
				data.setUsageParam(parameterName, usageParameter);
			}
			else {
				throw new IllegalStateException("Usage param set not found ( notification source = "+notificationSource+" , usage param set name = "+usageParameterSetName+" ) "); 
			}
		}
		else {
			throw new IllegalStateException("Notification source usage param sets not found ( notification source = "+notificationSource+" ) "); 
		}		
	}

	/**
	 * @param notificationSource
	 * @return
	 */
	public static Collection<String> getExistingSets(
			NotificationSource notificationSource) {
		Map<String,UsageParamSetLocalData> notificationSourceUsageParamSets = notificationSourceUsageParamSetsMap.get(notificationSource);
		if (notificationSourceUsageParamSets != null) {
			return Collections.unmodifiableSet(notificationSourceUsageParamSets.keySet());
		}
		else {
			return Collections.emptySet();
		}
	}

	/**
	 * @param notificationSource
	 * @param parameterSetName
	 * @return
	 */
	public static boolean setExists(NotificationSource notificationSource,
			String parameterSetName) {
		return getExistingSets(notificationSource).contains(parameterSetName);
	}

	
}
