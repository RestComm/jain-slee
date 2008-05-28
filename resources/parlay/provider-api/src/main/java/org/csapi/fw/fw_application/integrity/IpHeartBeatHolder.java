package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpHeartBeatHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpHeartBeat value;
	public IpHeartBeatHolder()
	{
	}
	public IpHeartBeatHolder (final IpHeartBeat initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpHeartBeatHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpHeartBeatHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpHeartBeatHelper.write (_out,value);
	}
}
