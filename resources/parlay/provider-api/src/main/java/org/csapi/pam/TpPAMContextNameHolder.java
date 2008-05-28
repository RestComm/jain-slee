package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMContextName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMContextName value;

	public TpPAMContextNameHolder ()
	{
	}
	public TpPAMContextNameHolder (final TpPAMContextName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMContextNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMContextNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMContextNameHelper.write (out,value);
	}
}
