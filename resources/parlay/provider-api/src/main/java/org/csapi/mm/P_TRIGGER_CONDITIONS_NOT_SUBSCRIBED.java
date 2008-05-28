package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED"
 *	@author JacORB IDL compiler 
 */

public final class P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED
	extends org.omg.CORBA.UserException
{
	public P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED()
	{
		super(org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
