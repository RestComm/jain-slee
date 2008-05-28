package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpLoadStatisticErrorList"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticErrorListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadStatisticError[] value;

	public TpLoadStatisticErrorListHolder ()
	{
	}
	public TpLoadStatisticErrorListHolder (final org.csapi.fw.TpLoadStatisticError[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticErrorListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticErrorListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticErrorListHelper.write (out,value);
	}
}
