package org.csapi.fw;
/**
 *	Generated from IDL definition of union "TpDomainID"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDomainID value;

	public TpDomainIDHolder ()
	{
	}
	public TpDomainIDHolder (final TpDomainID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDomainIDHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDomainIDHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDomainIDHelper.write (out, value);
	}
}
