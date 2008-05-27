package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpApplicationDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpApplicationDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpApplicationDescription value;

	public TpApplicationDescriptionHolder ()
	{
	}
	public TpApplicationDescriptionHolder(final org.csapi.cs.TpApplicationDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpApplicationDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpApplicationDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpApplicationDescriptionHelper.write(_out, value);
	}
}
