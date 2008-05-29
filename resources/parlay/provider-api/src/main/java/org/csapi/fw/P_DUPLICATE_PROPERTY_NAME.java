package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_DUPLICATE_PROPERTY_NAME"
 *	@author JacORB IDL compiler 
 */

public final class P_DUPLICATE_PROPERTY_NAME
	extends org.omg.CORBA.UserException
{
	public P_DUPLICATE_PROPERTY_NAME()
	{
		super(org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_DUPLICATE_PROPERTY_NAME(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_DUPLICATE_PROPERTY_NAME(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
