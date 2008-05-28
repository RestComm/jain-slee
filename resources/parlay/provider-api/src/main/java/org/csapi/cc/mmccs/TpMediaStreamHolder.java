package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMediaStream"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMediaStream value;

	public TpMediaStreamHolder ()
	{
	}
	public TpMediaStreamHolder(final org.csapi.cc.mmccs.TpMediaStream initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpMediaStreamHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpMediaStreamHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpMediaStreamHelper.write(_out, value);
	}
}
