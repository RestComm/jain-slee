package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_PROPERTY_NOT_SET"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_PROPERTY_NOT_SET
	extends org.omg.CORBA.UserException
{
	public P_GMS_PROPERTY_NOT_SET()
	{
		super(org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_PROPERTY_NOT_SET(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_PROPERTY_NOT_SET(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
