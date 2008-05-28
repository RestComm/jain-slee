package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpConfSearchResult"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchResultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpConfSearchResult value;

	public TpConfSearchResultHolder ()
	{
	}
	public TpConfSearchResultHolder(final org.csapi.cc.cccs.TpConfSearchResult initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpConfSearchResultHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpConfSearchResultHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpConfSearchResultHelper.write(_out, value);
	}
}
