package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNASSIGNED_ALIAS"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNASSIGNED_ALIAS
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNASSIGNED_ALIAS()
	{
		super(org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNASSIGNED_ALIAS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNASSIGNED_ALIAS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
