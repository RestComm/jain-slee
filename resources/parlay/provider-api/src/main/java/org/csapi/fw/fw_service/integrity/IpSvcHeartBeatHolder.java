package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcHeartBeatHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcHeartBeat value;
	public IpSvcHeartBeatHolder()
	{
	}
	public IpSvcHeartBeatHolder (final IpSvcHeartBeat initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcHeartBeatHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcHeartBeatHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcHeartBeatHelper.write (_out,value);
	}
}
