package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SAPS"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SAPS
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SAPS()
	{
		super(org.csapi.cm.P_UNKNOWN_SAPSHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SAPS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_SAPSHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SAPS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
