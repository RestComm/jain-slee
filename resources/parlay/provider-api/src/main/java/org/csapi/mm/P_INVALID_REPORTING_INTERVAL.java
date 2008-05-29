package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_INVALID_REPORTING_INTERVAL"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_REPORTING_INTERVAL
	extends org.omg.CORBA.UserException
{
	public P_INVALID_REPORTING_INTERVAL()
	{
		super(org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_REPORTING_INTERVAL(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_REPORTING_INTERVAL(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
