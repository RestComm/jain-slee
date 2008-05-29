package org.csapi;

/**
 *	Generated from IDL definition of exception "P_APPLICATION_NOT_ACTIVATED"
 *	@author JacORB IDL compiler 
 */

public final class P_APPLICATION_NOT_ACTIVATED
	extends org.omg.CORBA.UserException
{
	public P_APPLICATION_NOT_ACTIVATED()
	{
		super(org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_APPLICATION_NOT_ACTIVATED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_APPLICATION_NOT_ACTIVATED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
