package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_RANGE"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_RANGE
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_RANGE()
	{
		super(org.csapi.ui.P_ILLEGAL_RANGEHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_RANGE(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.ui.P_ILLEGAL_RANGEHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_RANGE(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
