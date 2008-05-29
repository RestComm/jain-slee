package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcOAMHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcOAM value;
	public IpSvcOAMHolder()
	{
	}
	public IpSvcOAMHolder (final IpSvcOAM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcOAMHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcOAMHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcOAMHelper.write (_out,value);
	}
}
