package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INSUFFICIENT_PRIVILEGE"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INSUFFICIENT_PRIVILEGEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE value;

	public P_GMS_INSUFFICIENT_PRIVILEGEHolder ()
	{
	}
	public P_GMS_INSUFFICIENT_PRIVILEGEHolder(final org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.write(_out, value);
	}
}
