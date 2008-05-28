package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMwatcherChangeType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMwatcherChangeTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMwatcherChangeType value;

	public TpPAMwatcherChangeTypeHolder ()
	{
	}
	public TpPAMwatcherChangeTypeHolder (final TpPAMwatcherChangeType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMwatcherChangeTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMwatcherChangeTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMwatcherChangeTypeHelper.write (out,value);
	}
}
