package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppFaultManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppFaultManager value;
	public IpAppFaultManagerHolder()
	{
	}
	public IpAppFaultManagerHolder (final IpAppFaultManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppFaultManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppFaultManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppFaultManagerHelper.write (_out,value);
	}
}
