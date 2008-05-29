package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpServiceTypePropertyMode"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyMode
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _NORMAL = 0;
	public static final TpServiceTypePropertyMode NORMAL = new TpServiceTypePropertyMode(_NORMAL);
	public static final int _MANDATORY = 1;
	public static final TpServiceTypePropertyMode MANDATORY = new TpServiceTypePropertyMode(_MANDATORY);
	public static final int _READONLY = 2;
	public static final TpServiceTypePropertyMode READONLY = new TpServiceTypePropertyMode(_READONLY);
	public static final int _MANDATORY_READONLY = 3;
	public static final TpServiceTypePropertyMode MANDATORY_READONLY = new TpServiceTypePropertyMode(_MANDATORY_READONLY);
	public int value()
	{
		return value;
	}
	public static TpServiceTypePropertyMode from_int(int value)
	{
		switch (value) {
			case _NORMAL: return NORMAL;
			case _MANDATORY: return MANDATORY;
			case _READONLY: return READONLY;
			case _MANDATORY_READONLY: return MANDATORY_READONLY;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpServiceTypePropertyMode(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
