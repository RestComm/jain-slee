package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpVprpStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpVprpStatus
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _ACTIVE = 0;
	public static final TpVprpStatus ACTIVE = new TpVprpStatus(_ACTIVE);
	public static final int _PENDING = 1;
	public static final TpVprpStatus PENDING = new TpVprpStatus(_PENDING);
	public static final int _DISALLOWED = 2;
	public static final TpVprpStatus DISALLOWED = new TpVprpStatus(_DISALLOWED);
	public int value()
	{
		return value;
	}
	public static TpVprpStatus from_int(int value)
	{
		switch (value) {
			case _ACTIVE: return ACTIVE;
			case _PENDING: return PENDING;
			case _DISALLOWED: return DISALLOWED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpVprpStatus(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
