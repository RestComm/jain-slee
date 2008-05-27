package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDayOfWeek"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDayOfWeekHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagDayOfWeek value;

	public TpNameDescrpTagDayOfWeekHolder ()
	{
	}
	public TpNameDescrpTagDayOfWeekHolder(final org.csapi.cm.TpNameDescrpTagDayOfWeek initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.write(_out, value);
	}
}
