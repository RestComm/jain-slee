package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCarrier"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCarrier value;

	public TpCarrierHolder ()
	{
	}
	public TpCarrierHolder(final org.csapi.cc.TpCarrier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCarrierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCarrierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCarrierHelper.write(_out, value);
	}
}
