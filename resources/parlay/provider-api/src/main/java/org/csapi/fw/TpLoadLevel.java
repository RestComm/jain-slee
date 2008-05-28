package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadLevel"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadLevel
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _LOAD_LEVEL_NORMAL = 0;
	public static final TpLoadLevel LOAD_LEVEL_NORMAL = new TpLoadLevel(_LOAD_LEVEL_NORMAL);
	public static final int _LOAD_LEVEL_OVERLOAD = 1;
	public static final TpLoadLevel LOAD_LEVEL_OVERLOAD = new TpLoadLevel(_LOAD_LEVEL_OVERLOAD);
	public static final int _LOAD_LEVEL_SEVERE_OVERLOAD = 2;
	public static final TpLoadLevel LOAD_LEVEL_SEVERE_OVERLOAD = new TpLoadLevel(_LOAD_LEVEL_SEVERE_OVERLOAD);
	public int value()
	{
		return value;
	}
	public static TpLoadLevel from_int(int value)
	{
		switch (value) {
			case _LOAD_LEVEL_NORMAL: return LOAD_LEVEL_NORMAL;
			case _LOAD_LEVEL_OVERLOAD: return LOAD_LEVEL_OVERLOAD;
			case _LOAD_LEVEL_SEVERE_OVERLOAD: return LOAD_LEVEL_SEVERE_OVERLOAD;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLoadLevel(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
