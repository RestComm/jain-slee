package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagTimePeriod"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagTimePeriodHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagTimePeriod value;

	public TpNameDescrpTagTimePeriodHolder ()
	{
	}
	public TpNameDescrpTagTimePeriodHolder(final org.csapi.cm.TpNameDescrpTagTimePeriod initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagTimePeriodHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagTimePeriodHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagTimePeriodHelper.write(_out, value);
	}
}
