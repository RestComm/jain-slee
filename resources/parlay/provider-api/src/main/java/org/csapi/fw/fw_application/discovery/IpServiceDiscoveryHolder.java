package org.csapi.fw.fw_application.discovery;

/**
 *	Generated from IDL interface "IpServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceDiscoveryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceDiscovery value;
	public IpServiceDiscoveryHolder()
	{
	}
	public IpServiceDiscoveryHolder (final IpServiceDiscovery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceDiscoveryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceDiscoveryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceDiscoveryHelper.write (_out,value);
	}
}
