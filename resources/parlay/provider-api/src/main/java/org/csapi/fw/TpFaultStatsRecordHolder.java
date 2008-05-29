package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpFaultStatsRecord"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsRecordHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpFaultStatsRecord value;

	public TpFaultStatsRecordHolder ()
	{
	}
	public TpFaultStatsRecordHolder(final org.csapi.fw.TpFaultStatsRecord initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpFaultStatsRecordHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpFaultStatsRecordHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpFaultStatsRecordHelper.write(_out, value);
	}
}
