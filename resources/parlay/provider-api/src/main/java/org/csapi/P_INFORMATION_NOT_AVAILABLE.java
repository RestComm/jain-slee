package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INFORMATION_NOT_AVAILABLE"
 *	@author JacORB IDL compiler 
 */

public final class P_INFORMATION_NOT_AVAILABLE
	extends org.omg.CORBA.UserException
{
	public P_INFORMATION_NOT_AVAILABLE()
	{
		super(org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INFORMATION_NOT_AVAILABLE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INFORMATION_NOT_AVAILABLE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
