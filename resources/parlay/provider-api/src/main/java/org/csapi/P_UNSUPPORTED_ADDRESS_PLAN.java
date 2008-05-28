package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNSUPPORTED_ADDRESS_PLAN"
 *	@author JacORB IDL compiler 
 */

public final class P_UNSUPPORTED_ADDRESS_PLAN
	extends org.omg.CORBA.UserException
{
	public P_UNSUPPORTED_ADDRESS_PLAN()
	{
		super(org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNSUPPORTED_ADDRESS_PLAN(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNSUPPORTED_ADDRESS_PLAN(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
