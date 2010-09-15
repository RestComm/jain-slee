package net.java.slee.resources.smpp.util;

import java.util.Calendar;
import java.util.TimeZone;

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
