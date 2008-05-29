package org.csapi.cc.mpccs;
/**
 *	Generated from IDL definition of union "TpAppMultiPartyCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiPartyCallBackHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppMultiPartyCallBack value;

	public TpAppMultiPartyCallBackHolder ()
	{
	}
	public TpAppMultiPartyCallBackHolder (final TpAppMultiPartyCallBack initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppMultiPartyCallBackHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppMultiPartyCallBackHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppMultiPartyCallBackHelper.write (out, value);
	}
}
