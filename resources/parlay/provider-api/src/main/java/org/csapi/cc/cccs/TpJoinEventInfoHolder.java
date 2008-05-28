package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpJoinEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpJoinEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpJoinEventInfo value;

	public TpJoinEventInfoHolder ()
	{
	}
	public TpJoinEventInfoHolder(final org.csapi.cc.cccs.TpJoinEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpJoinEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpJoinEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpJoinEventInfoHelper.write(_out, value);
	}
}
