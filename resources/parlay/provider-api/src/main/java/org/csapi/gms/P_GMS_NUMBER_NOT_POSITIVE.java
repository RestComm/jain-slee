package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_NUMBER_NOT_POSITIVE"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_NUMBER_NOT_POSITIVE
	extends org.omg.CORBA.UserException
{
	public P_GMS_NUMBER_NOT_POSITIVE()
	{
		super(org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_NUMBER_NOT_POSITIVE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_NUMBER_NOT_POSITIVE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
