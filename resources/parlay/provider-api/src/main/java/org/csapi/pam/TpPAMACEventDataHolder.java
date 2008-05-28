package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMACEventData value;

	public TpPAMACEventDataHolder ()
	{
	}
	public TpPAMACEventDataHolder(final org.csapi.pam.TpPAMACEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMACEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMACEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMACEventDataHelper.write(_out, value);
	}
}
