package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_USER"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_USER
	extends org.omg.CORBA.UserException
{
	public P_INVALID_USER()
	{
		super(org.csapi.cs.P_INVALID_USERHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_USER(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cs.P_INVALID_USERHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_USER(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
