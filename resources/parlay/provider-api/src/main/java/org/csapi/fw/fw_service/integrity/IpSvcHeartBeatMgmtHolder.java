package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcHeartBeatMgmtHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcHeartBeatMgmt value;
	public IpSvcHeartBeatMgmtHolder()
	{
	}
	public IpSvcHeartBeatMgmtHolder (final IpSvcHeartBeatMgmt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcHeartBeatMgmtHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcHeartBeatMgmtHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcHeartBeatMgmtHelper.write (_out,value);
	}
}
