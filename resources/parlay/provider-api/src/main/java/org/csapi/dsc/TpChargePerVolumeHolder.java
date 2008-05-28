package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpChargePerVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerVolumeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpChargePerVolume value;

	public TpChargePerVolumeHolder ()
	{
	}
	public TpChargePerVolumeHolder(final org.csapi.dsc.TpChargePerVolume initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpChargePerVolumeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpChargePerVolumeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpChargePerVolumeHelper.write(_out, value);
	}
}
