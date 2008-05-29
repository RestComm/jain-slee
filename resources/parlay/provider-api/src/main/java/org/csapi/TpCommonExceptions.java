package org.csapi;

/**
 *	Generated from IDL definition of exception "TpCommonExceptions"
 *	@author JacORB IDL compiler 
 */

public final class TpCommonExceptions
	extends org.omg.CORBA.UserException
{
	public TpCommonExceptions()
	{
		super(org.csapi.TpCommonExceptionsHelper.id());
	}

	public int ExceptionType;
	public java.lang.String ExtraInformation;
	public TpCommonExceptions(java.lang.String _reason,int ExceptionType, java.lang.String ExtraInformation)
	{
		super(org.csapi.TpCommonExceptionsHelper.id()+""+_reason);
		this.ExceptionType = ExceptionType;
		this.ExtraInformation = ExtraInformation;
	}
	public TpCommonExceptions(int ExceptionType, java.lang.String ExtraInformation)
	{
		this.ExceptionType = ExceptionType;
		this.ExtraInformation = ExtraInformation;
	}
}
