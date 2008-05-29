package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpFaultStats"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpFaultStats value;

	public TpFaultStatsHolder ()
	{
	}
	public TpFaultStatsHolder(final org.csapi.fw.TpFaultStats initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpFaultStatsHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpFaultStatsHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpFaultStatsHelper.write(_out, value);
	}
}
