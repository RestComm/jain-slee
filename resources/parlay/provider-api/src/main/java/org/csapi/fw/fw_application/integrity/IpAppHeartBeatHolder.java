package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppHeartBeatHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppHeartBeat value;
	public IpAppHeartBeatHolder()
	{
	}
	public IpAppHeartBeatHolder (final IpAppHeartBeat initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppHeartBeatHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppHeartBeatHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppHeartBeatHelper.write (_out,value);
	}
}
