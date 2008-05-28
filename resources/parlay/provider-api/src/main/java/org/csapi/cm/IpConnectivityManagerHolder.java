package org.csapi.cm;

/**
 *	Generated from IDL interface "IpConnectivityManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpConnectivityManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpConnectivityManager value;
	public IpConnectivityManagerHolder()
	{
	}
	public IpConnectivityManagerHolder (final IpConnectivityManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpConnectivityManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpConnectivityManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpConnectivityManagerHelper.write (_out,value);
	}
}
