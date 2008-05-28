package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceContractIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServiceContractIDListHolder ()
	{
	}
	public TpServiceContractIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceContractIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceContractIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceContractIDListHelper.write (out,value);
	}
}
