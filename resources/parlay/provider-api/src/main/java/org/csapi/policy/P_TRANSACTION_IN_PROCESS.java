package org.csapi.policy;

/**
 *	Generated from IDL definition of exception "P_TRANSACTION_IN_PROCESS"
 *	@author JacORB IDL compiler 
 */

public final class P_TRANSACTION_IN_PROCESS
	extends org.omg.CORBA.UserException
{
	public P_TRANSACTION_IN_PROCESS()
	{
		super(org.csapi.policy.P_TRANSACTION_IN_PROCESSHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_TRANSACTION_IN_PROCESS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.policy.P_TRANSACTION_IN_PROCESSHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_TRANSACTION_IN_PROCESS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
