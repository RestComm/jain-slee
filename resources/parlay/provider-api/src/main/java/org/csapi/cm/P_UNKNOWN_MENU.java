package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_MENU"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_MENU
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_MENU()
	{
		super(org.csapi.cm.P_UNKNOWN_MENUHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_MENU(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_MENUHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_MENU(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
