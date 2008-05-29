package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMediaStreamRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamRequestSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMediaStreamRequest[] value;

	public TpMediaStreamRequestSetHolder ()
	{
	}
	public TpMediaStreamRequestSetHolder (final org.csapi.cc.mmccs.TpMediaStreamRequest[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamRequestSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamRequestSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamRequestSetHelper.write (out,value);
	}
}
