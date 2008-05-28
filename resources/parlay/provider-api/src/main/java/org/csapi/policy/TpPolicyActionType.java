package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyActionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_PM_EVENT_ACTION = 0;
	public static final TpPolicyActionType P_PM_EVENT_ACTION = new TpPolicyActionType(_P_PM_EVENT_ACTION);
	public static final int _P_PM_EXPRESSION_ACTION = 1;
	public static final TpPolicyActionType P_PM_EXPRESSION_ACTION = new TpPolicyActionType(_P_PM_EXPRESSION_ACTION);
	public int value()
	{
		return value;
	}
	public static TpPolicyActionType from_int(int value)
	{
		switch (value) {
			case _P_PM_EVENT_ACTION: return P_PM_EVENT_ACTION;
			case _P_PM_EXPRESSION_ACTION: return P_PM_EXPRESSION_ACTION;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPolicyActionType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
