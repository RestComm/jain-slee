package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFWExceptionType"
 *	@author JacORB IDL compiler 
 */

public final class TpFWExceptionType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_FW_DUMMY = 0;
	public static final TpFWExceptionType P_FW_DUMMY = new TpFWExceptionType(_P_FW_DUMMY);
	public int value()
	{
		return value;
	}
	public static TpFWExceptionType from_int(int value)
	{
		switch (value) {
			case _P_FW_DUMMY: return P_FW_DUMMY;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpFWExceptionType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
