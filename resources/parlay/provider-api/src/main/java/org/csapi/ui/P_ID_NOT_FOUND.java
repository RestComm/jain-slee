package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ID_NOT_FOUND"
 *	@author JacORB IDL compiler 
 */

public final class P_ID_NOT_FOUND
	extends org.omg.CORBA.UserException
{
	public P_ID_NOT_FOUND()
	{
		super(org.csapi.ui.P_ID_NOT_FOUNDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ID_NOT_FOUND(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.ui.P_ID_NOT_FOUNDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ID_NOT_FOUND(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
