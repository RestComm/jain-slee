package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallFault"
 *	@author JacORB IDL compiler 
 */

public final class TpCallFault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_FAULT_UNDEFINED = 0;
	public static final TpCallFault P_CALL_FAULT_UNDEFINED = new TpCallFault(_P_CALL_FAULT_UNDEFINED);
	public static final int _P_CALL_TIMEOUT_ON_RELEASE = 1;
	public static final TpCallFault P_CALL_TIMEOUT_ON_RELEASE = new TpCallFault(_P_CALL_TIMEOUT_ON_RELEASE);
	public static final int _P_CALL_TIMEOUT_ON_INTERRUPT = 2;
	public static final TpCallFault P_CALL_TIMEOUT_ON_INTERRUPT = new TpCallFault(_P_CALL_TIMEOUT_ON_INTERRUPT);
	public int value()
	{
		return value;
	}
	public static TpCallFault from_int(int value)
	{
		switch (value) {
			case _P_CALL_FAULT_UNDEFINED: return P_CALL_FAULT_UNDEFINED;
			case _P_CALL_TIMEOUT_ON_RELEASE: return P_CALL_TIMEOUT_ON_RELEASE;
			case _P_CALL_TIMEOUT_ON_INTERRUPT: return P_CALL_TIMEOUT_ON_INTERRUPT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallFault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
