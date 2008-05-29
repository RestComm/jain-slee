package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpConfSearchCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpConfSearchCriteria value;

	public TpConfSearchCriteriaHolder ()
	{
	}
	public TpConfSearchCriteriaHolder(final org.csapi.cc.cccs.TpConfSearchCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpConfSearchCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpConfSearchCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpConfSearchCriteriaHelper.write(_out, value);
	}
}
