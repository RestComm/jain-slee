package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_GROUP"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_GROUP
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNKNOWN_GROUP()
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNKNOWN_GROUP(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNKNOWN_GROUP(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
