package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_SERVICE_TYPE_UNAVAILABLE"
 *	@author JacORB IDL compiler 
 */

public final class P_SERVICE_TYPE_UNAVAILABLE
	extends org.omg.CORBA.UserException
{
	public P_SERVICE_TYPE_UNAVAILABLE()
	{
		super(org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_SERVICE_TYPE_UNAVAILABLE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_SERVICE_TYPE_UNAVAILABLE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
