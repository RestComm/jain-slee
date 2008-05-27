package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpDomainIDType"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainIDTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDomainIDType value;

	public TpDomainIDTypeHolder ()
	{
	}
	public TpDomainIDTypeHolder (final TpDomainIDType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDomainIDTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDomainIDTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDomainIDTypeHelper.write (out,value);
	}
}
