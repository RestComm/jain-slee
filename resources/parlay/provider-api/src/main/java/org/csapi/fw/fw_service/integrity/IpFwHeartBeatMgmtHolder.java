package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwHeartBeatMgmtHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwHeartBeatMgmt value;
	public IpFwHeartBeatMgmtHolder()
	{
	}
	public IpFwHeartBeatMgmtHolder (final IpFwHeartBeatMgmt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwHeartBeatMgmtHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwHeartBeatMgmtHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwHeartBeatMgmtHelper.write (_out,value);
	}
}
