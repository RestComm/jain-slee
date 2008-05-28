package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwHeartBeatHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwHeartBeat value;
	public IpFwHeartBeatHolder()
	{
	}
	public IpFwHeartBeatHolder (final IpFwHeartBeat initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwHeartBeatHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwHeartBeatHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwHeartBeatHelper.write (_out,value);
	}
}
