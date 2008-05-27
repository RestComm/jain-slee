package org.csapi.policy;

/**
 *	Generated from IDL definition of exception "P_SYNTAX_ERROR"
 *	@author JacORB IDL compiler 
 */

public final class P_SYNTAX_ERROR
	extends org.omg.CORBA.UserException
{
	public P_SYNTAX_ERROR()
	{
		super(org.csapi.policy.P_SYNTAX_ERRORHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_SYNTAX_ERROR(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.policy.P_SYNTAX_ERRORHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_SYNTAX_ERROR(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
