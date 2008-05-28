package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyCategoryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallPartyCategory value;

	public TpCallPartyCategoryHolder ()
	{
	}
	public TpCallPartyCategoryHolder (final TpCallPartyCategory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallPartyCategoryHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallPartyCategoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallPartyCategoryHelper.write (out,value);
	}
}
