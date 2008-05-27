package org.csapi.fw.fw_service.discovery;

/**
 *	Generated from IDL interface "IpFwServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwServiceDiscoveryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwServiceDiscovery value;
	public IpFwServiceDiscoveryHolder()
	{
	}
	public IpFwServiceDiscoveryHolder (final IpFwServiceDiscovery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwServiceDiscoveryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwServiceDiscoveryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwServiceDiscoveryHelper.write (_out,value);
	}
}
