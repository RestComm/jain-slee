package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMGMCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMGMCEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMGMCEventData value;

	public TpPAMGMCEventDataHolder ()
	{
	}
	public TpPAMGMCEventDataHolder(final org.csapi.pam.TpPAMGMCEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMGMCEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMGMCEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMGMCEventDataHelper.write(_out, value);
	}
}
