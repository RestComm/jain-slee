package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMwatcherChangeType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMwatcherChangeType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PAM_WATCHERS_PERIODIC = 0;
	public static final TpPAMwatcherChangeType PAM_WATCHERS_PERIODIC = new TpPAMwatcherChangeType(_PAM_WATCHERS_PERIODIC);
	public static final int _PAM_WATCHERS_ADDED = 1;
	public static final TpPAMwatcherChangeType PAM_WATCHERS_ADDED = new TpPAMwatcherChangeType(_PAM_WATCHERS_ADDED);
	public static final int _PAM_WATCHERS_DELETED = 2;
	public static final TpPAMwatcherChangeType PAM_WATCHERS_DELETED = new TpPAMwatcherChangeType(_PAM_WATCHERS_DELETED);
	public int value()
	{
		return value;
	}
	public static TpPAMwatcherChangeType from_int(int value)
	{
		switch (value) {
			case _PAM_WATCHERS_PERIODIC: return PAM_WATCHERS_PERIODIC;
			case _PAM_WATCHERS_ADDED: return PAM_WATCHERS_ADDED;
			case _PAM_WATCHERS_DELETED: return PAM_WATCHERS_DELETED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMwatcherChangeType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
