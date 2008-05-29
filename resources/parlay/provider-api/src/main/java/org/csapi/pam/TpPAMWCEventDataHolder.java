package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMWCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMWCEventData value;

	public TpPAMWCEventDataHolder ()
	{
	}
	public TpPAMWCEventDataHolder(final org.csapi.pam.TpPAMWCEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMWCEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMWCEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMWCEventDataHelper.write(_out, value);
	}
}
