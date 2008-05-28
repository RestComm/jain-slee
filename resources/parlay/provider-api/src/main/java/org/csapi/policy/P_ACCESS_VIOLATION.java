package org.csapi.policy;

/**
 *	Generated from IDL definition of exception "P_ACCESS_VIOLATION"
 *	@author JacORB IDL compiler 
 */

public final class P_ACCESS_VIOLATION
	extends org.omg.CORBA.UserException
{
	public P_ACCESS_VIOLATION()
	{
		super(org.csapi.policy.P_ACCESS_VIOLATIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ACCESS_VIOLATION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.policy.P_ACCESS_VIOLATIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ACCESS_VIOLATION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
