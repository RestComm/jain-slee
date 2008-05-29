package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFaultManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFaultManager value;
	public IpFaultManagerHolder()
	{
	}
	public IpFaultManagerHolder (final IpFaultManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFaultManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFaultManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFaultManagerHelper.write (_out,value);
	}
}
