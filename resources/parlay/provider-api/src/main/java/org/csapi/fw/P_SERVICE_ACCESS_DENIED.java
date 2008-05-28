package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_SERVICE_ACCESS_DENIED"
 *	@author JacORB IDL compiler 
 */

public final class P_SERVICE_ACCESS_DENIED
	extends org.omg.CORBA.UserException
{
	public P_SERVICE_ACCESS_DENIED()
	{
		super(org.csapi.fw.P_SERVICE_ACCESS_DENIEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_SERVICE_ACCESS_DENIED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_SERVICE_ACCESS_DENIEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_SERVICE_ACCESS_DENIED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
