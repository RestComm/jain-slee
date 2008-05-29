package org.csapi.cc.mpccs;
/**
 *	Generated from IDL definition of enum "TpAppMultiPartyCallBackRefType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiPartyCallBackRefTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppMultiPartyCallBackRefType value;

	public TpAppMultiPartyCallBackRefTypeHolder ()
	{
	}
	public TpAppMultiPartyCallBackRefTypeHolder (final TpAppMultiPartyCallBackRefType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppMultiPartyCallBackRefTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppMultiPartyCallBackRefTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppMultiPartyCallBackRefTypeHelper.write (out,value);
	}
}
