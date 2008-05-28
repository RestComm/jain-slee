package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAuthDomain"
 *	@author JacORB IDL compiler 
 */

public final class TpAuthDomainHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpAuthDomain value;

	public TpAuthDomainHolder ()
	{
	}
	public TpAuthDomainHolder(final org.csapi.fw.TpAuthDomain initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpAuthDomainHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpAuthDomainHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpAuthDomainHelper.write(_out, value);
	}
}
