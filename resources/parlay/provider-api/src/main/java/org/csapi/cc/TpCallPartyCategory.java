package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyCategory
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_PARTY_CATEGORY_UNKNOWN = 0;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_UNKNOWN = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_UNKNOWN);
	public static final int _P_CALL_PARTY_CATEGORY_OPERATOR_F = 1;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_F = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_F);
	public static final int _P_CALL_PARTY_CATEGORY_OPERATOR_E = 2;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_E = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_E);
	public static final int _P_CALL_PARTY_CATEGORY_OPERATOR_G = 3;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_G = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_G);
	public static final int _P_CALL_PARTY_CATEGORY_OPERATOR_R = 4;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_R = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_R);
	public static final int _P_CALL_PARTY_CATEGORY_OPERATOR_S = 5;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_S = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_S);
	public static final int _P_CALL_PARTY_CATEGORY_ORDINARY_SUB = 6;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_ORDINARY_SUB = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_ORDINARY_SUB);
	public static final int _P_CALL_PARTY_CATEGORY_PRIORITY_SUB = 7;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_PRIORITY_SUB = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_PRIORITY_SUB);
	public static final int _P_CALL_PARTY_CATEGORY_DATA_CALL = 8;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_DATA_CALL = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_DATA_CALL);
	public static final int _P_CALL_PARTY_CATEGORY_TEST_CALL = 9;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_TEST_CALL = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_TEST_CALL);
	public static final int _P_CALL_PARTY_CATEGORY_PAYPHONE = 10;
	public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_PAYPHONE = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_PAYPHONE);
	public int value()
	{
		return value;
	}
	public static TpCallPartyCategory from_int(int value)
	{
		switch (value) {
			case _P_CALL_PARTY_CATEGORY_UNKNOWN: return P_CALL_PARTY_CATEGORY_UNKNOWN;
			case _P_CALL_PARTY_CATEGORY_OPERATOR_F: return P_CALL_PARTY_CATEGORY_OPERATOR_F;
			case _P_CALL_PARTY_CATEGORY_OPERATOR_E: return P_CALL_PARTY_CATEGORY_OPERATOR_E;
			case _P_CALL_PARTY_CATEGORY_OPERATOR_G: return P_CALL_PARTY_CATEGORY_OPERATOR_G;
			case _P_CALL_PARTY_CATEGORY_OPERATOR_R: return P_CALL_PARTY_CATEGORY_OPERATOR_R;
			case _P_CALL_PARTY_CATEGORY_OPERATOR_S: return P_CALL_PARTY_CATEGORY_OPERATOR_S;
			case _P_CALL_PARTY_CATEGORY_ORDINARY_SUB: return P_CALL_PARTY_CATEGORY_ORDINARY_SUB;
			case _P_CALL_PARTY_CATEGORY_PRIORITY_SUB: return P_CALL_PARTY_CATEGORY_PRIORITY_SUB;
			case _P_CALL_PARTY_CATEGORY_DATA_CALL: return P_CALL_PARTY_CATEGORY_DATA_CALL;
			case _P_CALL_PARTY_CATEGORY_TEST_CALL: return P_CALL_PARTY_CATEGORY_TEST_CALL;
			case _P_CALL_PARTY_CATEGORY_PAYPHONE: return P_CALL_PARTY_CATEGORY_PAYPHONE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallPartyCategory(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
