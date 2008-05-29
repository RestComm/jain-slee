package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPVersion"
 *	@author JacORB IDL compiler 
 */

public final class TpIPVersion
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _VERSION_UNKNOWN = 0;
	public static final TpIPVersion VERSION_UNKNOWN = new TpIPVersion(_VERSION_UNKNOWN);
	public static final int _VERSION_IPV4 = 1;
	public static final TpIPVersion VERSION_IPV4 = new TpIPVersion(_VERSION_IPV4);
	public static final int _VERSION_IPV6 = 2;
	public static final TpIPVersion VERSION_IPV6 = new TpIPVersion(_VERSION_IPV6);
	public int value()
	{
		return value;
	}
	public static TpIPVersion from_int(int value)
	{
		switch (value) {
			case _VERSION_UNKNOWN: return VERSION_UNKNOWN;
			case _VERSION_IPV4: return VERSION_IPV4;
			case _VERSION_IPV6: return VERSION_IPV6;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpIPVersion(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
