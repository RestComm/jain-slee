package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationEmergencyRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocationEmergencyRequest value;

	public TpUserLocationEmergencyRequestHolder ()
	{
	}
	public TpUserLocationEmergencyRequestHolder(final org.csapi.mm.TpUserLocationEmergencyRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpUserLocationEmergencyRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpUserLocationEmergencyRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpUserLocationEmergencyRequestHelper.write(_out, value);
	}
}
