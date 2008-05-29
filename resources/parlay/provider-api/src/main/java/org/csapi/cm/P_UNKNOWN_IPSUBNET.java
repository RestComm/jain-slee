package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_IPSUBNET"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_IPSUBNET
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_IPSUBNET()
	{
		super(org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_IPSUBNET(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_IPSUBNET(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
