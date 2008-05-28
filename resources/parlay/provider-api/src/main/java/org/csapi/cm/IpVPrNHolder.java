package org.csapi.cm;

/**
 *	Generated from IDL interface "IpVPrN"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpVPrNHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpVPrN value;
	public IpVPrNHolder()
	{
	}
	public IpVPrNHolder (final IpVPrN initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpVPrNHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpVPrNHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpVPrNHelper.write (_out,value);
	}
}
