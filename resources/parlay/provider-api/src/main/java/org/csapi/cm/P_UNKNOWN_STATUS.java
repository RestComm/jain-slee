package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_STATUS"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_STATUS
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_STATUS()
	{
		super(org.csapi.cm.P_UNKNOWN_STATUSHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_STATUS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_STATUSHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_STATUS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
