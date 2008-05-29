package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSubjectType"
 *	@author JacORB IDL compiler 
 */

public final class TpSubjectTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSubjectType value;

	public TpSubjectTypeHolder ()
	{
	}
	public TpSubjectTypeHolder (final TpSubjectType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSubjectTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSubjectTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSubjectTypeHelper.write (out,value);
	}
}
