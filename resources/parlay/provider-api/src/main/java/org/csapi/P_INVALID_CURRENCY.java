package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_CURRENCY"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_CURRENCY
	extends org.omg.CORBA.UserException
{
	public P_INVALID_CURRENCY()
	{
		super(org.csapi.P_INVALID_CURRENCYHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_CURRENCY(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_CURRENCYHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_CURRENCY(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
