package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_NETWORK_STATE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_NETWORK_STATE
	extends org.omg.CORBA.UserException
{
	public P_INVALID_NETWORK_STATE()
	{
		super(org.csapi.P_INVALID_NETWORK_STATEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_NETWORK_STATE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INVALID_NETWORK_STATEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_NETWORK_STATE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
