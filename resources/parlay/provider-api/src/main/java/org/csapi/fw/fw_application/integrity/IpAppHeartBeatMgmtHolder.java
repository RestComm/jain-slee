package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppHeartBeatMgmtHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppHeartBeatMgmt value;
	public IpAppHeartBeatMgmtHolder()
	{
	}
	public IpAppHeartBeatMgmtHolder (final IpAppHeartBeatMgmt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppHeartBeatMgmtHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppHeartBeatMgmtHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppHeartBeatMgmtHelper.write (_out,value);
	}
}
