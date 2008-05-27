package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_MESSAGE_NOT_REMOVED"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_MESSAGE_NOT_REMOVED
	extends org.omg.CORBA.UserException
{
	public P_GMS_MESSAGE_NOT_REMOVED()
	{
		super(org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_MESSAGE_NOT_REMOVED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_MESSAGE_NOT_REMOVED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
