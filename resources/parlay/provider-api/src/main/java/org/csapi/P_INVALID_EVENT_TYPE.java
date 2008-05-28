package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_EVENT_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_EVENT_TYPE
	extends org.omg.CORBA.UserException
{
	public P_INVALID_EVENT_TYPE()
	{
		super(org.csapi.P_INVALID_EVENT_TYPEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_EVENT_TYPE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_EVENT_TYPEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_EVENT_TYPE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
