package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SUBSCRIBER"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SUBSCRIBER
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_SUBSCRIBER()
	{
		super(org.csapi.P_UNKNOWN_SUBSCRIBERHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_SUBSCRIBER(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_UNKNOWN_SUBSCRIBERHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_SUBSCRIBER(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
