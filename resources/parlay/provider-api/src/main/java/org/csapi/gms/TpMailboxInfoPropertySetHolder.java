package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpMailboxInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertySetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpMailboxInfoProperty[] value;

	public TpMailboxInfoPropertySetHolder ()
	{
	}
	public TpMailboxInfoPropertySetHolder (final org.csapi.gms.TpMailboxInfoProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMailboxInfoPropertySetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMailboxInfoPropertySetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMailboxInfoPropertySetHelper.write (out,value);
	}
}
