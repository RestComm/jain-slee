package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUlExtendedDataSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUlExtendedDataSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUlExtendedData[] value;

	public TpUlExtendedDataSetHolder ()
	{
	}
	public TpUlExtendedDataSetHolder (final org.csapi.mm.TpUlExtendedData[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUlExtendedDataSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUlExtendedDataSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUlExtendedDataSetHelper.write (out,value);
	}
}
