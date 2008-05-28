package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_VOLUME"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_VOLUME
	extends org.omg.CORBA.UserException
{
	public P_INVALID_VOLUME()
	{
		super(org.csapi.cs.P_INVALID_VOLUMEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_INVALID_VOLUME(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cs.P_INVALID_VOLUMEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_INVALID_VOLUME(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
