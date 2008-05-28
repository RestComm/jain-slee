package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_VPRPID"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_VPRPIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_ILLEGAL_VPRPID value;

	public P_ILLEGAL_VPRPIDHolder ()
	{
	}
	public P_ILLEGAL_VPRPIDHolder(final org.csapi.cm.P_ILLEGAL_VPRPID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_ILLEGAL_VPRPIDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_ILLEGAL_VPRPIDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_ILLEGAL_VPRPIDHelper.write(_out, value);
	}
}
