package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_PIPEQOSINFO"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_PIPEQOSINFOHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_PIPEQOSINFO value;

	public P_UNKNOWN_PIPEQOSINFOHolder ()
	{
	}
	public P_UNKNOWN_PIPEQOSINFOHolder(final org.csapi.cm.P_UNKNOWN_PIPEQOSINFO initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_PIPEQOSINFOHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_PIPEQOSINFOHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_PIPEQOSINFOHelper.write(_out, value);
	}
}
