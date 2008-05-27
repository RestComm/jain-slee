package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAttribute value;

	public TpAttributeHolder ()
	{
	}
	public TpAttributeHolder(final org.csapi.TpAttribute initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpAttributeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpAttributeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpAttributeHelper.write(_out, value);
	}
}
