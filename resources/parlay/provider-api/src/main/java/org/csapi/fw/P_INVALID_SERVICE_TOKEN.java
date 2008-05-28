package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SERVICE_TOKEN"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SERVICE_TOKEN
	extends org.omg.CORBA.UserException
{
	public P_INVALID_SERVICE_TOKEN()
	{
		super(org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_SERVICE_TOKEN(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_SERVICE_TOKEN(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
