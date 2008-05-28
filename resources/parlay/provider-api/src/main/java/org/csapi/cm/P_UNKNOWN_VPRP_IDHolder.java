package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_VPRP_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_VPRP_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_VPRP_ID value;

	public P_UNKNOWN_VPRP_IDHolder ()
	{
	}
	public P_UNKNOWN_VPRP_IDHolder(final org.csapi.cm.P_UNKNOWN_VPRP_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_VPRP_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_VPRP_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_VPRP_IDHelper.write(_out, value);
	}
}
