package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppOAMHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppOAM value;
	public IpAppOAMHolder()
	{
	}
	public IpAppOAMHolder (final IpAppOAM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppOAMHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppOAMHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppOAMHelper.write (_out,value);
	}
}
