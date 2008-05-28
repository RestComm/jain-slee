package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpVolumeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpVolume value;

	public TpVolumeHolder ()
	{
	}
	public TpVolumeHolder(final org.csapi.cs.TpVolume initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpVolumeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpVolumeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpVolumeHelper.write(_out, value);
	}
}
