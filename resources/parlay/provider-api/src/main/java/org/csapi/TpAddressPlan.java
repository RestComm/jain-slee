package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPlan"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPlan
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ADDRESS_PLAN_NOT_PRESENT = 0;
	public static final TpAddressPlan P_ADDRESS_PLAN_NOT_PRESENT = new TpAddressPlan(_P_ADDRESS_PLAN_NOT_PRESENT);
	public static final int _P_ADDRESS_PLAN_UNDEFINED = 1;
	public static final TpAddressPlan P_ADDRESS_PLAN_UNDEFINED = new TpAddressPlan(_P_ADDRESS_PLAN_UNDEFINED);
	public static final int _P_ADDRESS_PLAN_IP = 2;
	public static final TpAddressPlan P_ADDRESS_PLAN_IP = new TpAddressPlan(_P_ADDRESS_PLAN_IP);
	public static final int _P_ADDRESS_PLAN_MULTICAST = 3;
	public static final TpAddressPlan P_ADDRESS_PLAN_MULTICAST = new TpAddressPlan(_P_ADDRESS_PLAN_MULTICAST);
	public static final int _P_ADDRESS_PLAN_UNICAST = 4;
	public static final TpAddressPlan P_ADDRESS_PLAN_UNICAST = new TpAddressPlan(_P_ADDRESS_PLAN_UNICAST);
	public static final int _P_ADDRESS_PLAN_E164 = 5;
	public static final TpAddressPlan P_ADDRESS_PLAN_E164 = new TpAddressPlan(_P_ADDRESS_PLAN_E164);
	public static final int _P_ADDRESS_PLAN_AESA = 6;
	public static final TpAddressPlan P_ADDRESS_PLAN_AESA = new TpAddressPlan(_P_ADDRESS_PLAN_AESA);
	public static final int _P_ADDRESS_PLAN_URL = 7;
	public static final TpAddressPlan P_ADDRESS_PLAN_URL = new TpAddressPlan(_P_ADDRESS_PLAN_URL);
	public static final int _P_ADDRESS_PLAN_NSAP = 8;
	public static final TpAddressPlan P_ADDRESS_PLAN_NSAP = new TpAddressPlan(_P_ADDRESS_PLAN_NSAP);
	public static final int _P_ADDRESS_PLAN_SMTP = 9;
	public static final TpAddressPlan P_ADDRESS_PLAN_SMTP = new TpAddressPlan(_P_ADDRESS_PLAN_SMTP);
	public static final int _P_ADDRESS_PLAN_MSMAIL = 10;
	public static final TpAddressPlan P_ADDRESS_PLAN_MSMAIL = new TpAddressPlan(_P_ADDRESS_PLAN_MSMAIL);
	public static final int _P_ADDRESS_PLAN_X400 = 11;
	public static final TpAddressPlan P_ADDRESS_PLAN_X400 = new TpAddressPlan(_P_ADDRESS_PLAN_X400);
	public static final int _P_ADDRESS_PLAN_SIP = 12;
	public static final TpAddressPlan P_ADDRESS_PLAN_SIP = new TpAddressPlan(_P_ADDRESS_PLAN_SIP);
	public static final int _P_ADDRESS_PLAN_ANY = 13;
	public static final TpAddressPlan P_ADDRESS_PLAN_ANY = new TpAddressPlan(_P_ADDRESS_PLAN_ANY);
	public static final int _P_ADDRESS_PLAN_NATIONAL = 14;
	public static final TpAddressPlan P_ADDRESS_PLAN_NATIONAL = new TpAddressPlan(_P_ADDRESS_PLAN_NATIONAL);
	public int value()
	{
		return value;
	}
	public static TpAddressPlan from_int(int value)
	{
		switch (value) {
			case _P_ADDRESS_PLAN_NOT_PRESENT: return P_ADDRESS_PLAN_NOT_PRESENT;
			case _P_ADDRESS_PLAN_UNDEFINED: return P_ADDRESS_PLAN_UNDEFINED;
			case _P_ADDRESS_PLAN_IP: return P_ADDRESS_PLAN_IP;
			case _P_ADDRESS_PLAN_MULTICAST: return P_ADDRESS_PLAN_MULTICAST;
			case _P_ADDRESS_PLAN_UNICAST: return P_ADDRESS_PLAN_UNICAST;
			case _P_ADDRESS_PLAN_E164: return P_ADDRESS_PLAN_E164;
			case _P_ADDRESS_PLAN_AESA: return P_ADDRESS_PLAN_AESA;
			case _P_ADDRESS_PLAN_URL: return P_ADDRESS_PLAN_URL;
			case _P_ADDRESS_PLAN_NSAP: return P_ADDRESS_PLAN_NSAP;
			case _P_ADDRESS_PLAN_SMTP: return P_ADDRESS_PLAN_SMTP;
			case _P_ADDRESS_PLAN_MSMAIL: return P_ADDRESS_PLAN_MSMAIL;
			case _P_ADDRESS_PLAN_X400: return P_ADDRESS_PLAN_X400;
			case _P_ADDRESS_PLAN_SIP: return P_ADDRESS_PLAN_SIP;
			case _P_ADDRESS_PLAN_ANY: return P_ADDRESS_PLAN_ANY;
			case _P_ADDRESS_PLAN_NATIONAL: return P_ADDRESS_PLAN_NATIONAL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAddressPlan(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
