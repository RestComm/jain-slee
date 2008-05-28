package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallMonitorMode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallMonitorMode
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_MONITOR_MODE_INTERRUPT = 0;
	public static final TpCallMonitorMode P_CALL_MONITOR_MODE_INTERRUPT = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_INTERRUPT);
	public static final int _P_CALL_MONITOR_MODE_NOTIFY = 1;
	public static final TpCallMonitorMode P_CALL_MONITOR_MODE_NOTIFY = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_NOTIFY);
	public static final int _P_CALL_MONITOR_MODE_DO_NOT_MONITOR = 2;
	public static final TpCallMonitorMode P_CALL_MONITOR_MODE_DO_NOT_MONITOR = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_DO_NOT_MONITOR);
	public int value()
	{
		return value;
	}
	public static TpCallMonitorMode from_int(int value)
	{
		switch (value) {
			case _P_CALL_MONITOR_MODE_INTERRUPT: return P_CALL_MONITOR_MODE_INTERRUPT;
			case _P_CALL_MONITOR_MODE_NOTIFY: return P_CALL_MONITOR_MODE_NOTIFY;
			case _P_CALL_MONITOR_MODE_DO_NOT_MONITOR: return P_CALL_MONITOR_MODE_DO_NOT_MONITOR;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallMonitorMode(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
