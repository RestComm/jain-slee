package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpValidityInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpValidityInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpValidityInfo value;

	public TpValidityInfoHolder ()
	{
	}
	public TpValidityInfoHolder(final org.csapi.cm.TpValidityInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpValidityInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpValidityInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpValidityInfoHelper.write(_out, value);
	}
}
