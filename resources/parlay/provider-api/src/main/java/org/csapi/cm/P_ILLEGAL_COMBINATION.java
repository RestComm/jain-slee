package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_COMBINATION"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_COMBINATION
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_COMBINATION()
	{
		super(org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_COMBINATION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_COMBINATION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
