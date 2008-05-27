package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUlExtendedData"
 *	@author JacORB IDL compiler 
 */

public final class TpUlExtendedDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUlExtendedData value;

	public TpUlExtendedDataHolder ()
	{
	}
	public TpUlExtendedDataHolder(final org.csapi.mm.TpUlExtendedData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpUlExtendedDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpUlExtendedDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpUlExtendedDataHelper.write(_out, value);
	}
}
