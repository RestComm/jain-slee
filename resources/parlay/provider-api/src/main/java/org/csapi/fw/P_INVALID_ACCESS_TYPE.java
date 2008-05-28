package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_ACCESS_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ACCESS_TYPE
	extends org.omg.CORBA.UserException
{
	public P_INVALID_ACCESS_TYPE()
	{
		super(org.csapi.fw.P_INVALID_ACCESS_TYPEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_ACCESS_TYPE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_INVALID_ACCESS_TYPEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_ACCESS_TYPE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
