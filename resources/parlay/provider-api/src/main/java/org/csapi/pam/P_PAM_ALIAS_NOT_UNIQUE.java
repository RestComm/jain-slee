package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_ALIAS_NOT_UNIQUE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_ALIAS_NOT_UNIQUE
	extends org.omg.CORBA.UserException
{
	public P_PAM_ALIAS_NOT_UNIQUE()
	{
		super(org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_ALIAS_NOT_UNIQUE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_ALIAS_NOT_UNIQUE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
