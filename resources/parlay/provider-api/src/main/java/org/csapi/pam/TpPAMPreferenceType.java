package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMPreferenceType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PAM_ACCESS_LIST = 0;
	public static final TpPAMPreferenceType PAM_ACCESS_LIST = new TpPAMPreferenceType(_PAM_ACCESS_LIST);
	public static final int _PAM_EXTERNAL_CONTROL = 1;
	public static final TpPAMPreferenceType PAM_EXTERNAL_CONTROL = new TpPAMPreferenceType(_PAM_EXTERNAL_CONTROL);
	public int value()
	{
		return value;
	}
	public static TpPAMPreferenceType from_int(int value)
	{
		switch (value) {
			case _PAM_ACCESS_LIST: return PAM_ACCESS_LIST;
			case _PAM_EXTERNAL_CONTROL: return PAM_EXTERNAL_CONTROL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMPreferenceType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
