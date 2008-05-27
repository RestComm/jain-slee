package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMContextName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PAM_CONTEXT_ANY = 0;
	public static final TpPAMContextName PAM_CONTEXT_ANY = new TpPAMContextName(_PAM_CONTEXT_ANY);
	public static final int _PAM_CONTEXT_COMMUNICATION = 1;
	public static final TpPAMContextName PAM_CONTEXT_COMMUNICATION = new TpPAMContextName(_PAM_CONTEXT_COMMUNICATION);
	public int value()
	{
		return value;
	}
	public static TpPAMContextName from_int(int value)
	{
		switch (value) {
			case _PAM_CONTEXT_ANY: return PAM_CONTEXT_ANY;
			case _PAM_CONTEXT_COMMUNICATION: return PAM_CONTEXT_COMMUNICATION;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMContextName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
