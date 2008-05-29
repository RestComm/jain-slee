package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionSuperviseVolumeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionSuperviseVolume value;

	public TpDataSessionSuperviseVolumeHolder ()
	{
	}
	public TpDataSessionSuperviseVolumeHolder(final org.csapi.dsc.TpDataSessionSuperviseVolume initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.write(_out, value);
	}
}
