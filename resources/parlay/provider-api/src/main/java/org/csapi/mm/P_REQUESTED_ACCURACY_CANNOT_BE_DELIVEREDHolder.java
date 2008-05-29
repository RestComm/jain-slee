package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED"
 *	@author JacORB IDL compiler 
 */

public final class P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED value;

	public P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHolder ()
	{
	}
	public P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHolder(final org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.write(_out, value);
	}
}
