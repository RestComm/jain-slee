package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpHeartBeatMgmtHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpHeartBeatMgmt value;
	public IpHeartBeatMgmtHolder()
	{
	}
	public IpHeartBeatMgmtHolder (final IpHeartBeatMgmt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpHeartBeatMgmtHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpHeartBeatMgmtHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpHeartBeatMgmtHelper.write (_out,value);
	}
}
