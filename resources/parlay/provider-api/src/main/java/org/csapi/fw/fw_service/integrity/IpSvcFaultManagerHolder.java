package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcFaultManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcFaultManager value;
	public IpSvcFaultManagerHolder()
	{
	}
	public IpSvcFaultManagerHolder (final IpSvcFaultManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcFaultManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcFaultManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcFaultManagerHelper.write (_out,value);
	}
}
