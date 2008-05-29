package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagTimeOfDay"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagTimeOfDayHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagTimeOfDay value;

	public TpNameDescrpTagTimeOfDayHolder ()
	{
	}
	public TpNameDescrpTagTimeOfDayHolder(final org.csapi.cm.TpNameDescrpTagTimeOfDay initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.write(_out, value);
	}
}
