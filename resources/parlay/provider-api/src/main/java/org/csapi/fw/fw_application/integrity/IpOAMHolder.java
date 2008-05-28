package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpOAMHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpOAM value;
	public IpOAMHolder()
	{
	}
	public IpOAMHolder (final IpOAM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpOAMHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpOAMHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpOAMHelper.write (_out,value);
	}
}
