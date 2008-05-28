package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceContractDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceContractDescription value;

	public TpServiceContractDescriptionHolder ()
	{
	}
	public TpServiceContractDescriptionHolder(final org.csapi.fw.TpServiceContractDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceContractDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceContractDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceContractDescriptionHelper.write(_out, value);
	}
}
