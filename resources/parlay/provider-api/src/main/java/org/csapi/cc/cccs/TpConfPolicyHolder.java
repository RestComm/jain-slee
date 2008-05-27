package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of union "TpConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpConfPolicyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpConfPolicy value;

	public TpConfPolicyHolder ()
	{
	}
	public TpConfPolicyHolder (final TpConfPolicy initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpConfPolicyHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpConfPolicyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpConfPolicyHelper.write (out, value);
	}
}
