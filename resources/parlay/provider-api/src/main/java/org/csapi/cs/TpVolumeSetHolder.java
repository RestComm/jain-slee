package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpVolumeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpVolumeSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpVolume[] value;

	public TpVolumeSetHolder ()
	{
	}
	public TpVolumeSetHolder (final org.csapi.cs.TpVolume[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpVolumeSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpVolumeSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpVolumeSetHelper.write (out,value);
	}
}
