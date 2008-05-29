package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ATTRIBUTE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ATTRIBUTE
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNKNOWN_ATTRIBUTE()
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNKNOWN_ATTRIBUTE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNKNOWN_ATTRIBUTE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
