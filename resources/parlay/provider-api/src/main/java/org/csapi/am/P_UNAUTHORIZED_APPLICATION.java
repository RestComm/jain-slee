package org.csapi.am;

/**
 *	Generated from IDL definition of exception "P_UNAUTHORIZED_APPLICATION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNAUTHORIZED_APPLICATION
	extends org.omg.CORBA.UserException
{
	public P_UNAUTHORIZED_APPLICATION()
	{
		super(org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNAUTHORIZED_APPLICATION(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNAUTHORIZED_APPLICATION(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
