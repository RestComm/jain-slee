package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_FOLDER_IS_OPEN"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_FOLDER_IS_OPEN
	extends org.omg.CORBA.UserException
{
	public P_GMS_FOLDER_IS_OPEN()
	{
		super(org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_FOLDER_IS_OPEN(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_FOLDER_IS_OPEN(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
