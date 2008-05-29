package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwFaultManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwFaultManager value;
	public IpFwFaultManagerHolder()
	{
	}
	public IpFwFaultManagerHolder (final IpFwFaultManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwFaultManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwFaultManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwFaultManagerHelper.write (_out,value);
	}
}
