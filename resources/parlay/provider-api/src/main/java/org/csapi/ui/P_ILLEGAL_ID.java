package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_ID
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_ID()
	{
		super(org.csapi.ui.P_ILLEGAL_IDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_ID(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.ui.P_ILLEGAL_IDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_ID(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
