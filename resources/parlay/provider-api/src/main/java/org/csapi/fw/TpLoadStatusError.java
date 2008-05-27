package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatusError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatusError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _LOAD_STATUS_ERROR_UNDEFINED = 0;
	public static final TpLoadStatusError LOAD_STATUS_ERROR_UNDEFINED = new TpLoadStatusError(_LOAD_STATUS_ERROR_UNDEFINED);
	public static final int _LOAD_STATUS_ERROR_UNAVAILABLE = 1;
	public static final TpLoadStatusError LOAD_STATUS_ERROR_UNAVAILABLE = new TpLoadStatusError(_LOAD_STATUS_ERROR_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpLoadStatusError from_int(int value)
	{
		switch (value) {
			case _LOAD_STATUS_ERROR_UNDEFINED: return LOAD_STATUS_ERROR_UNDEFINED;
			case _LOAD_STATUS_ERROR_UNAVAILABLE: return LOAD_STATUS_ERROR_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLoadStatusError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
