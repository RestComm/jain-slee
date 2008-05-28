package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallLegConnectionProperties"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegConnectionPropertiesHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallLegConnectionProperties value;

	public TpCallLegConnectionPropertiesHolder ()
	{
	}
	public TpCallLegConnectionPropertiesHolder(final org.csapi.cc.TpCallLegConnectionProperties initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallLegConnectionPropertiesHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallLegConnectionPropertiesHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallLegConnectionPropertiesHelper.write(_out, value);
	}
}
