package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITE_DESCRIPTION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITE_DESCRIPTION
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SITE_DESCRIPTION()
	{
		super(org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SITE_DESCRIPTION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SITE_DESCRIPTION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
