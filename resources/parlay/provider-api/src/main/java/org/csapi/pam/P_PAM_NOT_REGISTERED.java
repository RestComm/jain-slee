package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_NOT_REGISTERED"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_NOT_REGISTERED
	extends org.omg.CORBA.UserException
{
	public P_PAM_NOT_REGISTERED()
	{
		super(org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_NOT_REGISTERED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_NOT_REGISTERED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
