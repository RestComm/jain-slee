package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_VPRPID"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_VPRPID
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_VPRPID()
	{
		super(org.csapi.cm.P_ILLEGAL_VPRPIDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_VPRPID(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_ILLEGAL_VPRPIDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_VPRPID(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
