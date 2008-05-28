package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMediaStreamRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMediaStreamRequest value;

	public TpMediaStreamRequestHolder ()
	{
	}
	public TpMediaStreamRequestHolder(final org.csapi.cc.mmccs.TpMediaStreamRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpMediaStreamRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpMediaStreamRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpMediaStreamRequestHelper.write(_out, value);
	}
}
