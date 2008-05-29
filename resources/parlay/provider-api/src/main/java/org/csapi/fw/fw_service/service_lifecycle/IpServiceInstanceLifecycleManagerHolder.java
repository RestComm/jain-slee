package org.csapi.fw.fw_service.service_lifecycle;

/**
 *	Generated from IDL interface "IpServiceInstanceLifecycleManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceInstanceLifecycleManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceInstanceLifecycleManager value;
	public IpServiceInstanceLifecycleManagerHolder()
	{
	}
	public IpServiceInstanceLifecycleManagerHolder (final IpServiceInstanceLifecycleManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceInstanceLifecycleManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceInstanceLifecycleManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceInstanceLifecycleManagerHelper.write (_out,value);
	}
}
