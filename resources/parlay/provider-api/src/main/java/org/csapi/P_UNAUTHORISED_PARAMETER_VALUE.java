package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNAUTHORISED_PARAMETER_VALUE"
 *	@author JacORB IDL compiler 
 */

public final class P_UNAUTHORISED_PARAMETER_VALUE
	extends org.omg.CORBA.UserException
{
	public P_UNAUTHORISED_PARAMETER_VALUE()
	{
		super(org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNAUTHORISED_PARAMETER_VALUE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNAUTHORISED_PARAMETER_VALUE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
