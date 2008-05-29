package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_ADDRESS"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ADDRESS
	extends org.omg.CORBA.UserException
{
	public P_INVALID_ADDRESS()
	{
		super(org.csapi.P_INVALID_ADDRESSHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_ADDRESS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_ADDRESSHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_ADDRESS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
