package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressError"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ADDRESS_INVALID_UNDEFINED = 0;
	public static final TpAddressError P_ADDRESS_INVALID_UNDEFINED = new TpAddressError(_P_ADDRESS_INVALID_UNDEFINED);
	public static final int _P_ADDRESS_INVALID_MISSING = 1;
	public static final TpAddressError P_ADDRESS_INVALID_MISSING = new TpAddressError(_P_ADDRESS_INVALID_MISSING);
	public static final int _P_ADDRESS_INVALID_MISSING_ELEMENT = 2;
	public static final TpAddressError P_ADDRESS_INVALID_MISSING_ELEMENT = new TpAddressError(_P_ADDRESS_INVALID_MISSING_ELEMENT);
	public static final int _P_ADDRESS_INVALID_OUT_OF_RANGE = 3;
	public static final TpAddressError P_ADDRESS_INVALID_OUT_OF_RANGE = new TpAddressError(_P_ADDRESS_INVALID_OUT_OF_RANGE);
	public static final int _P_ADDRESS_INVALID_INCOMPLETE = 4;
	public static final TpAddressError P_ADDRESS_INVALID_INCOMPLETE = new TpAddressError(_P_ADDRESS_INVALID_INCOMPLETE);
	public static final int _P_ADDRESS_INVALID_CANNOT_DECODE = 5;
	public static final TpAddressError P_ADDRESS_INVALID_CANNOT_DECODE = new TpAddressError(_P_ADDRESS_INVALID_CANNOT_DECODE);
	public int value()
	{
		return value;
	}
	public static TpAddressError from_int(int value)
	{
		switch (value) {
			case _P_ADDRESS_INVALID_UNDEFINED: return P_ADDRESS_INVALID_UNDEFINED;
			case _P_ADDRESS_INVALID_MISSING: return P_ADDRESS_INVALID_MISSING;
			case _P_ADDRESS_INVALID_MISSING_ELEMENT: return P_ADDRESS_INVALID_MISSING_ELEMENT;
			case _P_ADDRESS_INVALID_OUT_OF_RANGE: return P_ADDRESS_INVALID_OUT_OF_RANGE;
			case _P_ADDRESS_INVALID_INCOMPLETE: return P_ADDRESS_INVALID_INCOMPLETE;
			case _P_ADDRESS_INVALID_CANNOT_DECODE: return P_ADDRESS_INVALID_CANNOT_DECODE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAddressError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
