package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpAction"
 *	@author JacORB IDL compiler 
 */

public final class TpAction
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _DROP = 0;
	public static final TpAction DROP = new TpAction(_DROP);
	public static final int _TRANSMIT = 1;
	public static final TpAction TRANSMIT = new TpAction(_TRANSMIT);
	public static final int _RESHAPE = 2;
	public static final TpAction RESHAPE = new TpAction(_RESHAPE);
	public static final int _REMARK = 3;
	public static final TpAction REMARK = new TpAction(_REMARK);
	public int value()
	{
		return value;
	}
	public static TpAction from_int(int value)
	{
		switch (value) {
			case _DROP: return DROP;
			case _TRANSMIT: return TRANSMIT;
			case _RESHAPE: return RESHAPE;
			case _REMARK: return REMARK;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAction(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
