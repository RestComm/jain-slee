package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INVALID_FOLDER_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INVALID_FOLDER_ID
	extends org.omg.CORBA.UserException
{
	public P_GMS_INVALID_FOLDER_ID()
	{
		super(org.csapi.gms.P_GMS_INVALID_FOLDER_IDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_INVALID_FOLDER_ID(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_INVALID_FOLDER_IDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_INVALID_FOLDER_ID(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
