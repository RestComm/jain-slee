package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCarrierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCarrier[] value;

	public TpCarrierSetHolder ()
	{
	}
	public TpCarrierSetHolder (final org.csapi.cc.TpCarrier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCarrierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCarrierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCarrierSetHelper.write (out,value);
	}
}
