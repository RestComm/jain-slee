package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwLoadManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwLoadManager value;
	public IpFwLoadManagerHolder()
	{
	}
	public IpFwLoadManagerHolder (final IpFwLoadManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwLoadManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwLoadManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwLoadManagerHelper.write (_out,value);
	}
}
