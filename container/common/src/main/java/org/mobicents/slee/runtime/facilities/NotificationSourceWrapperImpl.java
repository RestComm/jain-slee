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

package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import javax.slee.management.NotificationSource;

import org.mobicents.slee.container.facilities.NotificationSourceWrapper;

/**
 * TODO javadocs
 * @author baranowb
 *
 */
public class NotificationSourceWrapperImpl implements NotificationSourceWrapper, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private NotificationSource notificationSource = null;
	
	/**
	 * 
	 */
	private final AtomicLong sequence = new AtomicLong(0);
	
	/**
	 * 
	 * @param notificationSource
	 */
	public NotificationSourceWrapperImpl(NotificationSource notificationSource) {
		this.notificationSource = notificationSource;
	}
	
	@Override
	public int hashCode() {
		return ((notificationSource == null) ? 0 : notificationSource.hashCode());		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationSourceWrapperImpl other = (NotificationSourceWrapperImpl) obj;
		if (notificationSource == null) {
			if (other.notificationSource != null)
				return false;
		} else if (!notificationSource.equals(other.notificationSource))
			return false;
		return true;
	}
	
	/**
	 * @return
	 */
	public NotificationSource getNotificationSource() {
		return notificationSource;
	}
	
	/**
	 * @return
	 */
	public long getNextSequence() {
		return sequence.getAndIncrement();
	}
	
	@Override
	public String toString() {
		return notificationSource+"@"+super.toString();
	}
	
}