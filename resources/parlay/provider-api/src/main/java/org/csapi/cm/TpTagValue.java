package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpTagValue"
 *	@author JacORB IDL compiler 
 */

public final class TpTagValue
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PROVIDER_SPECIFIED = 0;
	public static final TpTagValue PROVIDER_SPECIFIED = new TpTagValue(_PROVIDER_SPECIFIED);
	public static final int _OPERATOR_SPECIFIED = 1;
	public static final TpTagValue OPERATOR_SPECIFIED = new TpTagValue(_OPERATOR_SPECIFIED);
	public static final int _UNSPECIFIED = 2;
	public static final TpTagValue UNSPECIFIED = new TpTagValue(_UNSPECIFIED);
	public int value()
	{
		return value;
	}
	public static TpTagValue from_int(int value)
	{
		switch (value) {
			case _PROVIDER_SPECIFIED: return PROVIDER_SPECIFIED;
			case _OPERATOR_SPECIFIED: return OPERATOR_SPECIFIED;
			case _UNSPECIFIED: return UNSPECIFIED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTagValue(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
