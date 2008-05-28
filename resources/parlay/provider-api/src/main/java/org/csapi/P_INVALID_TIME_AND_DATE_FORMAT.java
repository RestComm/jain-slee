package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_TIME_AND_DATE_FORMAT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_TIME_AND_DATE_FORMAT
	extends org.omg.CORBA.UserException
{
	public P_INVALID_TIME_AND_DATE_FORMAT()
	{
		super(org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_TIME_AND_DATE_FORMAT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_TIME_AND_DATE_FORMAT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
