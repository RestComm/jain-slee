package org.csapi;
/**
 *	Generated from IDL definition of enum "TpCallAoCOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAoCOrderCategory
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CHARGE_ADVICE_INFO = 0;
	public static final TpCallAoCOrderCategory P_CHARGE_ADVICE_INFO = new TpCallAoCOrderCategory(_P_CHARGE_ADVICE_INFO);
	public static final int _P_CHARGE_PER_TIME = 1;
	public static final TpCallAoCOrderCategory P_CHARGE_PER_TIME = new TpCallAoCOrderCategory(_P_CHARGE_PER_TIME);
	public static final int _P_CHARGE_NETWORK = 2;
	public static final TpCallAoCOrderCategory P_CHARGE_NETWORK = new TpCallAoCOrderCategory(_P_CHARGE_NETWORK);
	public int value()
	{
		return value;
	}
	public static TpCallAoCOrderCategory from_int(int value)
	{
		switch (value) {
			case _P_CHARGE_ADVICE_INFO: return P_CHARGE_ADVICE_INFO;
			case _P_CHARGE_PER_TIME: return P_CHARGE_PER_TIME;
			case _P_CHARGE_NETWORK: return P_CHARGE_NETWORK;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallAoCOrderCategory(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
