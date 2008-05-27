package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_IS_CYCLIC"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_IS_CYCLIC
	extends org.omg.CORBA.UserException
{
	public P_PAM_IS_CYCLIC()
	{
		super(org.csapi.pam.P_PAM_IS_CYCLICHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_IS_CYCLIC(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_IS_CYCLICHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_IS_CYCLIC(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
