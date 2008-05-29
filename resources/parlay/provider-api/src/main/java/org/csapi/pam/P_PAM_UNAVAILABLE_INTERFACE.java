package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNAVAILABLE_INTERFACE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNAVAILABLE_INTERFACE
	extends org.omg.CORBA.UserException
{
	public P_PAM_UNAVAILABLE_INTERFACE()
	{
		super(org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_UNAVAILABLE_INTERFACE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_UNAVAILABLE_INTERFACE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
