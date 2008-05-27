package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_NUMBER_NOT_POSITIVE"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_NUMBER_NOT_POSITIVEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE value;

	public P_GMS_NUMBER_NOT_POSITIVEHolder ()
	{
	}
	public P_GMS_NUMBER_NOT_POSITIVEHolder(final org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.write(_out, value);
	}
}
