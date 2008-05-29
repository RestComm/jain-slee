package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_PM_TIME_PERIOD_CONDITION = 0;
	public static final TpPolicyConditionType P_PM_TIME_PERIOD_CONDITION = new TpPolicyConditionType(_P_PM_TIME_PERIOD_CONDITION);
	public static final int _P_PM_EVENT_CONDITION = 1;
	public static final TpPolicyConditionType P_PM_EVENT_CONDITION = new TpPolicyConditionType(_P_PM_EVENT_CONDITION);
	public static final int _P_PM_EXPRESSION_CONDITION = 2;
	public static final TpPolicyConditionType P_PM_EXPRESSION_CONDITION = new TpPolicyConditionType(_P_PM_EXPRESSION_CONDITION);
	public int value()
	{
		return value;
	}
	public static TpPolicyConditionType from_int(int value)
	{
		switch (value) {
			case _P_PM_TIME_PERIOD_CONDITION: return P_PM_TIME_PERIOD_CONDITION;
			case _P_PM_EVENT_CONDITION: return P_PM_EVENT_CONDITION;
			case _P_PM_EXPRESSION_CONDITION: return P_PM_EXPRESSION_CONDITION;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPolicyConditionType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
