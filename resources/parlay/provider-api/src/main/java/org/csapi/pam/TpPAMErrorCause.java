package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMErrorCause"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMErrorCause
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_PAM_CAUSE_UNDEFINED = 0;
	public static final TpPAMErrorCause P_PAM_CAUSE_UNDEFINED = new TpPAMErrorCause(_P_PAM_CAUSE_UNDEFINED);
	public static final int _P_PAM_CAUSE_INVALID_ADDRESS = 1;
	public static final TpPAMErrorCause P_PAM_CAUSE_INVALID_ADDRESS = new TpPAMErrorCause(_P_PAM_CAUSE_INVALID_ADDRESS);
	public static final int _P_PAM_CAUSE_SYSTEM_FAILURE = 2;
	public static final TpPAMErrorCause P_PAM_CAUSE_SYSTEM_FAILURE = new TpPAMErrorCause(_P_PAM_CAUSE_SYSTEM_FAILURE);
	public static final int _P_PAM_CAUSE_INFO_UNAVAILABLE = 3;
	public static final TpPAMErrorCause P_PAM_CAUSE_INFO_UNAVAILABLE = new TpPAMErrorCause(_P_PAM_CAUSE_INFO_UNAVAILABLE);
	public static final int _P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED = 4;
	public static final TpPAMErrorCause P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED = new TpPAMErrorCause(_P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED);
	public int value()
	{
		return value;
	}
	public static TpPAMErrorCause from_int(int value)
	{
		switch (value) {
			case _P_PAM_CAUSE_UNDEFINED: return P_PAM_CAUSE_UNDEFINED;
			case _P_PAM_CAUSE_INVALID_ADDRESS: return P_PAM_CAUSE_INVALID_ADDRESS;
			case _P_PAM_CAUSE_SYSTEM_FAILURE: return P_PAM_CAUSE_SYSTEM_FAILURE;
			case _P_PAM_CAUSE_INFO_UNAVAILABLE: return P_PAM_CAUSE_INFO_UNAVAILABLE;
			case _P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED: return P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMErrorCause(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
