package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpLoadManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpLoadManager value;
	public IpLoadManagerHolder()
	{
	}
	public IpLoadManagerHolder (final IpLoadManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpLoadManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpLoadManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpLoadManagerHelper.write (_out,value);
	}
}
