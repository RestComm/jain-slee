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
		return (notificationSource == null) ? 0 : notificationSource.hashCode();		
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