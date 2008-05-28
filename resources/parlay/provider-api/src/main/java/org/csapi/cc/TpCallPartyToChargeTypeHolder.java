package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyToChargeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallPartyToChargeType value;

	public TpCallPartyToChargeTypeHolder ()
	{
	}
	public TpCallPartyToChargeTypeHolder (final TpCallPartyToChargeType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallPartyToChargeTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallPartyToChargeTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallPartyToChargeTypeHelper.write (out,value);
	}
}
