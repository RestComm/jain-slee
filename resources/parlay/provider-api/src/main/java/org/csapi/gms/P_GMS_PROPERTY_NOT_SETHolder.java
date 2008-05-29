package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_PROPERTY_NOT_SET"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_PROPERTY_NOT_SETHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_PROPERTY_NOT_SET value;

	public P_GMS_PROPERTY_NOT_SETHolder ()
	{
	}
	public P_GMS_PROPERTY_NOT_SETHolder(final org.csapi.gms.P_GMS_PROPERTY_NOT_SET initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.write(_out, value);
	}
}
