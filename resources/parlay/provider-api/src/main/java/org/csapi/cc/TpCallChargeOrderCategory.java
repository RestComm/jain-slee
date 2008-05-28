package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargeOrderCategory
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_CHARGE_TRANSPARENT = 0;
	public static final TpCallChargeOrderCategory P_CALL_CHARGE_TRANSPARENT = new TpCallChargeOrderCategory(_P_CALL_CHARGE_TRANSPARENT);
	public static final int _P_CALL_CHARGE_PREDEFINED_SET = 1;
	public static final TpCallChargeOrderCategory P_CALL_CHARGE_PREDEFINED_SET = new TpCallChargeOrderCategory(_P_CALL_CHARGE_PREDEFINED_SET);
	public int value()
	{
		return value;
	}
	public static TpCallChargeOrderCategory from_int(int value)
	{
		switch (value) {
			case _P_CALL_CHARGE_TRANSPARENT: return P_CALL_CHARGE_TRANSPARENT;
			case _P_CALL_CHARGE_PREDEFINED_SET: return P_CALL_CHARGE_PREDEFINED_SET;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallChargeOrderCategory(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
