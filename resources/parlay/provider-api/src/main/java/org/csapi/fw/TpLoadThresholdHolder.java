package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadThreshold"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadThresholdHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadThreshold value;

	public TpLoadThresholdHolder ()
	{
	}
	public TpLoadThresholdHolder(final org.csapi.fw.TpLoadThreshold initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpLoadThresholdHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpLoadThresholdHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpLoadThresholdHelper.write(_out, value);
	}
}
