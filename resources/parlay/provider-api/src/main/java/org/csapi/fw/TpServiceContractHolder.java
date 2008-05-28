package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceContract"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceContract value;

	public TpServiceContractHolder ()
	{
	}
	public TpServiceContractHolder(final org.csapi.fw.TpServiceContract initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceContractHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceContractHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceContractHelper.write(_out, value);
	}
}
