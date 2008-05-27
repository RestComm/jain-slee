package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_SERVICE_NOT_ENABLED"
 *	@author JacORB IDL compiler 
 */

public final class P_SERVICE_NOT_ENABLED
	extends org.omg.CORBA.UserException
{
	public P_SERVICE_NOT_ENABLED()
	{
		super(org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_SERVICE_NOT_ENABLED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_SERVICE_NOT_ENABLED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
