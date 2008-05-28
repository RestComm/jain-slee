package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpGeographicalPosition"
 *	@author JacORB IDL compiler 
 */

public final class TpGeographicalPositionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpGeographicalPosition value;

	public TpGeographicalPositionHolder ()
	{
	}
	public TpGeographicalPositionHolder(final org.csapi.mm.TpGeographicalPosition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpGeographicalPositionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpGeographicalPositionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpGeographicalPositionHelper.write(_out, value);
	}
}
