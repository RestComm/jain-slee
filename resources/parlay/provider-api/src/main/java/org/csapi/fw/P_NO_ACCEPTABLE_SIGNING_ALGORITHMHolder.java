package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_NO_ACCEPTABLE_SIGNING_ALGORITHM"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_ACCEPTABLE_SIGNING_ALGORITHMHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM value;

	public P_NO_ACCEPTABLE_SIGNING_ALGORITHMHolder ()
	{
	}
	public P_NO_ACCEPTABLE_SIGNING_ALGORITHMHolder(final org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.write(_out, value);
	}
}
