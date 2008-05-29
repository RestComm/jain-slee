package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionFault"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionFault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_DATA_SESSION_FAULT_UNDEFINED = 0;
	public static final TpDataSessionFault P_DATA_SESSION_FAULT_UNDEFINED = new TpDataSessionFault(_P_DATA_SESSION_FAULT_UNDEFINED);
	public static final int _P_DATA_SESSION_FAULT_USER_ABORTED = 1;
	public static final TpDataSessionFault P_DATA_SESSION_FAULT_USER_ABORTED = new TpDataSessionFault(_P_DATA_SESSION_FAULT_USER_ABORTED);
	public static final int _P_DATA_SESSION_TIMEOUT_ON_RELEASE = 2;
	public static final TpDataSessionFault P_DATA_SESSION_TIMEOUT_ON_RELEASE = new TpDataSessionFault(_P_DATA_SESSION_TIMEOUT_ON_RELEASE);
	public static final int _P_DATA_SESSION_TIMEOUT_ON_INTERRUPT = 3;
	public static final TpDataSessionFault P_DATA_SESSION_TIMEOUT_ON_INTERRUPT = new TpDataSessionFault(_P_DATA_SESSION_TIMEOUT_ON_INTERRUPT);
	public int value()
	{
		return value;
	}
	public static TpDataSessionFault from_int(int value)
	{
		switch (value) {
			case _P_DATA_SESSION_FAULT_UNDEFINED: return P_DATA_SESSION_FAULT_UNDEFINED;
			case _P_DATA_SESSION_FAULT_USER_ABORTED: return P_DATA_SESSION_FAULT_USER_ABORTED;
			case _P_DATA_SESSION_TIMEOUT_ON_RELEASE: return P_DATA_SESSION_TIMEOUT_ON_RELEASE;
			case _P_DATA_SESSION_TIMEOUT_ON_INTERRUPT: return P_DATA_SESSION_TIMEOUT_ON_INTERRUPT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDataSessionFault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
