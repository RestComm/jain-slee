package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMediaStreamSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMediaStream[] value;

	public TpMediaStreamSetHolder ()
	{
	}
	public TpMediaStreamSetHolder (final org.csapi.cc.mmccs.TpMediaStream[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamSetHelper.write (out,value);
	}
}
