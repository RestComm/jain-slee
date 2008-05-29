package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_DUPLICATE_PROPERTY_NAME"
 *	@author JacORB IDL compiler 
 */

public final class P_DUPLICATE_PROPERTY_NAMEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_DUPLICATE_PROPERTY_NAME value;

	public P_DUPLICATE_PROPERTY_NAMEHolder ()
	{
	}
	public P_DUPLICATE_PROPERTY_NAMEHolder(final org.csapi.fw.P_DUPLICATE_PROPERTY_NAME initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.write(_out, value);
	}
}
