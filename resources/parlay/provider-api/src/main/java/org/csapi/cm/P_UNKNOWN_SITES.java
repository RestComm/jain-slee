package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITES"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITES
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SITES()
	{
		super(org.csapi.cm.P_UNKNOWN_SITESHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SITES(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_SITESHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SITES(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
