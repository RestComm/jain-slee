package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INSUFFICIENT_PRIVILEGE"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INSUFFICIENT_PRIVILEGE
	extends org.omg.CORBA.UserException
{
	public P_GMS_INSUFFICIENT_PRIVILEGE()
	{
		super(org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_INSUFFICIENT_PRIVILEGE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_INSUFFICIENT_PRIVILEGE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
