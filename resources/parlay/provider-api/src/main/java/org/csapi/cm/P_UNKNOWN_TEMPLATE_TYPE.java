package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_TEMPLATE_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_TEMPLATE_TYPE
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_TEMPLATE_TYPE()
	{
		super(org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_TEMPLATE_TYPE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_TEMPLATE_TYPE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
