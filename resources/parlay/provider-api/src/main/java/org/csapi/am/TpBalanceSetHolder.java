package org.csapi.am;

/**
 *	Generated from IDL definition of alias "TpBalanceSet"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpBalance[] value;

	public TpBalanceSetHolder ()
	{
	}
	public TpBalanceSetHolder (final org.csapi.am.TpBalance[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpBalanceSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpBalanceSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpBalanceSetHelper.write (out,value);
	}
}
