package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_PROPERTY_TYPE_MISMATCH"
 *	@author JacORB IDL compiler 
 */

public final class P_PROPERTY_TYPE_MISMATCH
	extends org.omg.CORBA.UserException
{
	public P_PROPERTY_TYPE_MISMATCH()
	{
		super(org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PROPERTY_TYPE_MISMATCH(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PROPERTY_TYPE_MISMATCH(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
