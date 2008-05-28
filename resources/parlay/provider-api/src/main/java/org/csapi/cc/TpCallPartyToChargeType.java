package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyToChargeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_PARTY_ORIGINATING = 0;
	public static final TpCallPartyToChargeType P_CALL_PARTY_ORIGINATING = new TpCallPartyToChargeType(_P_CALL_PARTY_ORIGINATING);
	public static final int _P_CALL_PARTY_DESTINATION = 1;
	public static final TpCallPartyToChargeType P_CALL_PARTY_DESTINATION = new TpCallPartyToChargeType(_P_CALL_PARTY_DESTINATION);
	public static final int _P_CALL_PARTY_SPECIAL = 2;
	public static final TpCallPartyToChargeType P_CALL_PARTY_SPECIAL = new TpCallPartyToChargeType(_P_CALL_PARTY_SPECIAL);
	public int value()
	{
		return value;
	}
	public static TpCallPartyToChargeType from_int(int value)
	{
		switch (value) {
			case _P_CALL_PARTY_ORIGINATING: return P_CALL_PARTY_ORIGINATING;
			case _P_CALL_PARTY_DESTINATION: return P_CALL_PARTY_DESTINATION;
			case _P_CALL_PARTY_SPECIAL: return P_CALL_PARTY_SPECIAL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallPartyToChargeType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
