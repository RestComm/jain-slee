package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpResourceReservation"
 *	@author JacORB IDL compiler 
 */

public final class TpResourceReservationHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpResourceReservation value;

	public TpResourceReservationHolder ()
	{
	}
	public TpResourceReservationHolder(final org.csapi.cc.cccs.TpResourceReservation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpResourceReservationHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpResourceReservationHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpResourceReservationHelper.write(_out, value);
	}
}
