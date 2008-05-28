package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ASSIGNMENT"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ASSIGNMENT
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNKNOWN_ASSIGNMENT()
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNKNOWN_ASSIGNMENT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNKNOWN_ASSIGNMENT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
