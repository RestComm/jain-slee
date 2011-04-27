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

import java.io.Serializable;

/**
 * 
 * @author amit bhayani
 *
 */
public interface SMPPDate extends Serializable {

	/**
	 * Get the year part of this time format. The return value from this will be in the range 0 - 99 for relative times,
	 * or will be the full year (such as <code>2007</code>) for absolute times.
	 * 
	 * @return The year part of this time format.
	 */
	public int getYear();

	/**
	 * Get the month part of this time format. This will always return a value in the range 1 - 12.
	 * 
	 * @return The month part of this time format.
	 */
	public int getMonth();

	/**
	 * Get the day part of this time format. This will always return a value in the range 1 - 31.
	 * 
	 * @return The day part of this time format.
	 */
	public int getDay();

	/**
	 * Get the hour part of this time format. This will always return a value in the range 0 - 23.
	 * 
	 * @return The hour part of this time format.
	 */
	public int getHour();

	/**
	 * Get the minute part of this time format. This will always return a value in the range 0 - 59.
	 * 
	 * @return The minute part of this time format.
	 */
	public int getMinute();

	/**
	 * Get the second part of this time format. This will always return a value in the range 0 - 59.
	 * 
	 * @return The second part of this time format.
	 */
	public int getSecond();

	/**
	 * Get the tenths of a second part of this time format. This will always return a value in the range 0 - 9.
	 * 
	 * @return The tenths of a second part of this time format.
	 */
	public int getTenth();

	/**
	 * Get the UTC offset part of this time format. This will always return a value in the range 0 - 48. The "direction"
	 * of the offset should be determined using {@link #getSign()}.
	 * 
	 * @return The UTC offset part of this time format.
	 * @see #getTimeZone()
	 */
	public int getUtcOffset();

	/**
	 * Get the timezone offset modifier character. For absolute time formats, this will return one of '+' if the
	 * timezone offset is ahead of UTC, '-' if the timezone offset is behind UTC, or <code>(char) 0</code> if there is
	 * no timezone information.
	 * 
	 * @return One of '+', '-' or <code>(char) 0</code>.
	 */
	public char getSign();

	/**
	 * Determine if this date object represents an absolute time.
	 * 
	 * @return <code>true</code> if this object is an absolute time, <code>false</code> otherwise.
	 */
	public boolean isAbsolute();

	/**
	 * Determine if this date object represents a relative time.
	 * 
	 * @return <code>true</code> if this object is a relative time, <code>false</code> otherwise.
	 */
	public boolean isRelative();

}
