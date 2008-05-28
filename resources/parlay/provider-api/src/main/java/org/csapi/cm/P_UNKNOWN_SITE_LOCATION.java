package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITE_LOCATION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITE_LOCATION
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SITE_LOCATION()
	{
		super(org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SITE_LOCATION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SITE_LOCATION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
