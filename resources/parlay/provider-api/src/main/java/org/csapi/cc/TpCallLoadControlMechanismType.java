package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallLoadControlMechanismType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLoadControlMechanismType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_LOAD_CONTROL_PER_INTERVAL = 0;
	public static final TpCallLoadControlMechanismType P_CALL_LOAD_CONTROL_PER_INTERVAL = new TpCallLoadControlMechanismType(_P_CALL_LOAD_CONTROL_PER_INTERVAL);
	public int value()
	{
		return value;
	}
	public static TpCallLoadControlMechanismType from_int(int value)
	{
		switch (value) {
			case _P_CALL_LOAD_CONTROL_PER_INTERVAL: return P_CALL_LOAD_CONTROL_PER_INTERVAL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallLoadControlMechanismType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
