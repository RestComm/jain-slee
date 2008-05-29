package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMACLDefault"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACLDefault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PAM_ACCESS_ALLOW = 0;
	public static final TpPAMACLDefault PAM_ACCESS_ALLOW = new TpPAMACLDefault(_PAM_ACCESS_ALLOW);
	public static final int _PAM_ACCESS_DENY = 1;
	public static final TpPAMACLDefault PAM_ACCESS_DENY = new TpPAMACLDefault(_PAM_ACCESS_DENY);
	public int value()
	{
		return value;
	}
	public static TpPAMACLDefault from_int(int value)
	{
		switch (value) {
			case _PAM_ACCESS_ALLOW: return PAM_ACCESS_ALLOW;
			case _PAM_ACCESS_DENY: return PAM_ACCESS_DENY;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMACLDefault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
