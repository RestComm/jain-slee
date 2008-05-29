package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMEventName value;

	public TpPAMEventNameHolder ()
	{
	}
	public TpPAMEventNameHolder (final TpPAMEventName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMEventNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMEventNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMEventNameHelper.write (out,value);
	}
}
