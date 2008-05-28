package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpCallSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpCallSuperviseVolumeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpCallSuperviseVolume value;

	public TpCallSuperviseVolumeHolder ()
	{
	}
	public TpCallSuperviseVolumeHolder(final org.csapi.cc.mmccs.TpCallSuperviseVolume initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.write(_out, value);
	}
}
