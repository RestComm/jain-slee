package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallPartyToChargeAdditionalInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeAdditionalInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallPartyToChargeAdditionalInfo value;

	public TpCallPartyToChargeAdditionalInfoHolder ()
	{
	}
	public TpCallPartyToChargeAdditionalInfoHolder (final TpCallPartyToChargeAdditionalInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallPartyToChargeAdditionalInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallPartyToChargeAdditionalInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallPartyToChargeAdditionalInfoHelper.write (out, value);
	}
}
