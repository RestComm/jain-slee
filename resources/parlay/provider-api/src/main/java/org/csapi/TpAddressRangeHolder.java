package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAddressRange"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressRangeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAddressRange value;

	public TpAddressRangeHolder ()
	{
	}
	public TpAddressRangeHolder(final org.csapi.TpAddressRange initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpAddressRangeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpAddressRangeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpAddressRangeHelper.write(_out, value);
	}
}
