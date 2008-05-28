package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_ADDITION_TO_SAG"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ADDITION_TO_SAG
	extends org.omg.CORBA.UserException
{
	public P_INVALID_ADDITION_TO_SAG()
	{
		super(org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_ADDITION_TO_SAG(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_ADDITION_TO_SAG(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
