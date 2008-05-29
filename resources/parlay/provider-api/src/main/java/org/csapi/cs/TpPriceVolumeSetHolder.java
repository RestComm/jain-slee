package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpPriceVolumeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpPriceVolumeSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpPriceVolume[] value;

	public TpPriceVolumeSetHolder ()
	{
	}
	public TpPriceVolumeSetHolder (final org.csapi.cs.TpPriceVolume[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPriceVolumeSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPriceVolumeSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPriceVolumeSetHelper.write (out,value);
	}
}
