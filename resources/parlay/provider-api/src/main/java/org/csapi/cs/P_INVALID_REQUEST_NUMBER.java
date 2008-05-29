package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_REQUEST_NUMBER"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_REQUEST_NUMBER
	extends org.omg.CORBA.UserException
{
	public P_INVALID_REQUEST_NUMBER()
	{
		super(org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_REQUEST_NUMBER(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_REQUEST_NUMBER(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
