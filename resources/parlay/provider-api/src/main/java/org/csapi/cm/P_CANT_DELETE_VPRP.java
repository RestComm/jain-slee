package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_CANT_DELETE_VPRP"
 *	@author JacORB IDL compiler 
 */

public final class P_CANT_DELETE_VPRP
	extends org.omg.CORBA.UserException
{
	public P_CANT_DELETE_VPRP()
	{
		super(org.csapi.cm.P_CANT_DELETE_VPRPHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_CANT_DELETE_VPRP(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_CANT_DELETE_VPRPHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_CANT_DELETE_VPRP(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
