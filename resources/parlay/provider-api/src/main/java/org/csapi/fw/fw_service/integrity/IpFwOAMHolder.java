package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwOAMHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwOAM value;
	public IpFwOAMHolder()
	{
	}
	public IpFwOAMHolder (final IpFwOAM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwOAMHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwOAMHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwOAMHelper.write (_out,value);
	}
}
