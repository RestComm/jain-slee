package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT
	extends org.omg.CORBA.UserException
{
	public P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT()
	{
		super(org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
