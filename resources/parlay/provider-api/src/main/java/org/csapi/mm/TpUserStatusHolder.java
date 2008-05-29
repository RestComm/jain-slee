package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserStatus value;

	public TpUserStatusHolder ()
	{
	}
	public TpUserStatusHolder(final org.csapi.mm.TpUserStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpUserStatusHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpUserStatusHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpUserStatusHelper.write(_out, value);
	}
}
