package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY
	extends org.omg.CORBA.UserException
{
	public P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY()
	{
		super(org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
