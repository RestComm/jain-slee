package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMPresenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPresenceDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMPresenceData value;

	public TpPAMPresenceDataHolder ()
	{
	}
	public TpPAMPresenceDataHolder(final org.csapi.pam.TpPAMPresenceData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMPresenceDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMPresenceDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMPresenceDataHelper.write(_out, value);
	}
}
