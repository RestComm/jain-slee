package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_IPSUBNET"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_IPSUBNETHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_IPSUBNET value;

	public P_UNKNOWN_IPSUBNETHolder ()
	{
	}
	public P_UNKNOWN_IPSUBNETHolder(final org.csapi.cm.P_UNKNOWN_IPSUBNET initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.write(_out, value);
	}
}
