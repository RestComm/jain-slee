package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_TAG"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_TAGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_ILLEGAL_TAG value;

	public P_ILLEGAL_TAGHolder ()
	{
	}
	public P_ILLEGAL_TAGHolder(final org.csapi.cm.P_ILLEGAL_TAG initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_ILLEGAL_TAGHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_ILLEGAL_TAGHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_ILLEGAL_TAGHelper.write(_out, value);
	}
}
