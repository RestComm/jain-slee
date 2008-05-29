package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_AGENT"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_AGENT
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNKNOWN_AGENT()
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNKNOWN_AGENT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNKNOWN_AGENT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
