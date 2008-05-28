package org.csapi;

/**
 *	Generated from IDL interface "IpService"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpService value;
	public IpServiceHolder()
	{
	}
	public IpServiceHolder (final IpService initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceHelper.write (_out,value);
	}
}
