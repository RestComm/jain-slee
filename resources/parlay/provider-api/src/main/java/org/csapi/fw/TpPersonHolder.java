package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpPerson"
 *	@author JacORB IDL compiler 
 */

public final class TpPersonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpPerson value;

	public TpPersonHolder ()
	{
	}
	public TpPersonHolder(final org.csapi.fw.TpPerson initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpPersonHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpPersonHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpPersonHelper.write(_out, value);
	}
}
