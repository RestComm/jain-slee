package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INVALID_AUTHENTICATION_INFORMATION"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INVALID_AUTHENTICATION_INFORMATION
	extends org.omg.CORBA.UserException
{
	public P_GMS_INVALID_AUTHENTICATION_INFORMATION()
	{
		super(org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_INVALID_AUTHENTICATION_INFORMATION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_INVALID_AUTHENTICATION_INFORMATION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
