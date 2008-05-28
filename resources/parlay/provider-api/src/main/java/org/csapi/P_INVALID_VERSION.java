package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_VERSION"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_VERSION
	extends org.omg.CORBA.UserException
{
	public P_INVALID_VERSION()
	{
		super(org.csapi.P_INVALID_VERSIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_VERSION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_VERSIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_VERSION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
