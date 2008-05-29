package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMADEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMADEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMADEventData value;

	public TpPAMADEventDataHolder ()
	{
	}
	public TpPAMADEventDataHolder(final org.csapi.pam.TpPAMADEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMADEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMADEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMADEventDataHelper.write(_out, value);
	}
}
