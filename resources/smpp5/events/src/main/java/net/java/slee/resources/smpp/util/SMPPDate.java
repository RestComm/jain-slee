package net.java.slee.resources.smpp.util;

import java.io.Serializable;

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
