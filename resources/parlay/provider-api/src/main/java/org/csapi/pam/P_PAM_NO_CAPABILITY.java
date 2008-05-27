package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_NO_CAPABILITY"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_NO_CAPABILITY
	extends org.omg.CORBA.UserException
{
	public P_PAM_NO_CAPABILITY()
	{
		super(org.csapi.pam.P_PAM_NO_CAPABILITYHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_NO_CAPABILITY(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_NO_CAPABILITYHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_NO_CAPABILITY(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
