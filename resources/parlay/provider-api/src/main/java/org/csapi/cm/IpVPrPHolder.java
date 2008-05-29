package org.csapi.cm;

/**
 *	Generated from IDL interface "IpVPrP"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpVPrPHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpVPrP value;
	public IpVPrPHolder()
	{
	}
	public IpVPrPHolder (final IpVPrP initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpVPrPHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpVPrPHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpVPrPHelper.write (_out,value);
	}
}
