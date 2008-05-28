package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIFault"
 *	@author JacORB IDL compiler 
 */

public final class TpUIFault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_FAULT_UNDEFINED = 0;
	public static final TpUIFault P_UI_FAULT_UNDEFINED = new TpUIFault(_P_UI_FAULT_UNDEFINED);
	public static final int _P_UI_CALL_ENDED = 1;
	public static final TpUIFault P_UI_CALL_ENDED = new TpUIFault(_P_UI_CALL_ENDED);
	public int value()
	{
		return value;
	}
	public static TpUIFault from_int(int value)
	{
		switch (value) {
			case _P_UI_FAULT_UNDEFINED: return P_UI_FAULT_UNDEFINED;
			case _P_UI_CALL_ENDED: return P_UI_CALL_ENDED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIFault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
