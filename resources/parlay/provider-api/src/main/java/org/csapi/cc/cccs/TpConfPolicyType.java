package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of enum "TpConfPolicyType"
 *	@author JacORB IDL compiler 
 */

public final class TpConfPolicyType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CONFERENCE_POLICY_UNDEFINED = 0;
	public static final TpConfPolicyType P_CONFERENCE_POLICY_UNDEFINED = new TpConfPolicyType(_P_CONFERENCE_POLICY_UNDEFINED);
	public static final int _P_CONFERENCE_POLICY_MONOMEDIA = 1;
	public static final TpConfPolicyType P_CONFERENCE_POLICY_MONOMEDIA = new TpConfPolicyType(_P_CONFERENCE_POLICY_MONOMEDIA);
	public static final int _P_CONFERENCE_POLICY_MULTIMEDIA = 2;
	public static final TpConfPolicyType P_CONFERENCE_POLICY_MULTIMEDIA = new TpConfPolicyType(_P_CONFERENCE_POLICY_MULTIMEDIA);
	public int value()
	{
		return value;
	}
	public static TpConfPolicyType from_int(int value)
	{
		switch (value) {
			case _P_CONFERENCE_POLICY_UNDEFINED: return P_CONFERENCE_POLICY_UNDEFINED;
			case _P_CONFERENCE_POLICY_MONOMEDIA: return P_CONFERENCE_POLICY_MONOMEDIA;
			case _P_CONFERENCE_POLICY_MULTIMEDIA: return P_CONFERENCE_POLICY_MULTIMEDIA;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpConfPolicyType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
