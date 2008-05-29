package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionListType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_PM_DNF = 0;
	public static final TpPolicyConditionListType P_PM_DNF = new TpPolicyConditionListType(_P_PM_DNF);
	public static final int _P_PM_CNF = 1;
	public static final TpPolicyConditionListType P_PM_CNF = new TpPolicyConditionListType(_P_PM_CNF);
	public int value()
	{
		return value;
	}
	public static TpPolicyConditionListType from_int(int value)
	{
		switch (value) {
			case _P_PM_DNF: return P_PM_DNF;
			case _P_PM_CNF: return P_PM_CNF;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPolicyConditionListType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
