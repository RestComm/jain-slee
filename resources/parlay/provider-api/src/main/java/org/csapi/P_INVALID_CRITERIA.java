package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_CRITERIA"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_CRITERIA
	extends org.omg.CORBA.UserException
{
	public P_INVALID_CRITERIA()
	{
		super(org.csapi.P_INVALID_CRITERIAHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_CRITERIA(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_CRITERIAHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_CRITERIA(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
