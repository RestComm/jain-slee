package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpPriceVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpPriceVolumeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpPriceVolume value;

	public TpPriceVolumeHolder ()
	{
	}
	public TpPriceVolumeHolder(final org.csapi.cs.TpPriceVolume initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpPriceVolumeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpPriceVolumeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpPriceVolumeHelper.write(_out, value);
	}
}
