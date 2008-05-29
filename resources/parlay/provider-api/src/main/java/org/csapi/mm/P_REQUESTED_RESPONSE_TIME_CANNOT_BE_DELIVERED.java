package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED"
 *	@author JacORB IDL compiler 
 */

public final class P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED
	extends org.omg.CORBA.UserException
{
	public P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED()
	{
		super(org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
