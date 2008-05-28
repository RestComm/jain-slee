package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_REF_VALUE"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_REF_VALUE
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_REF_VALUE()
	{
		super(org.csapi.cm.P_ILLEGAL_REF_VALUEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_REF_VALUE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_ILLEGAL_REF_VALUEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_REF_VALUE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
