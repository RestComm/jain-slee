package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcLoadManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcLoadManager value;
	public IpSvcLoadManagerHolder()
	{
	}
	public IpSvcLoadManagerHolder (final IpSvcLoadManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcLoadManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcLoadManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcLoadManagerHelper.write (_out,value);
	}
}
