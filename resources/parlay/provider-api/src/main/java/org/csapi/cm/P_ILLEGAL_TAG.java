package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_TAG"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_TAG
	extends org.omg.CORBA.UserException
{
	public P_ILLEGAL_TAG()
	{
		super(org.csapi.cm.P_ILLEGAL_TAGHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_ILLEGAL_TAG(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.cm.P_ILLEGAL_TAGHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_ILLEGAL_TAG(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
