package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SERVICE_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SERVICE_ID
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SERVICE_ID()
	{
		super(org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SERVICE_ID(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SERVICE_ID(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
