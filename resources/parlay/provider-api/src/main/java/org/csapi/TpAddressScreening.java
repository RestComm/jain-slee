package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressScreening"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressScreening
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ADDRESS_SCREENING_UNDEFINED = 0;
	public static final TpAddressScreening P_ADDRESS_SCREENING_UNDEFINED = new TpAddressScreening(_P_ADDRESS_SCREENING_UNDEFINED);
	public static final int _P_ADDRESS_SCREENING_USER_VERIFIED_PASSED = 1;
	public static final TpAddressScreening P_ADDRESS_SCREENING_USER_VERIFIED_PASSED = new TpAddressScreening(_P_ADDRESS_SCREENING_USER_VERIFIED_PASSED);
	public static final int _P_ADDRESS_SCREENING_USER_NOT_VERIFIED = 2;
	public static final TpAddressScreening P_ADDRESS_SCREENING_USER_NOT_VERIFIED = new TpAddressScreening(_P_ADDRESS_SCREENING_USER_NOT_VERIFIED);
	public static final int _P_ADDRESS_SCREENING_USER_VERIFIED_FAILED = 3;
	public static final TpAddressScreening P_ADDRESS_SCREENING_USER_VERIFIED_FAILED = new TpAddressScreening(_P_ADDRESS_SCREENING_USER_VERIFIED_FAILED);
	public static final int _P_ADDRESS_SCREENING_NETWORK = 4;
	public static final TpAddressScreening P_ADDRESS_SCREENING_NETWORK = new TpAddressScreening(_P_ADDRESS_SCREENING_NETWORK);
	public int value()
	{
		return value;
	}
	public static TpAddressScreening from_int(int value)
	{
		switch (value) {
			case _P_ADDRESS_SCREENING_UNDEFINED: return P_ADDRESS_SCREENING_UNDEFINED;
			case _P_ADDRESS_SCREENING_USER_VERIFIED_PASSED: return P_ADDRESS_SCREENING_USER_VERIFIED_PASSED;
			case _P_ADDRESS_SCREENING_USER_NOT_VERIFIED: return P_ADDRESS_SCREENING_USER_NOT_VERIFIED;
			case _P_ADDRESS_SCREENING_USER_VERIFIED_FAILED: return P_ADDRESS_SCREENING_USER_VERIFIED_FAILED;
			case _P_ADDRESS_SCREENING_NETWORK: return P_ADDRESS_SCREENING_NETWORK;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAddressScreening(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
