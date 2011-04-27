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

package net.java.slee.resources.smpp.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 
 * @author amit bhayani
 *
 */
public interface AbsoluteSMPPDate extends SMPPDate {

	/**
	 * Get the timezone of this SMPPDate.
	 * 
	 * @return The timezone of this SMPPDate object, or <code>null</code> if there is no timezone.
	 */
	public TimeZone getTimeZone();

	/**
	 * Get a calendar object that represents the time specified by this SMPPDate. The returned value will be
	 * <code>null</code> for relative SMPP times. Also, for absolute SMPP times that do not contain timezone
	 * information, the returned calendar&apos;s timezone cannot be trusted - it will simply be initialised to whatever
	 * <code>java.util.Calendar</code> considers its default (usually the timezone of the JVM).
	 * 
	 * @return A calendar object, or <code>null</code> if this is a relative time specification.
	 */
	public Calendar getCalendar();
}
