package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallAdditionalEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAdditionalEventInfo value;

	public TpCallAdditionalEventInfoHolder ()
	{
	}
	public TpCallAdditionalEventInfoHolder (final TpCallAdditionalEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAdditionalEventInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAdditionalEventInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAdditionalEventInfoHelper.write (out, value);
	}
}
