package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_TEMPLATES"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_TEMPLATES
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_TEMPLATES()
	{
		super(org.csapi.cm.P_UNKNOWN_TEMPLATESHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_TEMPLATES(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_TEMPLATESHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_TEMPLATES(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
