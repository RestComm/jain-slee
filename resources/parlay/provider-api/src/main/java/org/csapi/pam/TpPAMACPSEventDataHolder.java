package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACPSEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMACPSEventData value;

	public TpPAMACPSEventDataHolder ()
	{
	}
	public TpPAMACPSEventDataHolder(final org.csapi.pam.TpPAMACPSEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMACPSEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMACPSEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMACPSEventDataHelper.write(_out, value);
	}
}
