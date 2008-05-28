package org.csapi.cm;

/**
 *	Generated from IDL interface "IpEnterpriseNetwork"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpEnterpriseNetworkHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpEnterpriseNetwork value;
	public IpEnterpriseNetworkHolder()
	{
	}
	public IpEnterpriseNetworkHolder (final IpEnterpriseNetwork initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpEnterpriseNetworkHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpEnterpriseNetworkHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpEnterpriseNetworkHelper.write (_out,value);
	}
}
