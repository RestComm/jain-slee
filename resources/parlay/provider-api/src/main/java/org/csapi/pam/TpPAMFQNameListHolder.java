package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMFQNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMFQNameListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpPAMFQNameListHolder ()
	{
	}
	public TpPAMFQNameListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMFQNameListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMFQNameListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMFQNameListHelper.write (out,value);
	}
}
