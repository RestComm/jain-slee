package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadStatistic"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadStatistic value;

	public TpLoadStatisticHolder ()
	{
	}
	public TpLoadStatisticHolder(final org.csapi.fw.TpLoadStatistic initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpLoadStatisticHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpLoadStatisticHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpLoadStatisticHelper.write(_out, value);
	}
}
