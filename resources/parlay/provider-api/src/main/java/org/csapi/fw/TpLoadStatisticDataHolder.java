package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadStatisticData"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadStatisticData value;

	public TpLoadStatisticDataHolder ()
	{
	}
	public TpLoadStatisticDataHolder(final org.csapi.fw.TpLoadStatisticData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpLoadStatisticDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpLoadStatisticDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpLoadStatisticDataHelper.write(_out, value);
	}
}
