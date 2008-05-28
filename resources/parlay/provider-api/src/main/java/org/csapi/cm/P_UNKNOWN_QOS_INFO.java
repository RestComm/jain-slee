package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_QOS_INFO"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_QOS_INFO
	extends org.omg.CORBA.UserException
{
	public P_UNKNOWN_QOS_INFO()
	{
		super(org.csapi.cm.P_UNKNOWN_QOS_INFOHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_UNKNOWN_QOS_INFO(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_UNKNOWN_QOS_INFOHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_UNKNOWN_QOS_INFO(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
