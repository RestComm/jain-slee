package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of enum "TpConfPolicyType"
 *	@author JacORB IDL compiler 
 */

public final class TpConfPolicyTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpConfPolicyType value;

	public TpConfPolicyTypeHolder ()
	{
	}
	public TpConfPolicyTypeHolder (final TpConfPolicyType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpConfPolicyTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpConfPolicyTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpConfPolicyTypeHelper.write (out,value);
	}
}
