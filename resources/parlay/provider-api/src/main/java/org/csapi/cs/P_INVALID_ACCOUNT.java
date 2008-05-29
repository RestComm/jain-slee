package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_ACCOUNT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ACCOUNT
	extends org.omg.CORBA.UserException
{
	public P_INVALID_ACCOUNT()
	{
		super(org.csapi.cs.P_INVALID_ACCOUNTHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_ACCOUNT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cs.P_INVALID_ACCOUNTHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_ACCOUNT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
