package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_AMOUNT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_AMOUNT
	extends org.omg.CORBA.UserException
{
	public P_INVALID_AMOUNT()
	{
		super(org.csapi.P_INVALID_AMOUNTHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_AMOUNT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_AMOUNTHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_AMOUNT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
