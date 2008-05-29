package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_CANT_DELETE_VPRP"
 *	@author JacORB IDL compiler 
 */

public final class P_CANT_DELETE_VPRPHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_CANT_DELETE_VPRP value;

	public P_CANT_DELETE_VPRPHolder ()
	{
	}
	public P_CANT_DELETE_VPRPHolder(final org.csapi.cm.P_CANT_DELETE_VPRP initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_CANT_DELETE_VPRPHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_CANT_DELETE_VPRPHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_CANT_DELETE_VPRPHelper.write(_out, value);
	}
}
