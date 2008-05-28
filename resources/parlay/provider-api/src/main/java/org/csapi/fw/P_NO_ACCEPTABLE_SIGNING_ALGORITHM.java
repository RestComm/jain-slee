package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_NO_ACCEPTABLE_SIGNING_ALGORITHM"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_ACCEPTABLE_SIGNING_ALGORITHM
	extends org.omg.CORBA.UserException
{
	public P_NO_ACCEPTABLE_SIGNING_ALGORITHM()
	{
		super(org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_NO_ACCEPTABLE_SIGNING_ALGORITHM(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_NO_ACCEPTABLE_SIGNING_ALGORITHM(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
