package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_ACCESS_DENIED"
 *	@author JacORB IDL compiler 
 */

public final class P_ACCESS_DENIEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_ACCESS_DENIED value;

	public P_ACCESS_DENIEDHolder ()
	{
	}
	public P_ACCESS_DENIEDHolder(final org.csapi.fw.P_ACCESS_DENIED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_ACCESS_DENIEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_ACCESS_DENIEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, value);
	}
}
