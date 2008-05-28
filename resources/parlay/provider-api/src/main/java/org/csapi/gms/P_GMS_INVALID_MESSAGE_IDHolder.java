package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INVALID_MESSAGE_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INVALID_MESSAGE_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_INVALID_MESSAGE_ID value;

	public P_GMS_INVALID_MESSAGE_IDHolder ()
	{
	}
	public P_GMS_INVALID_MESSAGE_IDHolder(final org.csapi.gms.P_GMS_INVALID_MESSAGE_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, value);
	}
}
