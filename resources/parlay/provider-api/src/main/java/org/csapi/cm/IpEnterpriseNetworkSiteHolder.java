package org.csapi.cm;

/**
 *	Generated from IDL interface "IpEnterpriseNetworkSite"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpEnterpriseNetworkSiteHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpEnterpriseNetworkSite value;
	public IpEnterpriseNetworkSiteHolder()
	{
	}
	public IpEnterpriseNetworkSiteHolder (final IpEnterpriseNetworkSite initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpEnterpriseNetworkSiteHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpEnterpriseNetworkSiteHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpEnterpriseNetworkSiteHelper.write (_out,value);
	}
}
