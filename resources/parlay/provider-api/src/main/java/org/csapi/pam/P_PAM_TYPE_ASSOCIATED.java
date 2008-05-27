package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_TYPE_ASSOCIATED"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_TYPE_ASSOCIATED
	extends org.omg.CORBA.UserException
{
	public P_PAM_TYPE_ASSOCIATED()
	{
		super(org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_TYPE_ASSOCIATED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_TYPE_ASSOCIATED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
