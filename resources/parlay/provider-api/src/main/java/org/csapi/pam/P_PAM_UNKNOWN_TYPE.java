package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_TYPE
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNKNOWN_TYPE()
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNKNOWN_TYPE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNKNOWN_TYPE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
