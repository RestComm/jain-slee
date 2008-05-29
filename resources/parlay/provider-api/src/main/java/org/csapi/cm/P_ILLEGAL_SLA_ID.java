package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_SLA_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_SLA_ID
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_SLA_ID()
	{
		super(org.csapi.cm.P_ILLEGAL_SLA_IDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_SLA_ID(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_ILLEGAL_SLA_IDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_SLA_ID(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
