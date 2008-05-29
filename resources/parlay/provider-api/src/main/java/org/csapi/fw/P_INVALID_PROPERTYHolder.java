package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_PROPERTY"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_PROPERTYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_PROPERTY value;

	public P_INVALID_PROPERTYHolder ()
	{
	}
	public P_INVALID_PROPERTYHolder(final org.csapi.fw.P_INVALID_PROPERTY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_PROPERTYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_PROPERTYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_PROPERTYHelper.write(_out, value);
	}
}
